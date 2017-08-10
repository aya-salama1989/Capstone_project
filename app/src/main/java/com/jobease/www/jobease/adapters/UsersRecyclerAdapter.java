package com.jobease.www.jobease.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.RoundedImageView;
import com.jobease.www.jobease.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Dell on 23/07/2017.
 */

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserItemViewHolder> {

    public static final int BUTTON_SETTINGS = 0;
    public static final int BUTTON_CALL = 1;
    public static final int BUTTON_HIRE = 2;
    public static final int ITEM_CLICK = 3;
    private Context context;
    private ArrayList<User> users;
    private UserClickListener userClickListener;

    public UsersRecyclerAdapter(ArrayList<User> users, UserClickListener userClickListener) {
        this.users = users;
        this.userClickListener = userClickListener;
    }

    @Override
    public UserItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.item_user, parent, false);
        UserItemViewHolder userItemViewHolder = new UserItemViewHolder(v);
        return userItemViewHolder;
    }

    @Override
    public void onBindViewHolder(UserItemViewHolder holder, int position) {
        holder.setData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface UserClickListener {
        void onUserClickListener(int Type, int position);
    }

    class UserItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RoundedImageView roundedImageView;
        private ImageButton settingsImgBtn;
        private Button btnCallUser, btnHireUser;
        private RatingBar ratingBar;
        private TextView userNameTextView, userLocationTxtVue;

        public UserItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            roundedImageView = ButterKnife.findById(itemView, R.id.rv_userImage);

            settingsImgBtn = ButterKnife.findById(itemView, R.id.btn_Settings);
            settingsImgBtn.setOnClickListener(this);

            btnCallUser = ButterKnife.findById(itemView, R.id.btn_call);
            btnCallUser.setOnClickListener(this);

            btnHireUser = ButterKnife.findById(itemView, R.id.btn_hire);
            btnHireUser.setOnClickListener(this);

            userNameTextView = ButterKnife.findById(itemView, R.id.tv_userName);
            userLocationTxtVue = ButterKnife.findById(itemView, R.id.tv_user_location);
            ratingBar = ButterKnife.findById(itemView, R.id.rating_bar);
        }

        protected void setData(User user) {
            Picasso.with(context).load(user.getImage()).into(roundedImageView);
            userNameTextView.setText(user.getName());
            userLocationTxtVue.setText(user.getAddress());
            ratingBar.setRating(user.getRating());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_call:
                    userClickListener.onUserClickListener(BUTTON_CALL, getAdapterPosition());
                    break;
                case R.id.btn_hire:
                    userClickListener.onUserClickListener(BUTTON_HIRE, getAdapterPosition());

                    break;
                case R.id.btn_Settings:
                    userClickListener.onUserClickListener(BUTTON_SETTINGS, getAdapterPosition());

                    break;
                default:
                    userClickListener.onUserClickListener(ITEM_CLICK, getAdapterPosition());

            }
        }
    }
}
