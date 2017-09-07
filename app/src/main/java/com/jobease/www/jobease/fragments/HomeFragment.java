package com.jobease.www.jobease.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
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

    private static final String KEY_DATA_ARRAY = "data";
    private static final String KEY_SCROLLPOSITION = "scroll_position";
    private static final String KEY_SELECTIONPOSITION = "selection_position";
    private static final String KEY_LAYOUT_MANAGER = "layout_manager_state";


    private static FragmentInteractionListener mFragmentInteractionListener;
    @BindView(R.id.rv_jobs)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Gson gson = new Gson();
    private ArrayList<Job> mJobs;
    private RecyclerView.LayoutManager linearLayoutManager;
    private JobsRecyclerAdapter jobsRecyclerAdapter;
    private int scrollPosition = 0, selectionPosition = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public static void setListener(FragmentInteractionListener fragmentInteractionListener){
        mFragmentInteractionListener = fragmentInteractionListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logging.log("onCreateView");
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        mJobs = new ArrayList<>();
        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt(KEY_SCROLLPOSITION);
            selectionPosition = savedInstanceState.getInt(KEY_SELECTIONPOSITION);
            mJobs.addAll(savedInstanceState.getParcelableArrayList(KEY_DATA_ARRAY));
            bindViewsAfterRotation(savedInstanceState.getParcelableArrayList(KEY_DATA_ARRAY),
                    scrollPosition, selectionPosition);
        } else {
            bindViews();
        }

        fab.setOnClickListener((View view) -> {
            if (getActivity().getResources().getBoolean(R.bool.twoPaneMode)) {
                mFragmentInteractionListener.onInteraction("1");
            } else {
                Intent intent = new Intent(getActivity(), AddJobActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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
        int firstVisiblePosition = ((LinearLayoutManager) linearLayoutManager).findFirstVisibleItemPosition();
        outState.putInt(KEY_SCROLLPOSITION, firstVisiblePosition);
        outState.putInt(KEY_SELECTIONPOSITION, selectionPosition);
        outState.putParcelableArrayList(KEY_DATA_ARRAY, mJobs);
        outState.putParcelable(KEY_LAYOUT_MANAGER, linearLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    private void bindViewsAfterRotation(ArrayList<Job> jobs, int scrollPosition, int selectionPosition) {
        jobsRecyclerAdapter = new JobsRecyclerAdapter(jobs, this);
        recyclerView.setAdapter(jobsRecyclerAdapter);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        if (scrollPosition != recyclerView.NO_POSITION)
            recyclerView.smoothScrollToPosition(scrollPosition);
        //TODO-2 : throws a nullPointer exception when performing a click or a selection over an item programmatically
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                recyclerView.findViewHolderForAdapterPosition(selectionPosition).itemView.setSelected(true);
//            }
//        }, 500);
    }


    private void bindViews() {
        jobsRecyclerAdapter = new JobsRecyclerAdapter(mJobs, this);
        recyclerView.setAdapter(jobsRecyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
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
    public void onJobsDataChange(ArrayList<Job> jobs, int type) {
        //gets called 8 times so stupid -_-
        mJobs.addAll(jobs);
        jobsRecyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActionDone(Object... objects) {
        mJobs.clear();
        getAllJobs(this);
        jobsRecyclerAdapter.notifyDataSetChanged();
    }


}
