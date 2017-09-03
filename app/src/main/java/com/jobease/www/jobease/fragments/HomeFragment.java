package com.jobease.www.jobease.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
    private static final String KEY_RECYCLER_STATE = "state";
    private static final String KEY_DATA_ARRAY = "data";
    private static final String KEY_POSITION = "position";


    private static FragmentInteractionListener mFragmentInteractionListener;
    @BindView(R.id.rv_jobs)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Gson gson = new Gson();
    Parcelable layoutManagerStateInstance;
    private ArrayList<Job> mJobs;
    private GridLayoutManager linearLayoutManager;
    private JobsRecyclerAdapter jobsRecyclerAdapter;
    private int mPosition = recyclerView.NO_POSITION;
    private int scrollPosition = 0;
    private int Pos = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(FragmentInteractionListener fragmentInteractionListener) {
        mFragmentInteractionListener = fragmentInteractionListener;
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        mJobs = new ArrayList<>();
        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt(KEY_POSITION);
//            recyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerStateInstance);
        }

        if (layoutManagerStateInstance != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerStateInstance);
        }
        bindViews();
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
        outState.putInt(KEY_POSITION, mPosition);
        outState.putInt("pos", linearLayoutManager.findFirstVisibleItemPosition());

        outState.putParcelable(KEY_RECYCLER_STATE, layoutManagerStateInstance);
        outState.putParcelableArrayList(KEY_DATA_ARRAY, mJobs);
        super.onSaveInstanceState(outState);


    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        Logging.log("onActivityCreated");
//
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
////            recyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerStateInstance);
//
//            Pos = savedInstanceState.getInt("pos");
//        }
//
//    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        Logging.log("onViewStateRestored");
//
//        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState != null) {
//            int pos = savedInstanceState.getInt(KEY_POSITION);
//            recyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerStateInstance);
//        }
//    }


    private void bindViews() {
        jobsRecyclerAdapter = new JobsRecyclerAdapter(mJobs, this);
        recyclerView.setAdapter(jobsRecyclerAdapter);

        linearLayoutManager = new GridLayoutManager(getActivity(), 1);
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
                mPosition = position;
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
        mJobs.addAll(jobs);
        jobsRecyclerAdapter.notifyDataSetChanged();

        if (Pos != 0) {
            new Handler().postDelayed(() -> {
                recyclerView.smoothScrollToPosition(Pos);
//                recyclerView.findViewHolderForAdapterPosition(Pos).itemView.performClick();
            }, 500);
        }

        if (mPosition == recyclerView.NO_POSITION) mPosition = 0;
        mFragmentInteractionListener.onInteraction(gson.toJson(mJobs.get(mPosition)));
    }


    @Override
    public void onActionDone(Object... objects) {
        mJobs.clear();
        getAllJobs(this);
        jobsRecyclerAdapter.notifyDataSetChanged();
    }


}
