package com.jobease.www.jobease.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.jobease.www.jobease.database.FireBaseDataBaseHelper;
import com.jobease.www.jobease.models.Job;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.adapters.JobsRecyclerAdapter.ITEM_JOB;
import static com.jobease.www.jobease.adapters.JobsRecyclerAdapter.ITEM_SETTINGS;
import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.getAllJobs;


public class HomeFragment extends Fragment implements JobsRecyclerAdapter.JobClickListener,
        FireBaseDataBaseHelper.JobsDataChangeListener, DialogActionsListener {

    public static final int FRAGMENT_HOME = 0;

    private static final String IS_MULTIPANE = "multiPane";
    private static final String KEY_RECYCLER_STATE = "state";
    private static final String KEY_DATA_ARRAY = "data";

    private static FragmentInteractionListener mFragmentInteractionListener;
    @BindView(R.id.rv_jobs)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Gson gson = new Gson();
    private ArrayList<Job> mJobs;
    private int selectionPosition;
    private LinearLayoutManager linearLayoutManager;
    //    private OnFragmentInteractionListener mListener;
    private JobsRecyclerAdapter jobsRecyclerAdapter;
    private Parcelable mListState;
    private Bundle mBundleRecyclerViewState;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(boolean param1, FragmentInteractionListener fragmentInteractionListener) {
        mFragmentInteractionListener = fragmentInteractionListener;
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_MULTIPANE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        mJobs = new ArrayList<>();

        if (savedInstanceState != null) {
            if (HomeFragment.this.isAdded()) {
                onRestoreDataBindViews();
                mListState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
                mJobs.addAll(savedInstanceState.getParcelableArrayList(KEY_DATA_ARRAY));
            }
        } else {
            bindViews();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mJobs.isEmpty()) {
            getAllJobs(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_RECYCLER_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(KEY_DATA_ARRAY, mJobs);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }


    private void onRestoreDataBindViews() {
        jobsRecyclerAdapter.notifyDataSetChanged();
        recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
    }

    private void bindViews() {
        jobsRecyclerAdapter = new JobsRecyclerAdapter(mJobs, this);
        recyclerView.setAdapter(jobsRecyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());


        recyclerView.setLayoutManager(linearLayoutManager);
        fab.setOnClickListener((View view) -> {
            if (getArguments().getBoolean(IS_MULTIPANE)) {
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
                if (getArguments().getBoolean(IS_MULTIPANE)) {
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
    public void onJobsDataChange(ArrayList<Job> jobs, int type) {
        mJobs.addAll(jobs);
        jobsRecyclerAdapter.notifyDataSetChanged();
        mFragmentInteractionListener.onInteraction(gson.toJson(mJobs.get(0)));
//        new Handler().postDelayed(() -> {
////            recyclerView.findViewHolderForAdapterPosition(0).itemView.performClick();
//
//        }, 800);
    }


    @Override
    public void onActionDone(Object... objects) {
        mJobs.clear();
        getAllJobs(this);
        jobsRecyclerAdapter.notifyDataSetChanged();
    }
}
