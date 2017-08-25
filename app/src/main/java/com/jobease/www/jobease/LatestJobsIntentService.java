package com.jobease.www.jobease;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class LatestJobsIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_LATEST_JOBS = "com.jobease.www.jobease.action.get.latest.jobs";
    private static final String ACTION_GET_APPLIERS = "com.jobease.www.jobease.action.get.appliers";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.jobease.www.jobease.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.jobease.www.jobease.extra.PARAM2";

    public LatestJobsIntentService() {
        super("LatestJobsIntentService");
    }


    public static void startActionGettingLatestJobs(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LatestJobsIntentService.class);
        intent.setAction(ACTION_GET_LATEST_JOBS);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    public static void startActionGettingAppliers(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LatestJobsIntentService.class);
        intent.setAction(ACTION_GET_APPLIERS);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_LATEST_JOBS.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionGettingLatestJobs(param1, param2);
            } else if (ACTION_GET_APPLIERS.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionGettingAppliers(param1, param2);
            }
        }
    }

    private void handleActionGettingLatestJobs(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionGettingAppliers(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
