package com.jobease.www.jobease.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.Utilities.RoundedImageView;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.models.User;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jobease.www.jobease.Utilities.DateUtils.getCurrentDeviceDate;
import static com.jobease.www.jobease.Utilities.DateUtils.getDiffYears;

public class UserDataActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_GET_LOCATION = 1;
    @BindView(R.id.img_user)
    RoundedImageView roundedImageView;
    @BindView(R.id.tv_user_name)
    TextView txtVueUserName;
    @BindView(R.id.et_phone)
    EditText etPhoneNumber;
    @BindView(R.id.et_birth_date)
    EditText etBirthDate;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.btn_detect_location)
    ImageButton imgBtn;
    GoogleApiClient mGoogleApiClient;
    private User user;
    private int _birthYear, _month, _day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        ButterKnife.bind(this);
        getData();
        bindViews();

    }

    private void bindViews() {

        etBirthDate.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog_MinWidth, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();

        });
        etBirthDate.setOnClickListener((View v) -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog_MinWidth, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
        if (user != null) {
            Picasso.with(this).load(user.getImage()).into(roundedImageView);
            txtVueUserName.setText(user.getName());
        }
        btnDone.setOnClickListener((View v) -> {
            if (etPhoneNumber.getText().toString().trim().isEmpty()) {
                etPhoneNumber.setError(getString(R.string.empty_field));
            } else if (etBirthDate.getText().toString().trim().isEmpty()) {
                etBirthDate.setError(getString(R.string.empty_field));
            } else {
                new UserSettings().setPhone(this, etPhoneNumber.getText().toString().trim());
                new UserSettings().setUserBirthDate(this, etBirthDate.getText().toString().trim());
                Intent intent = new Intent(UserDataActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        });

        imgBtn.setOnClickListener((View v) -> {
            getUserLocation();
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addApi(LocationServices.API)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .build();
//            mGoogleApiClient.connect();
        });
    }

    private void getData() {
        if (getIntent().getExtras().getParcelable("userData") != null) {
            user = getIntent().getExtras().getParcelable("userData");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        _birthYear = year;
        _month = month + 1;
        _day = dayOfMonth;
        updateDisplay();
    }

    // updates the date in the birth date EditText
    private void updateDisplay() {
        StringBuilder pickedDate = new StringBuilder()
                .append(_month).append("-").append(_day).append("-").append(_birthYear).append(" ");

        DateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        Date currentDate = null, picked = null;
        try {
            currentDate = format.parse(getCurrentDeviceDate("MM-dd-yyyy"));
            picked = format.parse(pickedDate.toString());
            if (picked.after(currentDate) || currentDate.equals(picked)) {
                etBirthDate.setText(pickedDate);
                etBirthDate.setError(getString(R.string.invalid_date_format));
                Logging.shortToast(this, getString(R.string.invalid_date_format));
            } else if (getDiffYears(picked, currentDate) < 13) {
                etBirthDate.setText(pickedDate);
                etBirthDate.setError(getString(R.string.minor));
                Logging.shortToast(this, getString(R.string.minor));
            } else {
                etBirthDate.setText(pickedDate);
                etBirthDate.setError(null);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean getUserLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(UserDataActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSION_GET_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_GET_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Logging.log("connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Logging.log("suspend");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Logging.log("failed");
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_GET_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
