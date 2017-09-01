package com.jobease.www.jobease.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.fragments.ProfileFragment;

public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ProfileFragment profileFragment = ProfileFragment.newInstance("home", "");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_placeHolder, profileFragment).commit();
    }
}
