package com.jobease.www.jobease;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.jobease.www.jobease.database.FireBaseDataBaseHelper;
import com.jobease.www.jobease.models.Job;

import java.util.ArrayList;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.getAllJobs;

/**
 * Created by Dell on 24/08/2017.
 */

public class JobsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return new JobsRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class JobsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, FireBaseDataBaseHelper.JobsDataChangeListener {

    private Context mContext;
    private int mAppWidgetId;
    private ArrayList<Job> mJobs;

    public JobsRemoteViewsFactory(Context context, Intent intent) {

        this.mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mJobs = new ArrayList<>();
        getAllJobs(this);
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mJobs.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_jobs_widget);
        rv.setTextViewText(R.id.job_title, mJobs.get(position).getTitle());
        rv.setTextViewText(R.id.job_address, mJobs.get(position).getAddress());

        // Next, set a fill-intent, which will be used to fill in the pending intent template
        // that is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        extras.putInt(JobsWidgetProvider.OPEN_JOB_ACTION, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        rv.setOnClickFillInIntent(R.id.btn_apply, fillInIntent);
//        rv.setOnClickPendingIntent(R.id.btn_apply, );
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onJobsDataChange(ArrayList<Job> jobs, int type) {
        mJobs.addAll(jobs);

        //TODO - mentor: trying to update data here
        AppWidgetManager.getInstance(mContext).notifyAppWidgetViewDataChanged(mAppWidgetId, R.id.widget_list);

    }
}
