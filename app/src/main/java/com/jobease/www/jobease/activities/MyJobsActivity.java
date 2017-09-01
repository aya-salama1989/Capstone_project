package com.jobease.www.jobease.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.fragments.MyJobsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyJobsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);
        ButterKnife.bind(this);
        setToolBar();
        MyJobsFragment fragment = MyJobsFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_placeHolder, fragment).commit();
    }

    private void setToolBar(){
        toolbar.setTitle(getString(R.string.my_jobs));
        setSupportActionBar(toolbar);
    }
}
