package com.jobease.www.jobease.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.AppSettings;
import com.jobease.www.jobease.adapters.SideMenuRecyclerAdapter;
import com.jobease.www.jobease.fragments.HomeFragment;
import com.jobease.www.jobease.fragments.JobsFragment;
import com.jobease.www.jobease.fragments.MyJobsFragment;
import com.jobease.www.jobease.fragments.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.fragments.HomeFragment.FRAGMENT_HOME;
import static com.jobease.www.jobease.fragments.JobsFragment.FRAGMENT_JOBS;
import static com.jobease.www.jobease.fragments.MyJobsFragment.FRAGMENT_APPLIERS;
import static com.jobease.www.jobease.fragments.ProfileFragment.FRAGMENT_PROFILE;

public class HomeActivity extends AppCompatActivity
        implements SideMenuRecyclerAdapter.SideMenuClickListener {

    @BindView(R.id.rv_side_menu)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;


    private SideMenuRecyclerAdapter sideMenuRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        getFragment(FRAGMENT_HOME);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        sideMenuRecyclerAdapter = new SideMenuRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sideMenuRecyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        if (new AppSettings().getIsFirstLogin(this)) {
            new Handler().postDelayed(() -> {
                drawer.openDrawer(GravityCompat.START);
            }, 800);
        }
    }


//    @Override
//    public void onBackPressed() {
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }


    @Override
    public void onSideItemCick(int position) {
        switch (position) {
            case 0:
                getFragment(FRAGMENT_HOME);
                break;
            case 1:
                getFragment(FRAGMENT_PROFILE);
                break;
            case 2:
                getFragment(FRAGMENT_APPLIERS);
                break;
            case 3:
                getFragment(FRAGMENT_JOBS);
                break;
            case 4:
//TODO: Logout App unAuth user
                break;
            default:
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private Fragment getFragment(int fragmentId) {
        Fragment fragment = null;
        switch (fragmentId) {
            case FRAGMENT_HOME:
                fragment = HomeFragment.newInstance("", "");
                break;
            case FRAGMENT_APPLIERS:
                fragment = MyJobsFragment.newInstance("", "");
                break;
            case FRAGMENT_JOBS:
                fragment = JobsFragment.newInstance("", "");
                break;
            case FRAGMENT_PROFILE:
                fragment = ProfileFragment.newInstance("home", "");
                break;
            default:
                throw new UnsupportedOperationException("UnSupportedFragmentId: " + fragmentId);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragments_holder, fragment)
                .setTransition(android.R.anim.bounce_interpolator).commit();
        return fragment;
    }
}
