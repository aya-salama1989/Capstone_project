package com.jobease.www.jobease.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.fragments.ProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileActivity extends AppCompatActivity {
    private String userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (getIntent().getExtras() != null) {
            userData = getIntent().getExtras().getString("userData");
        }

        ProfileFragment profileFragment = ProfileFragment.newInstance("other", userData);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeHolder, profileFragment).commit();
    }
}
