package com.jobease.www.jobease.database;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.models.Job;
import com.jobease.www.jobease.models.User;
import com.jobease.www.jobease.models.Users;

import java.util.ArrayList;

/**
 * Created by Dell on 23/07/2017.
 */

public class FireBaseDataBaseHelper {
    static DatabaseReference postsDBRef, usersDBRef;
    static FirebaseDatabase firebaseDatabase;
    private static User user;

    public static FirebaseDatabase getFireBaseDataBaseInstance() {
        //DB instantiated
        firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseDatabase.setPersistenceEnabled(true);
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
    }

    public static void getUser(String uid, UserGettingListener userGettingListener) {
        //User is created and pushed to the DB
        usersDBRef = getDataBaseReference("users").getRef();
        usersDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                userGettingListener.onUserGot(user);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(getClass().getName().toString(), ": " + databaseError.getMessage());
            }
        });
    }

    public static boolean createJob(Job job) {
        //Job  created and pushed to the DB
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        postsDBRef = firebaseDatabase.getReference("posts").push();
        postsDBRef.setValue(job);

        firebaseDatabase.getReference("posts")
                .child(postsDBRef.getKey()).child("jobId").setValue(postsDBRef.getKey());
        if (postsDBRef.getKey() == null) {
            return false;
        } else if (postsDBRef.getKey().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static void getAllJobs(JobsDataChangeListener jobsDataChangeListener) {
        postsDBRef = getDataBaseReference("posts");
        postsDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
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

    public static void editAJob(Job job) {
        getDataBaseReference("posts").child(job.getJobId()).setValue(job);
        //Re
    }

    public static void deleteAJob(String key) {
        getDataBaseReference("posts").child(key).removeValue();
    }

    public static void reportAJob(Job job) {
//TODO: edit report properity at other posts
        getDataBaseReference("posts").child(job.getJobId()).child("reported").setValue(true);
        getDataBaseReference("posts").child(job.getJobId()).child("noOfReports").setValue(job.getNoOfReports() + 1);

    }

    public static void rateAJob(Job job) {
//TODO: edit Rating properity at other posts
        getDataBaseReference("posts").child(job.getJobId()).child("rating").setValue(job.getRating());
        getDataBaseReference("posts").child(job.getJobId()).child("noOfRaters").setValue(job.getNoOfRaters() + 1);
    }

    public static void applyToAJob(Job job, User user) {
        //TODO: add/ edit users list
        getDataBaseReference("posts").child(job.getJobId()).child("appliedUsers").setValue(user);
    }

    public static Users getAllAppliedUsers() {
        return new Users();
    }


    public interface JobsDataChangeListener {
        void onJobsDataChange(ArrayList<Job> jobs, int type);
    }

    public interface UserGettingListener{
        void onUserGot(User user);
    }

}
