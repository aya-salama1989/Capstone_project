package com.jobease.www.jobease.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.RoundedImageView;
import com.jobease.www.jobease.models.Job;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Dell on 23/07/2017.
 */

public class JobsRecyclerAdapter extends RecyclerView.Adapter<JobsRecyclerAdapter.JobItemViewHolder> {

    public static final int ITEM_SETTINGS = 0;
    public static final int ITEM_JOB = 1;
    private ArrayList<Job> jobs;
    private Context context;
    private JobClickListener jobClickListener;
    private int selectedItem = -1;

    public JobsRecyclerAdapter(ArrayList<Job> jobs, JobClickListener jobClickListener) {
        this.jobs = jobs;
        this.jobClickListener = jobClickListener;
        setHasStableIds(true);
    }

    @Override
    public JobItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.item_job, parent, false);
        JobItemViewHolder jobItemViewHolder = new JobItemViewHolder(v);
        return jobItemViewHolder;
    }

    @Override
    public void onBindViewHolder(JobItemViewHolder holder, int position) {
        holder.setData(jobs.get(position));
        if (position == selectedItem) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
    }

    @Override
    public long getItemId(int position) {
        return jobs.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public interface JobClickListener {
        void onJobClick(int Type, int position);
    }

    class JobItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RoundedImageView userImageView;
        ImageButton settingsImageButton;
        TextView userNameTV, jobTitleTV, jobLocationTV, jobDescriptionTV;

        public JobItemViewHolder(View itemView) {
            super(itemView);
            userImageView = ButterKnife.findById(itemView, R.id.rv_userImage);
            settingsImageButton = ButterKnife.findById(itemView, R.id.ib_jobSettings);
            userNameTV = ButterKnife.findById(itemView, R.id.tv_userName);
            jobTitleTV = ButterKnife.findById(itemView, R.id.tv_jobTitle);
            jobLocationTV = ButterKnife.findById(itemView, R.id.tv_jobLocation);
            jobDescriptionTV = ButterKnife.findById(itemView, R.id.tv_jobDescription);
            itemView.setOnClickListener(this);
            settingsImageButton.setOnClickListener(this);
        }

        public void setData(Job job) {
            if (job.getUserImage() != null) {
                if (!job.getUserImage().isEmpty()) {
                    Picasso.with(context).load(job.getUserImage()).into(userImageView);
                }
            }
            userNameTV.setText(job.getUserName());
            jobTitleTV.setText(job.getTitle());
            jobLocationTV.setText(job.getAddress());
            jobDescriptionTV.setText(job.getDescription());
            //TODO: add currency, no of workers
        }

        @Override
        public void onClick(View v) {
            selectedItem = getAdapterPosition();
            if (v.getId() == R.id.ib_jobSettings) {
                jobClickListener.onJobClick(ITEM_SETTINGS, getAdapterPosition());
            } else {
                jobClickListener.onJobClick(ITEM_JOB, getAdapterPosition());
            }
            notifyDataSetChanged();
        }
    }
}
