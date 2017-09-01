package com.jobease.www.jobease.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.fragments.JobDetailsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobDetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String data;
    private JobDetailsFragment jobDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            data = getIntent().getExtras().getString("jobObject");
        } else {
            Logging.log("Mafesh Dataaaa");
        }
        setToolBar();

            jobDetailsFragment = JobDetailsFragment.newInstance(data);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeHolder, jobDetailsFragment).commit();


    }



    private void setToolBar() {
        toolbar.setTitle(getString(R.string.job_details));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
