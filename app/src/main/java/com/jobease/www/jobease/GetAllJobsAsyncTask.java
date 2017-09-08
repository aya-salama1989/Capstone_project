package com.jobease.www.jobease;

import android.os.AsyncTask;

import com.jobease.www.jobease.models.Job;

import java.util.ArrayList;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.getAllJobs;

/**
 * Created by Dell on 04/09/2017.
 */

public class GetAllJobsAsyncTask extends AsyncTask<Void, Integer, ArrayList<Job>> {
    private ArrayList<Job> mJobs;
    private GetAllJobsListener getAllJobsListener;

    public GetAllJobsAsyncTask(GetAllJobsListener getAllJobsListener) {
        this.getAllJobsListener = getAllJobsListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Job> doInBackground(Void... params) {
        mJobs = new ArrayList<>();
        mJobs.addAll(getAllJobs());
        return mJobs;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Job> jobs) {
        super.onPostExecute(jobs);
        getAllJobsListener.getAllJobs(mJobs);
    }


    public interface GetAllJobsListener {
        void getAllJobs(ArrayList<Job> jobs);
    }
}
