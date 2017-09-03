package com.jobease.www.jobease.fragments;

import android.content.Intent;
import android.os.AsyncTask;
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
        MyJobsRecyclerAdapter.OnMyJobClickListener, DialogActionsListener {
    public static final int FRAGMENT_APPLIERS = 2;

    @BindView(R.id.rv_appliers)
    RecyclerView recyclerView;
    private ArrayList<Job> myJobs;
    private MyJobsRecyclerAdapter myJobsRecyclerAdapter;


    public MyJobsFragment() {
        // Required empty public constructor
    }


    public static MyJobsFragment newInstance() {
        MyJobsFragment fragment = new MyJobsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appliers, container, false);
        ButterKnife.bind(this, v);
        myJobs = new ArrayList<>();
        bindData();
        new MyAsyncTask().execute();
        return v;
    }


    private void bindData() {
        myJobsRecyclerAdapter = new MyJobsRecyclerAdapter(this, myJobs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(myJobsRecyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void OnMyJobItemClick(int type, int position) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(myJobs.get(position));

        if (type == 0) {
            PosterJobActions posterJobActions = new PosterJobActions();
            posterJobActions.newInstance(this, jsonObject).show(getChildFragmentManager(), "Poster_Actions");
        } else {
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
        new MyAsyncTask().execute();
        myJobsRecyclerAdapter.notifyDataSetChanged();
    }

    class MyAsyncTask extends AsyncTask<Void, Integer, ArrayList<Job>>
            implements FireBaseDataBaseHelper.JobsDataChangeListener {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Job> doInBackground(Void... params) {
            getMyJobs(this, getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Job> jobs) {
            super.onPostExecute(jobs);
        }


        @Override
        public void onJobsDataChange(ArrayList<Job> jobs, int type) {
            myJobs.addAll(jobs);
            myJobsRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
