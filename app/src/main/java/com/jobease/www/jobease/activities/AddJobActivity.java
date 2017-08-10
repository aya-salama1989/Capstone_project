package com.jobease.www.jobease.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.models.Job;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.createJob;

public class AddJobActivity extends AppCompatActivity {

    @BindView(R.id.et_job_title)
    EditText etJobTitle;
    @BindView(R.id.et_job_salary)
    EditText etJobSalary;

    @BindView(R.id.et_job_currency)
    EditText etJobCurrency;

    @BindView(R.id.et_no_workers)
    EditText etJobNoOfWorkers;

    @BindView(R.id.et_job_address)
    EditText etJobAddress;

    @BindView(R.id.et_description)
    EditText etJobTDescription;

    @BindView(R.id.btn_add_job)
    Button btnAddJob;

    String jobTitle, jobSalary, jobCurrency, noOfWorkers, jobAddress, jobDescription;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        ButterKnife.bind(this);
        setToolBar();
        bindData();
    }

    private void setToolBar() {
        toolbar.setTitle(getString(R.string.add_job));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void bindData() {
        btnAddJob.setOnClickListener((View v) -> {
            getData();
            if (jobTitle.isEmpty()) {
                etJobTitle.setError(getString(R.string.empty_field));
            } else if (jobSalary.isEmpty()) {
                etJobSalary.setError(getString(R.string.empty_field));
            } else if (jobCurrency.isEmpty()) {
                etJobCurrency.setError(getString(R.string.empty_field));
            } else if (noOfWorkers.isEmpty()) {
                etJobNoOfWorkers.setError(getString(R.string.empty_field));
            } else if (noOfWorkers.equalsIgnoreCase("0")) {
                etJobNoOfWorkers.setError(getString(R.string.cant_zero));
            } else if (jobAddress.isEmpty()) {
                etJobAddress.setError(getString(R.string.empty_field));
            } else if (jobDescription.isEmpty()) {
                etJobTDescription.setError(getString(R.string.empty_field));
            } else {
                Job job = new Job();
                job.setAddress(jobAddress);
                job.setTitle(jobTitle);
                job.setNoOfWorkers(Integer.valueOf(noOfWorkers));
                job.setDescription(jobDescription);
                job.setSalary(jobSalary);
                job.setCurrency(jobCurrency);
                job.setUserId(new UserSettings().getUserID(AddJobActivity.this));
                //TODO: populate extra data
//                1- get current long and lat if user agreed or already saved before
                if (createJob(job)) {
                    emptyViews();
                } else {
                    Logging.longToast(AddJobActivity.this, getString(R.string.something_went_wrong));
                }
            }
        });
    }


    private void emptyViews() {
        etJobTitle.setText("");
        etJobSalary.setText("");
        etJobCurrency.setText("");
        etJobNoOfWorkers.setText("");
        etJobAddress.setText("");
        etJobTDescription.setText("");
        super.onBackPressed();
    }

    private void getData() {
        jobTitle = etJobTitle.getText().toString().trim();
        jobSalary = etJobSalary.getText().toString().trim();
        jobCurrency = etJobCurrency.getText().toString().trim();
        noOfWorkers = etJobNoOfWorkers.getText().toString().trim();
        jobAddress = etJobAddress.getText().toString().trim();
        jobDescription = etJobTDescription.getText().toString().trim();
    }
}
