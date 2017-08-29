package com.jobease.www.jobease.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.activities.HomeActivity;
import com.jobease.www.jobease.models.Job;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.createJob;
import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.editAJob;


public class AddJobFragment extends Fragment {

    private static final String JOB_OBJECT = "job_object";
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
    private View v;
    private String object;
    private JSONObject jsonObject;
    private Job job;

    public AddJobFragment() {
        // Required empty public constructor
    }


    public static AddJobFragment newInstance(String jobObject) {
        AddJobFragment fragment = new AddJobFragment();
        Bundle args = new Bundle();
        args.putString(JOB_OBJECT, jobObject);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_job, container, false);
        ButterKnife.bind(this, v);
        if(getArguments().getString(JOB_OBJECT)!=null||!getArguments().getString(JOB_OBJECT).isEmpty()){
            setVisibility(btnEditJob);
            try {
                jsonObject = new JSONObject(getArguments().getString(JOB_OBJECT));
                job = new Job(jsonObject);
                setData(job);

            } catch (JSONException e) {
                Logging.log(e.getMessage());
            }
        }else{
            setVisibility(btnAddJob);

        }

        bindData();
        return v;
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
                job.setUserId(new UserSettings().getUserID(getActivity()));
                //TODO: populate extra data
//                1- get current long and lat if user agreed or already saved before
                editAJob(job);
                emptyViews();
                startActivity(new Intent(getActivity(), HomeActivity.class));

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
                job.setUserId(new UserSettings().getUserID(getActivity()));
//                Map<String, User> map = new HashMap<>();
//                map.put("ay haga", new User());
//                job.setAppliedUsers(map);
                //TODO: populate extra data
//                1- get current long and lat if user agreed or already saved before
                createJob(job);
                emptyViews();
//                super.onBackPressed();
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
