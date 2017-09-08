package com.jobease.www.jobease.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.activities.AddJobActivity;
import com.jobease.www.jobease.activities.JobDetailsActivity;
import com.jobease.www.jobease.adapters.JobsRecyclerAdapter;
import com.jobease.www.jobease.models.Job;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.adapters.JobsRecyclerAdapter.ITEM_JOB;
import static com.jobease.www.jobease.adapters.JobsRecyclerAdapter.ITEM_SETTINGS;


public class HomeFragment extends Fragment implements JobsRecyclerAdapter.JobClickListener,
        DialogActionsListener {

    private static final String KEY_JOBS_ARRAY = "jobs_array";
    private static final String KEY_SELECTION_POSITION = "selection_position";


    @BindView(R.id.rv_jobs)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private FragmentInteractionListener mFragmentInteractionListener;

    private RecyclerView.LayoutManager linearLayoutManager;
    private JobsRecyclerAdapter jobsRecyclerAdapter;
    private int selectionPosition = 0;
    private ArrayList<Job> mJobs;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(ArrayList<Job> jobsArray) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_JOBS_ARRAY, jobsArray);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        if (savedInstanceState != null) {
            selectionPosition = savedInstanceState.getInt(KEY_SELECTION_POSITION);
            mJobs = savedInstanceState.getParcelableArrayList(KEY_JOBS_ARRAY);
            new Handler().postDelayed(() -> {
                if (selectionPosition != recyclerView.NO_POSITION & selectionPosition != 0) {
                    recyclerView.smoothScrollToPosition(selectionPosition);
                    recyclerView.findViewHolderForAdapterPosition(selectionPosition).itemView.setSelected(true);
                } else {
                    recyclerView.smoothScrollToPosition(((LinearLayoutManager) linearLayoutManager)
                            .findFirstCompletelyVisibleItemPosition());
                }
            }, 500);
        } else {
            mJobs = getArguments().getParcelableArrayList(KEY_JOBS_ARRAY);
        }
        bindViews();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mFragmentInteractionListener = (FragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mFragmentInteractionListener != null) {
            mFragmentInteractionListener = null;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SELECTION_POSITION, selectionPosition);
        outState.putParcelableArrayList(KEY_JOBS_ARRAY, mJobs);
        outState.putParcelable("layoutManagerState", linearLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }


    private void bindViews() {
        jobsRecyclerAdapter = new JobsRecyclerAdapter(mJobs, this);
        recyclerView.setAdapter(jobsRecyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        fab.setOnClickListener((View view) -> {
            if (getActivity().getResources().getBoolean(R.bool.twoPaneMode)) {
                mFragmentInteractionListener.onInteraction("1");
            } else {
                Intent intent = new Intent(getActivity(), AddJobActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onJobClick(int type, int position) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(mJobs.get(position));
        switch (type) {
            case ITEM_SETTINGS:
                if (mJobs.get(position).getUserId().equals(new UserSettings().getUserID(getActivity()))) {
                    PosterJobActions posterJobActions = new PosterJobActions();
                    posterJobActions.newInstance(this, jsonObject).show(getChildFragmentManager(), "Poster_Actions");
                } else {
                    SeekerJobActions seekerJobActions = new SeekerJobActions();
                    seekerJobActions.newInstance(this, jsonObject).show(getChildFragmentManager(), "Seeker_Actions");
                }
                break;
            case ITEM_JOB:
                selectionPosition = position;
                if (getResources().getBoolean(R.bool.twoPaneMode)) {
                    mFragmentInteractionListener.onInteraction(jsonObject);
                } else {
                    Intent intent = new Intent(getActivity(), JobDetailsActivity.class);
                    intent.putExtra("jobObject", jsonObject);
                    startActivity(intent);
                }
                break;
            default:
        }
    }


    @Override
    public void onActionDone(Object... objects) {
        mJobs.clear();
        mFragmentInteractionListener.onInteraction("update data");
    }


}
