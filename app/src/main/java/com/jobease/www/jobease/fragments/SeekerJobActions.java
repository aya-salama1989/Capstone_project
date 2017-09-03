package com.jobease.www.jobease.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.database.FireBaseDataBaseHelper;
import com.jobease.www.jobease.models.Job;
import com.jobease.www.jobease.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.applyToAJob;
import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.getUser;
import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.rateAJob;
import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.reportAJob;


public class SeekerJobActions extends DialogFragment implements FireBaseDataBaseHelper.UserGettingListener {
    private static final String JOB_OBJECT = "jobObject";
    private DialogActionsListener dialogActionsListener;
    private String object;
    private JSONObject jsonObject;
    private Job job;

    public SeekerJobActions newInstance(DialogActionsListener dialogActionsListener, String jobObject) {
        this.dialogActionsListener = dialogActionsListener;
        Bundle args = new Bundle();
        args.putString(JOB_OBJECT, jobObject);
        this.setArguments(args);
        return this;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            object = getArguments().getString(JOB_OBJECT);
        }
        try {
            jsonObject = new JSONObject(object);
            job = new Job(jsonObject);
        } catch (JSONException e) {
            Logging.log(e.getMessage());
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.seeker_actions, (DialogInterface dialog, int which) -> {
            switch (which) {
                case 0:
                    reportAJob(job);
                    break;
                case 1:
                    rateAJob(job);
                    break;
                case 2:
                    String userId = new UserSettings().getUserID(getActivity());
                    getUser(userId, this);
                    break;
                case 3:
                    super.dismiss();
                    break;
                default:
            }
            if (dialogActionsListener != null)
                dialogActionsListener.onActionDone();
        });
        return builder.create();
    }

    @Override
    public void onUserGot(User user) {
        User mUser = user;
        if (user != null) {
            applyToAJob(job, user, getActivity());
        } else {
            Logging.log("null user");
        }
    }
}
