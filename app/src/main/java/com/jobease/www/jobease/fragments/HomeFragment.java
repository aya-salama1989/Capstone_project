package com.jobease.www.jobease.fragments;

import android.content.Intent;
import android.os.Bundle;
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

    private static final String IS_MULTIPANE = "multiPane";
    @BindView(R.id.rv_jobs)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private ArrayList<Job> mJobs;
    Gson gson = new Gson();


    private static FragmentInteractionListener mFragmentInteractionListener;

    //    private OnFragmentInteractionListener mListener;
    private JobsRecyclerAdapter jobsRecyclerAdapter;

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
        bindViews();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mJobs.isEmpty()) {
            getAllJobs(this);
        }
    }

    private void bindViews() {
        jobsRecyclerAdapter = new JobsRecyclerAdapter(mJobs, this);
        recyclerView.setAdapter(jobsRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fab.setOnClickListener((View view) -> {
            if (getArguments().getBoolean(IS_MULTIPANE)) {
                mFragmentInteractionListener.onInteraction("1");
            }else {
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

    }


    @Override
    public void onActionDone(Object... objects) {
        mJobs.clear();
        getAllJobs(this);
        jobsRecyclerAdapter.notifyDataSetChanged();
    }
}
