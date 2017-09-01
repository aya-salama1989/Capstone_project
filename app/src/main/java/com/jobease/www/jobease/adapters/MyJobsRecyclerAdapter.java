package com.jobease.www.jobease.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.models.Job;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Dell on 21/08/2017.
 */

public class MyJobsRecyclerAdapter extends RecyclerView.Adapter<MyJobsRecyclerAdapter.MyJobViewHoldere> {
    private OnMyJobClickListener onMyJobClickListener;
    private ArrayList<Job> jobs;
    private int selectedItem;

    public MyJobsRecyclerAdapter(OnMyJobClickListener onMyJobClickListener, ArrayList<Job> jobs) {
        this.onMyJobClickListener = onMyJobClickListener;
        this.jobs = jobs;
        setHasStableIds(true);
    }

    @Override
    public MyJobViewHoldere onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_my_job, parent, false);
        MyJobViewHoldere myJobViewHolder = new MyJobViewHoldere(v);
        return myJobViewHolder;
    }

    @Override
    public void onBindViewHolder(MyJobViewHoldere holder, int position) {
        holder.setData(jobs.get(position));
        if (position == selectedItem) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public interface OnMyJobClickListener {
        void OnMyJobItemClick(int type, int position);
    }

    class MyJobViewHoldere extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtVueJobTitle;
        private ImageButton imageButton;

        public MyJobViewHoldere(View itemView) {
            super(itemView);
            txtVueJobTitle = ButterKnife.findById(itemView, R.id.tv_job_title);
            imageButton = ButterKnife.findById(itemView, R.id.btn_settings);
            imageButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        private void setData(Job job) {
            txtVueJobTitle.setText(job.getTitle());
        }

        @Override
        public void onClick(View v) {
            selectedItem = getAdapterPosition();
            switch (v.getId()) {
                case R.id.btn_settings:
                    onMyJobClickListener.OnMyJobItemClick(0, getAdapterPosition());
                    break;
                case R.id.my_job_item:
                    onMyJobClickListener.OnMyJobItemClick(1, getAdapterPosition());
                    break;
                default:
            }
            notifyDataSetChanged();
        }
    }


}
