package com.jobease.www.jobease.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.jobease.www.jobease.database.FireBaseDataBaseHelper;
import com.jobease.www.jobease.models.Job;
import com.jobease.www.jobease.models.User;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.getMyJobs;


public class MyJobsFragment extends Fragment implements
        FireBaseDataBaseHelper.JobsDataChangeListener, MyJobsRecyclerAdapter.OnMyJobClickListener {
    public static final int FRAGMENT_APPLIERS = 2;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.rv_appliers)
    RecyclerView recyclerView;
    private ArrayList<Job> myJobs;
    private MyJobsRecyclerAdapter myJobsRecyclerAdapter;


    public MyJobsFragment() {
        // Required empty public constructor
    }


    public static MyJobsFragment newInstance(String param1, String param2) {
        MyJobsFragment fragment = new MyJobsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appliers, container, false);
        ButterKnife.bind(this, v);
        myJobs = new ArrayList<>();
        bindData();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myJobs.isEmpty()) {
            getData();
        }
    }

    private void bindData() {
        myJobsRecyclerAdapter = new MyJobsRecyclerAdapter(this, myJobs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(myJobsRecyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getData() {
        getMyJobs(this, getActivity());
    }


    @Override
    public void onJobsDataChange(ArrayList<Job> jobs, int type) {
        myJobs.addAll(jobs);
        myJobsRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnMyJobItemClick(int type, int position) {
        if (type == 0) {
//settings
        } else {
            Map<String, User> users = myJobs.get(position).getAppliedUsers();
            Gson gson = new Gson();
            String json = gson.toJson(users);
            Intent intent = new Intent(getActivity(), JobAppliersActivity.class);
            intent.putExtra("appliersList", json.toString());
            startActivity(intent);
        }
    }
}
