package com.jobease.www.jobease.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.adapters.SideMenuRecyclerAdapter;
import com.jobease.www.jobease.fragments.AppliersFragment;
import com.jobease.www.jobease.fragments.HomeFragment;
import com.jobease.www.jobease.fragments.JobsFragment;
import com.jobease.www.jobease.fragments.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.fragments.AppliersFragment.FRAGMENT_APPLIERS;
import static com.jobease.www.jobease.fragments.HomeFragment.FRAGMENT_HOME;
import static com.jobease.www.jobease.fragments.JobsFragment.FRAGMENT_JOBS;
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



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        sideMenuRecyclerAdapter = new SideMenuRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sideMenuRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSideItemCick(int position) {
        switch (position) {
            case 0:
                getFragment(FRAGMENT_HOME);
                break;
            case 1:
                getFragment(FRAGMENT_APPLIERS);

                break;
            case 2:
                getFragment(FRAGMENT_JOBS);

                break;
            case 3:
                getFragment(FRAGMENT_PROFILE);

                break;

            case 4:
//TODO: Logout App
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
                fragment = AppliersFragment.newInstance("", "");

                break;
            case FRAGMENT_JOBS:
                fragment = JobsFragment.newInstance("", "");

                break;
            case FRAGMENT_PROFILE:
                fragment = ProfileFragment.newInstance("", "");

                break;
            default:
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragments_holder, fragment).setTransition(android.R.anim.bounce_interpolator).commit();

        return fragment;
    }
}
