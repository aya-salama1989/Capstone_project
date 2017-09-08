package com.jobease.www.jobease.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.database.FireBaseDataBaseHelper;
import com.jobease.www.jobease.fragments.FragmentInteractionListener;
import com.jobease.www.jobease.fragments.JobAppliersFragment;
import com.jobease.www.jobease.fragments.MyJobsFragment;
import com.jobease.www.jobease.models.Job;
import com.jobease.www.jobease.models.User;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.getMyJobs;

public class MyJobsActivity extends AppCompatActivity implements FragmentInteractionListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<Job> myJobs;
    private MyJobsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);
        ButterKnife.bind(this);
        myJobs = new ArrayList<>();
        setToolBar();
        if (savedInstanceState != null) {
            getSupportFragmentManager().getFragment(savedInstanceState, "my_fragment");
        } else {
            new MyAsyncTask().execute();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getSupportFragmentManager().putFragment(outState, "my_fragment", fragment);
        super.onSaveInstanceState(outState, outPersistentState);
    }


    private void setToolBar() {
        toolbar.setTitle(getString(R.string.my_jobs));
        setSupportActionBar(toolbar);
    }

    @Override
    public void onInteraction(Object... data) {
        if (data != null) {
            String jobData = (String) data[0];
            if (jobData.equalsIgnoreCase("update data")) {
                new MyAsyncTask().execute();
            } else {
                if (getResources().getBoolean(R.bool.twoPaneMode)) {
                    JobAppliersFragment jobAppliersFragment
                            = JobAppliersFragment.newInstance(jobData);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.details_placeHolder, jobAppliersFragment).commit();
                }
            }
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Integer, ArrayList<Job>>
            implements FireBaseDataBaseHelper.JobsDataChangeListener {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Job> doInBackground(Void... params) {
            getMyJobs(this, MyJobsActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Job> jobs) {
            super.onPostExecute(jobs);
            fragment = MyJobsFragment.newInstance(myJobs);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_placeHolder, fragment).commit();

        }

        @Override
        public void onJobsDataChange(ArrayList<Job> jobs, int type) {
            myJobs.addAll(jobs);
            if (getResources().getBoolean(R.bool.twoPaneMode)) {
                Gson gson = new Gson();
                Job job = myJobs.get(0);
                Map<String, User> users = job.getAppliedUsers();
                String json = gson.toJson(users);
                JobAppliersFragment jobAppliersFragment = JobAppliersFragment.newInstance(json.toString());
                getSupportFragmentManager().beginTransaction().replace(R.id.details_placeHolder, jobAppliersFragment).commit();
            }
        }
    }
}
