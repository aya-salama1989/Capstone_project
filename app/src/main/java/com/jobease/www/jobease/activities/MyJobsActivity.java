package com.jobease.www.jobease.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.fragments.MyJobsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyJobsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MyJobsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);
        ButterKnife.bind(this);
        setToolBar();
        if(savedInstanceState!=null){
            getSupportFragmentManager().getFragment(savedInstanceState, "my_fragment");
        }else {
            fragment = MyJobsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_placeHolder, fragment).commit();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getSupportFragmentManager().putFragment(outState,"my_fragment",fragment);
        super.onSaveInstanceState(outState, outPersistentState);
    }



    private void setToolBar(){
        toolbar.setTitle(getString(R.string.my_jobs));
        setSupportActionBar(toolbar);
    }
}
