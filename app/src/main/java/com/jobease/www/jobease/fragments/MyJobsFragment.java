package com.jobease.www.jobease.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.activities.JobAppliersActivity;
import com.jobease.www.jobease.adapters.MyJobsRecyclerAdapter;
import com.jobease.www.jobease.models.Job;
import com.jobease.www.jobease.models.User;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyJobsFragment extends Fragment implements
        DialogActionsListener, MyJobsRecyclerAdapter.OnMyJobClickListener {
    private static final String MY_JOBS_DATA = "my_jobs_data";
    private static final String KEY_SELECTION_POSITION = "selection_position";
    @BindView(R.id.rv_appliers)
    RecyclerView recyclerView;
    private ArrayList<Job> myJobs;
    private MyJobsRecyclerAdapter myJobsRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int selectionPosition;

    private FragmentInteractionListener fragmentInteractionListener;

    public MyJobsFragment() {
        // Required empty public constructor
    }


    public static MyJobsFragment newInstance(ArrayList<Job> jobs) {
        MyJobsFragment fragment = new MyJobsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MY_JOBS_DATA, jobs);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appliers, container, false);
        ButterKnife.bind(this, v);
        myJobs = new ArrayList<>();
        if (savedInstanceState != null) {
            selectionPosition = savedInstanceState.getInt(KEY_SELECTION_POSITION);
            myJobs = savedInstanceState.getParcelableArrayList(MY_JOBS_DATA);

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
            myJobs = getArguments().getParcelableArrayList(MY_JOBS_DATA);
        }
        bindData();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(KEY_SELECTION_POSITION, firstVisiblePosition);
        outState.putParcelableArrayList(MY_JOBS_DATA, myJobs);
        super.onSaveInstanceState(outState);
    }


    private void bindData() {
        myJobsRecyclerAdapter = new MyJobsRecyclerAdapter(this, myJobs);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(myJobsRecyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentInteractionListener = (FragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (fragmentInteractionListener != null) {
            fragmentInteractionListener = null;
        }
    }

    @Override
    public void OnMyJobItemClick(int type, int position) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(myJobs.get(position));
        if (type == 0) {
            PosterJobActions posterJobActions = new PosterJobActions();
            posterJobActions.newInstance(this, jsonObject).show(getChildFragmentManager(), "Poster_Actions");
        } else {
            selectionPosition = position;
            Map<String, User> users = myJobs.get(position).getAppliedUsers();
            String json = gson.toJson(users);
            Intent intent = new Intent(getActivity(), JobAppliersActivity.class);
            intent.putExtra("appliersList", json.toString());
            startActivity(intent);
        }
    }

    @Override
    public void onActionDone(Object... objects) {
        myJobs.clear();
        fragmentInteractionListener.onInteraction("update data");
    }


}
