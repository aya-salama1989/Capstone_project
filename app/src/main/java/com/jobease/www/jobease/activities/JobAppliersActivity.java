package com.jobease.www.jobease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.adapters.UsersRecyclerAdapter;
import com.jobease.www.jobease.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.Utilities.Utilities.dialNumber;
import static com.jobease.www.jobease.adapters.UsersRecyclerAdapter.BUTTON_CALL;
import static com.jobease.www.jobease.adapters.UsersRecyclerAdapter.ITEM_CLICK;

public class JobAppliersActivity extends AppCompatActivity implements UsersRecyclerAdapter.UserClickListener {

    @BindView(R.id.rv_appliers)
    RecyclerView recyclerView;

    @BindView(R.id.empty_placeHolder)
    TextView textView;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private ArrayList<User> mUsers;
    private LinearLayoutManager linearLayoutManager;
    private int scrollPosition, selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_appliers);
        ButterKnife.bind(this);
        getData();
        bindViews();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
        outState.putInt("firstVisiblePosition", firstVisiblePosition);
        outState.putInt("selectedPosition", selectedPosition);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        scrollPosition = savedInstanceState.getInt("firstVisiblePosition");
        selectedPosition = savedInstanceState.getInt("selectedPosition");
        super.onRestoreInstanceState(savedInstanceState);
    }


    private void bindViews() {
        usersRecyclerAdapter = new UsersRecyclerAdapter(mUsers, this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(usersRecyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (scrollPosition != recyclerView.NO_POSITION) {
            recyclerView.smoothScrollToPosition(scrollPosition);
        }

        //TODO-2: to Mentor, what should i do to avoid this bug

//        if (selectedPosition != recyclerView.NO_POSITION) {
//            recyclerView.findViewHolderForAdapterPosition(selectedPosition).itemView.setSelected(true);
//        }
    }

    private void getData() {
        mUsers = new ArrayList<>();
        String appliersList = getIntent().getExtras().getString("appliersList");
        try {
            JSONObject jsonArray = new JSONObject(appliersList);
            Logging.log(jsonArray.toString());
            Iterator iterator = jsonArray.keys();
            if (!iterator.hasNext()) {
                setViewVisible(textView);
            } else {
                setViewVisible(recyclerView);
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    User user = new User(jsonArray.getJSONObject(key));
                    mUsers.add(user);
                }
            }
        } catch (JSONException e) {
            Logging.log(e.getMessage());
        }
    }

    private void setViewVisible(View v) {
        recyclerView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUserClickListener(int Type, int position) {
        switch (Type) {
            case BUTTON_CALL:
                dialNumber(this, mUsers.get(position).getUserPhone());
                break;
 /*           case BUTTON_HIRE:

                break;*/
/*            case BUTTON_SETTINGS:
//TODO: delayed to version 2
                break;*/

            case ITEM_CLICK:
                selectedPosition = position;
                Intent intent = new Intent(JobAppliersActivity.this, UserProfileActivity.class);
                Gson gson = new Gson();
                intent.putExtra("userData", gson.toJson(mUsers.get(position)).toString());
                startActivity(intent);
                break;
            default:
        }
    }
}
