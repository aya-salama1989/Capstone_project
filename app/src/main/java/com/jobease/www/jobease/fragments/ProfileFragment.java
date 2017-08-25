package com.jobease.www.jobease.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.Utilities.RoundedImageView;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.database.FireBaseDataBaseHelper;
import com.jobease.www.jobease.models.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.editUser;
import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.flagUser;
import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.getUser;


public class ProfileFragment extends Fragment implements FireBaseDataBaseHelper.UserGettingListener {
    public static final int FRAGMENT_PROFILE = 1;


    private static final String ARG_FROM = "from";
    private static final String USER_DATA = "user_data";


    @BindView(R.id.profile_iv_image)
    RoundedImageView roundedImageView;
    @BindView(R.id.profile_tv_name)
    TextView userNameTxtView;
    @BindView(R.id.profile_tv_phone)
    TextView userPhoneTxtView;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.tv_applied)
    TextView appliedTxtView;
    @BindView(R.id.tv_reports)
    TextView reportsTextView;
    @BindView(R.id.tv_posts)
    TextView postsTextView;
    @BindView(R.id.btn_flag_user)
    ImageButton btnFlagUser;
    @BindView(R.id.btn_edit_user)
    ImageButton btnEditUser;

    private User mUser;


    private View v;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String from, String userData) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FROM, from);
        args.putString(USER_DATA, userData);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, v);
        mUser = new User();
        getData();
        initViews();
        return v;
    }

    private void initViews() {
        if (getArguments() != null) {
            //TODO: edit delayed to version 2
            if (getArguments().getString(ARG_FROM).equalsIgnoreCase("home")) {
                setViewVisibility(btnEditUser);
            } else {
                setViewVisibility(btnFlagUser);
            }
        }

        btnFlagUser.setOnClickListener((View v) -> {
            flagUser(mUser);
        });

        btnEditUser.setOnClickListener((View v) -> {
            editUser(mUser);
        });
    }

    private void getData() {
        if (getArguments() != null) {
            if (getArguments().getString(ARG_FROM).equalsIgnoreCase("home")) {
                getUser(new UserSettings().getUserID(getActivity()), this);
            } else {
                String user = getArguments().getString(USER_DATA);
                try {
                    JSONObject jsonObject = new JSONObject(user);
                    mUser = new User(jsonObject);
                    setData(mUser);

                } catch (JSONException e) {
                    Logging.log(e.getMessage());
                }


            }
        }
    }


    @Override
    public void onUserGot(User user) {
        mUser = user;
        setData(mUser);
    }

    private void setData(User user) {
        Picasso.with(getActivity()).load(user.getImage()).into(roundedImageView);
        userNameTxtView.setText(user.getName());
        userPhoneTxtView.setText(user.getUserPhone());
        ratingBar.setRating(user.getRating());
        appliedTxtView.setText(getString(R.string.appliers_placeHolder, "" + user.getNoApplies()));
        reportsTextView.setText(getString(R.string.flags_placeHolder, "" + user.getNoFlags()));
        postsTextView.setText(getString(R.string.posts_placeHolder, "" + user.getNoPosts()));
    }

    private void setViewVisibility(View v) {
        btnFlagUser.setVisibility(View.INVISIBLE);
        btnEditUser.setVisibility(View.INVISIBLE);
        v.setVisibility(View.VISIBLE);
    }
}
