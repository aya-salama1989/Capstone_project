package com.jobease.www.jobease.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.jobease.www.jobease.R;
import com.jobease.www.jobease.Utilities.AppSettings;
import com.jobease.www.jobease.Utilities.Logging;
import com.jobease.www.jobease.Utilities.RoundedImageView;
import com.jobease.www.jobease.Utilities.UserSettings;
import com.jobease.www.jobease.models.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
import static com.jobease.www.jobease.Utilities.Utilities.createAlertDialogue;
import static com.jobease.www.jobease.database.FireBaseDataBaseHelper.createUser;

public class UserDataActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, ActivityCompat.OnRequestPermissionsResultCallback {

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
    private User user = new User();
    private int _birthYear, _month, _day;
    private UserSettings userSettings = new UserSettings();
    //    private AppSettings appSettings = new AppSettings();
    private double longitude, latitude;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        ButterKnife.bind(this);


        if (savedInstanceState != null) {
            etPhoneNumber.setText(savedInstanceState.getString("phone"));
            etBirthDate.setText(savedInstanceState.getString("birth_date"));
        }
        getData();
        bindViews();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("phone", etPhoneNumber.getText().toString().trim());
        outState.putString("birth_date", etBirthDate.getText().toString().trim());

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
                new AppSettings().setIsFirstLogin(this, false);
                userSettings.setPhone(this, etPhoneNumber.getText().toString().trim());
                userSettings.setUserBirthDate(this, etBirthDate.getText().toString().trim());
                userSettings.setUserImamge(this, user.getImage());
                if (user != null) {
                    user.setBirthDate(userSettings.getUserBirthDate(this));
                    user.setUserPhone(userSettings.getPhone(this));
                    user.setLatitude(latitude);
                    user.setLongitude(longitude);
                }

                createUser(user, this);

                Intent intent = new Intent(UserDataActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                UserDataActivity.this.finish();
            }
        });

        imgBtn.setOnClickListener((View v) -> {
            getUserLocation();
        });
    }


    private void getData() {
        if (getIntent().getExtras() != null) {
            String userString = getIntent().getStringExtra("userData");
            try {
                JSONObject jsonObject = new JSONObject(userString);
                user.setName(jsonObject.getString("name"));
                user.setImage(jsonObject.getString("image"));
                user.setUid(jsonObject.getString("uid"));
            } catch (JSONException e) {
                Logging.log(e.getMessage());
            }
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
                .append(_month).append("-").append(_day).append("-").append(_birthYear);

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
            Logging.log(e.getMessage());
        }
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserDataActivity.this,
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                    PERMISSION_GET_LOCATION);
            return;
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, (Location location) -> {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_GET_LOCATION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Logging.longToast(UserDataActivity.this, getString(R.string.permission_granted));
                    } else {
                        createAlertDialogue(UserDataActivity.this, getString(R.string.permission_request),
                                getString(R.string.please_allow),
                                getString(R.string.no),
                                (DialogInterface dialog, int which) -> {
                                    dialog.dismiss();
                                }
                                , getString(R.string.ok),
                                (DialogInterface dialog, int which) -> {
                                    ActivityCompat.requestPermissions(UserDataActivity.this,
                                            new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                                            PERMISSION_GET_LOCATION);
                                });
                    }
                }
            }
        }
    }
}
