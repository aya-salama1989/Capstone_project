package com.jobease.www.jobease.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.activities.AddJobActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.deleteAJob;


public class PosterJobActions extends DialogFragment {

    private static final String JOB_OBJECT = "jobObject";
    private String object;
    private JSONObject jsonObject;

    private String key;
    private DialogActionsListener dialogActionsListener;

    public PosterJobActions newInstance(DialogActionsListener dialogActionsListener, String jobObject) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.poster_actions, (DialogInterface dialog, int which) -> {
            try {
                jsonObject = new JSONObject(object);
                key = jsonObject.getString("jobId");
            } catch (JSONException e) {
                Logging.log(e.getMessage());
            }
            switch (which) {
                case 0:
                    Intent intent = new Intent(getActivity(), AddJobActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("jobObject", object);
                    startActivity(intent);
                    break;
                case 1:
                    deleteAJob(key);
                    break;
                case 2:
                    super.dismiss();
                default:
            }
            if (dialogActionsListener != null)
                dialogActionsListener.onActionDone();
        });
        return builder.create();
    }

}
