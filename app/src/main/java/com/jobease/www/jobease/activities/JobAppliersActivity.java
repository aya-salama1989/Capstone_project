package com.jobease.www.jobease.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.fragments.FragmentInteractionListener;
import com.jobease.www.jobease.fragments.JobAppliersFragment;

import butterknife.ButterKnife;

public class JobAppliersActivity extends AppCompatActivity implements FragmentInteractionListener {


    private JobAppliersFragment jobAppliersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_appliers);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            getSupportFragmentManager().getFragment(savedInstanceState, "jobAppliersFragment");
        } else {
            String appliersList = getIntent().getExtras().getString("appliersList");
            jobAppliersFragment = JobAppliersFragment.newInstance(appliersList);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeHolder, jobAppliersFragment).commit();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getSupportFragmentManager().putFragment(outState, "jobAppliersFragment", jobAppliersFragment);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onInteraction(Object... data) {

    }
}
