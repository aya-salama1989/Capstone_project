package com.jobease.www.jobease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.AppSettings;
import com.jobease.www.jobease.adapters.SideMenuRecyclerAdapter;
import com.jobease.www.jobease.fragments.AddJobFragment;
import com.jobease.www.jobease.fragments.FragmentInteractionListener;
import com.jobease.www.jobease.fragments.HomeFragment;
import com.jobease.www.jobease.fragments.JobDetailsFragment;
import com.jobease.www.jobease.fragments.MyJobsFragment;
import com.jobease.www.jobease.fragments.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.fragments.HomeFragment.FRAGMENT_HOME;
import static com.jobease.www.jobease.fragments.MyJobsFragment.FRAGMENT_APPLIERS;
import static com.jobease.www.jobease.fragments.ProfileFragment.FRAGMENT_PROFILE;

public class HomeActivity extends AppCompatActivity
        implements SideMenuRecyclerAdapter.SideMenuClickListener, FragmentInteractionListener {

    @BindView(R.id.rv_side_menu)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    private Fragment fragment;
    private SideMenuRecyclerAdapter sideMenuRecyclerAdapter;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        MobileAds.initialize(this, getString(R.string.Add_mob_id));
        initMobileViews();
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (savedInstanceState != null) {
            getSupportFragmentManager().getFragment(savedInstanceState, "jobs_frag");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState, "jobs_frag", fragment);

    }

    private void initMobileViews() {
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


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment instanceof MyJobsFragment || fragment instanceof ProfileFragment) {
            super.onBackPressed();
        } else {
            logout();
        }
    }

    public void logout() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    @Override
    public void onSideItemCick(int position) {
        switch (position) {
            case 0:
                getFragment(FRAGMENT_HOME);
                break;
            case 1:
                if (getResources().getBoolean(R.bool.twoPaneMode)) {
                    Intent intent = new Intent(this, MyProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    getFragment(FRAGMENT_PROFILE);
                }
                break;
            case 2:
                if (getResources().getBoolean(R.bool.twoPaneMode)) {
                    Intent intent = new Intent(this, MyJobsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    getFragment(FRAGMENT_APPLIERS);
                }
                break;
            case 3:
                new AppSettings().setIsFirstLogin(this, true);
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("logOut", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            default:
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    private Fragment getFragment(int fragmentId) {

        switch (fragmentId) {
            case FRAGMENT_HOME:
                fragment = HomeFragment.newInstance(getResources().getBoolean(R.bool.twoPaneMode), this);
                break;
            case FRAGMENT_APPLIERS:
                fragment = MyJobsFragment.newInstance();
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

    @Override
    public void onInteraction(Object... data) {
        if (data != null) {
            if (getResources().getBoolean(R.bool.twoPaneMode)) {
                String jobData = (String) data[0];
                if (jobData.equalsIgnoreCase("1")) {
                    AddJobFragment addJobFragment = AddJobFragment.newInstance("");
                    getSupportFragmentManager().beginTransaction().replace(R.id.details_frag, addJobFragment).commit();
                } else {
                    JobDetailsFragment jobDetailsFragment = JobDetailsFragment.newInstance(jobData);
                    getSupportFragmentManager().beginTransaction().replace(R.id.details_frag, jobDetailsFragment).commit();
                }
            }
        }
    }
}
