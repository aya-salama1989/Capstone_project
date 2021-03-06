package com.jobease.www.jobease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.AppSettings;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.models.User;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = getClass().getName().toString();
    public FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (!new AppSettings().getIsFirstLogin(this)) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            initViews();
        }

        if (getIntent() != null) {
            if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("logOut"))
                signOut();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Logging.log(loginResult.toString());
                handleFacebookAccessToken(loginResult.getAccessToken());
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, picture, name, email, first_name, last_name, gender, birthday");
            }

            @Override
            public void onCancel() {
                Logging.longToast(LoginActivity.this, getString(R.string.something_went_wrong));
            }

            @Override
            public void onError(FacebookException exception) {
                Logging.log("FacebookException: " + exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, (@NonNull Task<AuthResult> task) -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.auth_fail),
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }).addOnFailureListener(this, (@NonNull Exception e) -> {
            Logging.log("Exception: " + e.getMessage());
            e.printStackTrace();
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            User mainUser = new User();
            mainUser.setName(user.getDisplayName());
            mainUser.setImage(user.getPhotoUrl().toString());
            mainUser.setUid(user.getUid());
            Intent intent = new Intent(this, UserDataActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("userData", new Gson().toJson(mainUser).toString());
            startActivity(intent);
        }
    }

    private void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


}
