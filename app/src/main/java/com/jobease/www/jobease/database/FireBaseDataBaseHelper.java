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
    static FirebaseDatabase firebaseDatabase;
    private static User user;

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
        firebaseDatabase.setPersistenceEnabled(true);

        //User UniqueId is retrieved and set.
        new UserSettings().setUserID(activity, user.getUid());
    }

    public static User getUser(String uid) {
        //User is created and pushed to the DB
        usersDBRef = getDataBaseReference("users").getRef();

        usersDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(getClass().getName().toString(), ": " + databaseError.getMessage());
            }
        });
        return user;
    }

    public static boolean createJob(Job job) {
        //Job  created and pushed to the DB
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        postsDBRef = firebaseDatabase.getReference("posts").push();
        //TODO: return something proving success

        postsDBRef.setValue(job);
        if (postsDBRef.getKey() == null) {
            return false;
        } else if (postsDBRef.getKey().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static void getAllJobs() {

        postsDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Job job = dataSnapshot.getValue(Job.class);
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
