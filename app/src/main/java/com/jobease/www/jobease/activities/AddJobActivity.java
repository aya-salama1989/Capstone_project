package com.jobease.www.jobease.activities;

import android.content.Intent;
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
import com.jobease.www.jobease.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.createJob;
import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.editAJob;

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

    @BindView(R.id.btn_edit_job)
    Button btnEditJob;


    String jobTitle, jobSalary, jobCurrency, noOfWorkers, jobAddress, jobDescription;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String object;
    private JSONObject jsonObject;
    private Job job;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            setVisibility(btnEditJob);
            object = getIntent().getStringExtra("jobObject");
            try {
                jsonObject = new JSONObject(object);
                job = new Job(jsonObject);
                setData(job);

            } catch (JSONException e) {
                Logging.log(e.getMessage());
            }
        } else {
            setVisibility(btnAddJob);
        }
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
        btnEditJob.setOnClickListener((View v) -> {

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
                job.setAddress(jobAddress);
                job.setTitle(jobTitle);
                job.setNoOfWorkers(Integer.valueOf(noOfWorkers));
                job.setDescription(jobDescription);
                job.setSalary(jobSalary);
                job.setCurrency(jobCurrency);
                job.setUserId(new UserSettings().getUserID(AddJobActivity.this));
                //TODO: populate extra data
//                1- get current long and lat if user agreed or already saved before
                editAJob(job);
                emptyViews();
                startActivity(new Intent(AddJobActivity.this, HomeActivity.class));

            }
        });

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
//                Map<String, User> map = new HashMap<>();
//                map.put("ay haga", new User());
//                job.setAppliedUsers(map);
                //TODO: populate extra data
//                1- get current long and lat if user agreed or already saved before
                if (createJob(job)) {
                    emptyViews();
                    super.onBackPressed();

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
    }

    private void getData() {
        jobTitle = etJobTitle.getText().toString().trim();
        jobSalary = etJobSalary.getText().toString().trim();
        jobCurrency = etJobCurrency.getText().toString().trim();
        noOfWorkers = etJobNoOfWorkers.getText().toString().trim();
        jobAddress = etJobAddress.getText().toString().trim();
        jobDescription = etJobTDescription.getText().toString().trim();
    }

    private void setData(Job job) {
        etJobTitle.setText(job.getTitle());
        etJobSalary.setText(job.getSalary());
        etJobCurrency.setText(job.getCurrency());
        etJobNoOfWorkers.setText("" + job.getNoOfWorkers());
        etJobAddress.setText(job.getAddress());
        etJobTDescription.setText(job.getDescription());
    }

    private void setVisibility(View v) {
        btnAddJob.setVisibility(View.INVISIBLE);
        btnEditJob.setVisibility(View.INVISIBLE);
        v.setVisibility(View.VISIBLE);
    }
}
