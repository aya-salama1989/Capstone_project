package com.jobease.www.jobease.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.activities.HomeActivity;
import com.jobease.www.jobease.database.FireBaseDataBaseHelper;
import com.jobease.www.jobease.models.Job;
import com.jobease.www.jobease.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.applyToAJob;
import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.getUser;


public class JobDetailsFragment extends Fragment implements FireBaseDataBaseHelper.UserGettingListener,
        FireBaseDataBaseHelper.ApplyToJobListener {

    private static final String JOB_DATA = "job_data";

    @BindView(R.id.job_title)
    TextView jobTitleTxtVue;

    @BindView(R.id.job_location)
    TextView jobLocationTxtVue;

    @BindView(R.id.job_salary)
    TextView jobSalaryTxtVue;

    @BindView(R.id.job_description)
    TextView jobDescriptionTxtVue;


    @BindView(R.id.btn_apply)
    Button btnApply;


    private View v;
    private JSONObject jsonObject;
    private Job job;

    public JobDetailsFragment() {
        // Required empty public constructor
    }


    public static JobDetailsFragment newInstance(String jobData) {
        JobDetailsFragment fragment = new JobDetailsFragment();
        Bundle args = new Bundle();
        args.putString(JOB_DATA, jobData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_job_details, container, false);
        ButterKnife.bind(this, v);
        try {
            String shopData = getArguments().getString(JOB_DATA);
            jsonObject = new JSONObject(shopData);
            job = new Job(jsonObject);
        } catch (JSONException e) {
            Logging.log(e.getMessage());
        }
//        setToolBar();
        initViews();
        return v;
    }


    private void initViews() {
        jobTitleTxtVue.setText(job.getTitle());
        jobLocationTxtVue.setText(job.getAddress());
        jobSalaryTxtVue.setText(job.getSalary());
        jobDescriptionTxtVue.setText(job.getDescription());
        btnApply.setOnClickListener((View v) -> {
            getUser(new UserSettings().getUserID(getActivity()), this);
        });
    }

    @Override
    public void onUserGot(User user) {
        applyToAJob(job, user, this);

    }

    @Override
    public void onApplyToJob(boolean isSuccessful) {
        if (isSuccessful) {
            Logging.shortToast(getActivity(), getString(R.string.applied_successfully));
            
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }
}
