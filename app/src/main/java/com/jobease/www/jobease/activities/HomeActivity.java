package com.jobease.www.jobease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.gson.Gson;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.AppSettings;
import com.jobease.www.jobease.adapters.SideMenuRecyclerAdapter;
import com.jobease.www.jobease.database.FireBaseDataBaseHelper;
import com.jobease.www.jobease.fragments.AddJobFragment;
import com.jobease.www.jobease.fragments.FragmentInteractionListener;
import com.jobease.www.jobease.fragments.HomeFragment;
import com.jobease.www.jobease.fragments.JobDetailsFragment;
import com.jobease.www.jobease.fragments.MyJobsFragment;
import com.jobease.www.jobease.fragments.ProfileFragment;
import com.jobease.www.jobease.models.Job;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.getAllJobs;
import static com.jobease.www.jobease.fragments.ProfileFragment.FRAGMENT_PROFILE;

public class HomeActivity extends AppCompatActivity
        implements FireBaseDataBaseHelper.JobsDataChangeListener,
        SideMenuRecyclerAdapter.SideMenuClickListener, FragmentInteractionListener {

    @BindView(R.id.rv_side_menu)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    private Fragment fragment;
    private SideMenuRecyclerAdapter sideMenuRecyclerAdapter;
    private AdView mAdView;
    private ArrayList<Job> mJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mJobs = new ArrayList<>();
        initAds();
        initSideMenu();
        if (savedInstanceState != null) {
            reInitViews(savedInstanceState);
        } else {
            getAllJobs(this);
        }

        if (new AppSettings().getIsFirstLogin(this)) {
            new Handler().postDelayed(() -> {
                drawer.openDrawer(GravityCompat.START);
            }, 800);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mJobs.isEmpty()) {
//        }
//    }

    private void initAds() {
        //intiate google Ads
        MobileAds.initialize(this, getString(R.string.Add_mob_id));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getSupportFragmentManager().putFragment(outState, "jobs_frag", fragment);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void reInitViews(Bundle savedInstanceState) {
        getSupportFragmentManager().getFragment(savedInstanceState, "jobs_frag");
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

    private void initSideMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        sideMenuRecyclerAdapter = new SideMenuRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sideMenuRecyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

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
                Intent intent = new Intent(this, MyJobsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case 3:
                new AppSettings().setIsFirstLogin(this, true);
                Intent intentTwo = new Intent(this, LoginActivity.class);
                intentTwo.putExtra("logOut", true);
                intentTwo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentTwo);
                finish();
                break;
            default:
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    private Fragment getFragment(int fragmentId) {
        switch (fragmentId) {

            case FRAGMENT_PROFILE:
                fragment = ProfileFragment.newInstance("home", "");
                break;
            default:
                throw new UnsupportedOperationException("UnSupportedFragmentId: " + fragmentId);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack("fragment")
                .replace(R.id.fragments_holder, fragment).commit();
        return fragment;
    }

    @Override
    public void onInteraction(Object... data) {
        if (data != null) {
            String jobData = (String) data[0];
            if (jobData.equalsIgnoreCase("update data")) {
                getAllJobs(this);
            }
            if (getResources().getBoolean(R.bool.twoPaneMode)) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (jobData.equalsIgnoreCase("1")) {
                    fragmentTransaction.replace(R.id.details_frag, AddJobFragment.newInstance("")).commit();
                } else {
                    fragmentTransaction.replace(R.id.details_frag, JobDetailsFragment.newInstance(jobData)).commit();
                }
            }
        }
    }

    @Override
    public void onJobsDataChange(ArrayList<Job> jobs, int type) {
        mJobs.addAll(jobs);
        if (!isFinishing()) {
            HomeFragment homeFragment = HomeFragment.newInstance(mJobs);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragments_holder, homeFragment).commitAllowingStateLoss();
            if (getResources().getBoolean(R.bool.twoPaneMode)) {
                Gson gson = new Gson();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.details_frag, JobDetailsFragment.newInstance(gson.toJson(mJobs.get(0)))).commit();
            }
        }


    }
}
