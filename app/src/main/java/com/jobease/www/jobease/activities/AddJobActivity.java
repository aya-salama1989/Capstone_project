package com.jobease.www.jobease.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.fragments.AddJobFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddJobActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String object;
    private AddJobFragment addJobFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            object = getIntent().getStringExtra("jobObject");
            addJobFragment = AddJobFragment.newInstance(object);
        } else {
            addJobFragment = AddJobFragment.newInstance("");
        }

        if (savedInstanceState != null) {
            getSupportFragmentManager().getFragment(savedInstanceState, "add_job");
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeHolder, addJobFragment).commit();
        }
        setToolBar();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState, "add_job", addJobFragment);
    }

    private void setToolBar() {
        toolbar.setTitle(getString(R.string.add_job));
        setSupportActionBar(toolbar);

    }


}
