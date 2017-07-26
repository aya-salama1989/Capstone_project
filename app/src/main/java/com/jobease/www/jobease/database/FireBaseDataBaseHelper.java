package com.jobease.www.jobease.database;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.models.Job;
import com.jobease.www.jobease.models.User;
import com.jobease.www.jobease.models.Users;

/**
 * Created by Dell on 23/07/2017.
 */

public class FireBaseDataBaseHelper {
    static DatabaseReference postsDBRef, usersDBRef;

    public static FirebaseDatabase getFireBaseDataBaseInstance() {
        //DB instantiated
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        return firebaseDatabase;
    }

    public static DatabaseReference getDataBaseReference(String reference) {
        //DB Reference created
        return getFireBaseDataBaseInstance().getReference(reference);
    }

    public static void createUser(User user, Activity activity) {
        //User is created and pushed to the DB

        usersDBRef = getDataBaseReference("users").push();
        usersDBRef.setValue(user);

        //User UniqueId is retrieved and set.
        new UserSettings().setUserID(activity, usersDBRef.getKey());
    }

    public static void createJob(Job job) {
        //Job  created and pushed to the DB
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        postsDBRef = firebaseDatabase.getReference("posts").push();
        postsDBRef.setValue(job);
    }

    public static void getAllJobs() {

        postsDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue(Job.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(getClass().getName().toString(), ": " + databaseError.getMessage());
            }
        });
    }

    public static Users getAllAppliedUsers() {

        return new Users();
    }

}
