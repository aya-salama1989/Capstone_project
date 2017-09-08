package com.jobease.www.jobease.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.activities.JobAppliersActivity;
import com.jobease.www.jobease.activities.UserProfileActivity;
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


public class JobAppliersFragment extends Fragment  implements UsersRecyclerAdapter.UserClickListener{

    private static final String APPLIED_USERS = "applied_users";

    private View v;

    @BindView(R.id.rv_appliers)
    RecyclerView recyclerView;

    @BindView(R.id.empty_placeHolder)
    TextView textView;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int scrollPosition, selectedPosition;
    private ArrayList<User> mUsers;

    private FragmentInteractionListener mListener;

    public JobAppliersFragment() {
        // Required empty public constructor
    }


    public static JobAppliersFragment newInstance(String param1) {
        JobAppliersFragment fragment = new JobAppliersFragment();
        Bundle args = new Bundle();
        args.putString(APPLIED_USERS, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_job_appliers, container, false);
        ButterKnife.bind(this, v);
        getData();
        bindViews();

        return v;
    }


    private void bindViews() {
        usersRecyclerAdapter = new UsersRecyclerAdapter(mUsers, this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(usersRecyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (scrollPosition != recyclerView.NO_POSITION) {
            recyclerView.smoothScrollToPosition(scrollPosition);
        }

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            mListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setViewVisible(View v) {
        recyclerView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        v.setVisibility(View.VISIBLE);
    }

    private void getData() {
        mUsers = new ArrayList<>();
        String appliersList = getArguments().getString(APPLIED_USERS);
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
    @Override
    public void onUserClickListener(int Type, int position) {
        switch (Type) {
            case BUTTON_CALL:
                dialNumber(getActivity(), mUsers.get(position).getUserPhone());
                break;
 /*           case BUTTON_HIRE:

                break;*/
/*            case BUTTON_SETTINGS:
//TODO: delayed to version 2
                break;*/

            case ITEM_CLICK:
                selectedPosition = position;
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                Gson gson = new Gson();
                intent.putExtra("userData", gson.toJson(mUsers.get(position)).toString());
                startActivity(intent);
                break;
            default:
        }
    }
}
