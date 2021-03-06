package com.jobease.www.jobease.database;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.AppSettings;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.models.Job;
import com.jobease.www.jobease.models.User;

import java.util.ArrayList;

/**
 * Created by Dell on 23/07/2017.
 */

public class FireBaseDataBaseHelper {
    static DatabaseReference postsDBRef, usersDBRef;
    static FirebaseDatabase firebaseDatabase;
    private ApplyToJobListener applyToJobListener;

    public static FirebaseDatabase getFireBaseDataBaseInstance() {
        //DB instantiated
        firebaseDatabase = FirebaseDatabase.getInstance();
        return firebaseDatabase;
    }

    public static DatabaseReference getDataBaseReference(String reference) {
        //DB Reference created
        return getFireBaseDataBaseInstance().getReference(reference);
    }

    public static void createUser(User user, Activity activity) {
        //User is created and pushed to the DB
        usersDBRef = getDataBaseReference("users").child(user.getUid());
        usersDBRef.setValue(user);
        //User UniqueId is retrieved and set.
        new UserSettings().setUserID(activity, user.getUid());

        if (new AppSettings().getIsFirstLogin(activity) == true)
            firebaseDatabase.setPersistenceEnabled(true);

    }

    public static void editUser(User user, Context context) {
        usersDBRef = getDataBaseReference("users").child(user.getUid());
        usersDBRef.setValue(user, (DatabaseError databaseError, DatabaseReference databaseReference) -> {
            if (databaseError == null) {
                Logging.longToast(context, context.getString(R.string.edit_success));
            } else {
                Logging.log(databaseError.getMessage());
            }
        });
    }

    public static void flagUser(User user, Context context) {
        usersDBRef = getDataBaseReference("users").child(user.getUid()).child("noFlags");
        usersDBRef.setValue(user.getNoFlags() + 1, (DatabaseError databaseError, DatabaseReference databaseReference) -> {
            if (databaseError == null) {
                Logging.longToast(context, context.getString(R.string.flagged_success));
            } else {
                Logging.log(databaseError.getMessage());
            }

        });
    }

    public static void getUser(String uid, UserGettingListener userGettingListener) {
        //User is created and pushed to the DB
        usersDBRef = getDataBaseReference("users").child(uid);
        usersDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userGettingListener.onUserGot(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(getClass().getName().toString(), ": " + databaseError.getMessage());
            }
        });
    }

    public static void createJob(Job job, Context context) {
        //Job  created and pushed to the DB
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        postsDBRef = firebaseDatabase.getReference("posts").push();
        postsDBRef.setValue(job);
        firebaseDatabase.getReference("posts")
                .child(postsDBRef.getKey()).child("jobId").setValue(postsDBRef.getKey(),
                (DatabaseError databaseError, DatabaseReference databaseReference) -> {
                    if (databaseError == null) {
                        Logging.shortToast(context, context.getString(R.string.added_successfully));
                    } else {
                        Logging.log(databaseError.getMessage());
                    }
                });
    }

    public static void getAllJobs(JobsDataChangeListener jobsDataChangeListener) {
        postsDBRef = getDataBaseReference("posts");
        postsDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Logging.log("dataSnapshot: " + dataSnapshot.getChildrenCount());

                ArrayList<Job> jobs = new ArrayList<>();
                Job job = dataSnapshot.getValue(Job.class);
                jobs.add(job);
                jobsDataChangeListener.onJobsDataChange(jobs, 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(getClass().getName().toString(), ": " + databaseError.getMessage());
            }
        });

    }

    public static ArrayList<Job> getAllJobs() {
        ArrayList<Job> jobs = new ArrayList<>();

        postsDBRef = getDataBaseReference("posts");
        postsDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Job job = dataSnapshot.getValue(Job.class);
                jobs.add(job);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(getClass().getName().toString(), ": " + databaseError.getMessage());
            }
        });
        return jobs;
    }

    public static void getMyJobs(JobsDataChangeListener jobsDataChangeListener, Activity activity) {
        postsDBRef = getDataBaseReference("posts");
        postsDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArrayList<Job> jobs = new ArrayList<>();
                Job job = dataSnapshot.getValue(Job.class);
                if (job.getUserId().equalsIgnoreCase(new UserSettings().getUserID(activity))) {
                    jobs.add(job);
                }
                jobsDataChangeListener.onJobsDataChange(jobs, 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(getClass().getName().toString(), ": " + databaseError.getMessage());
            }
        });
    }

    public static void editAJob(Job job) {
        getDataBaseReference("posts").child(job.getJobId()).setValue(job);
    }

    public static void deleteAJob(String key) {
        getDataBaseReference("posts").child(key).removeValue();
    }

    public static void reportAJob(Job job) {
//TODO: get No of reports first then add 1
        getDataBaseReference("posts").child(job.getJobId()).child("reported").setValue(true);
        getDataBaseReference("posts").child(job.getJobId()).child("noOfReports").setValue(job.getNoOfReports() + 1);

    }

    public static void rateAJob(Job job) {
//TODO: get No of raters first then add 1
        getDataBaseReference("posts").child(job.getJobId()).child("rating").setValue(job.getRating());
        getDataBaseReference("posts").child(job.getJobId()).child("noOfRaters").setValue(job.getNoOfRaters() + 1);
    }

    public static void applyToAJob(Job job, User user, ApplyToJobListener applyToJobListener) {
        User mUser = user;
        String btngan = user.getUid();
        getDataBaseReference("posts").child(job.getJobId()).child("appliedUsers").child(btngan)
                .setValue(user, (DatabaseError databaseError, DatabaseReference databaseReference) -> {
                    if (databaseError == null) {
                        applyToJobListener.onApplyToJob(true);
                    } else {
                        applyToJobListener.onApplyToJob(false);

                        Logging.log(databaseError.getMessage());
                    }
                });
    }

    public interface JobsDataChangeListener {
        void onJobsDataChange(ArrayList<Job> jobs, int type);
    }

    public interface UserGettingListener {
        void onUserGot(User user);
    }

    public interface ApplyToJobListener {
        void onApplyToJob(boolean isSuccessful);
    }

}
