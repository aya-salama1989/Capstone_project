package com.jobease.www.jobease.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.jobease.www.jobease.Utilities.Logging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dell on 21/07/2017.
 */
/*
@JsonIgnoreProperties(ignoreUnknown = true)
*/

@IgnoreExtraProperties
public class Job implements Parcelable {

    public Map<String, User> appliedUsers = new HashMap<>();
    private String title;
    private String salary;
    private String currency;
    private String description;
    private String address;
    private long longitude;
    private long latitude;
    private long rating;
    private String userId;
    private String jobId;
    private int noOfWorkers;
    private boolean isApplied;
    private boolean isReported;
    private int noOfRaters;
    private int noOfReports;
    private String userImage;
    private String userName;


    protected Job(Parcel in) {
        title = in.readString();
        salary = in.readString();
        currency = in.readString();
        description = in.readString();
        address = in.readString();
        longitude = in.readLong();
        latitude = in.readLong();
        rating = in.readLong();
        userId = in.readString();
        jobId = in.readString();
        noOfWorkers = in.readInt();
        isApplied = in.readByte() != 0;
        isReported = in.readByte() != 0;
        noOfRaters = in.readInt();
        noOfReports = in.readInt();
        userImage = in.readString();
        userName = in.readString();
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public Map<String, User> getAppliedUsers() {
        return appliedUsers;
    }

    public void setAppliedUsers(Map<String, User> appliedUsers) {
        this.appliedUsers = appliedUsers;
    }

    public Job() {
    }

    public Job(JSONObject jsonObject) {
        try {
            if (jsonObject.has("longitude")) {
                longitude = jsonObject.getLong("longitude");
            }
            if (jsonObject.has("latitude")) {
                latitude = jsonObject.getLong("latitude");
            }
            if (jsonObject.has("rating")) {
                rating = jsonObject.getLong("rating");
            }
            if (jsonObject.has("noOfWorkers")) {
                noOfWorkers = jsonObject.getInt("noOfWorkers");
            }
            if (jsonObject.has("noOfRaters")) {
                noOfRaters = jsonObject.getInt("noOfRaters");
            }
            if (jsonObject.has("noOfReports")) {
                noOfReports = jsonObject.getInt("noOfReports");
            }

            if (jsonObject.has("isApplied")) {
                isApplied = jsonObject.getBoolean("isApplied");
            }
            if (jsonObject.has("isReported")) {
                isReported = jsonObject.getBoolean("isReported");
            }
            if (jsonObject.has("address")) {
                address = jsonObject.getString("address");
            }
            if (jsonObject.has("title")) {
                title = jsonObject.getString("title");
            }
            if (jsonObject.has("salary")) {
                salary = jsonObject.getString("salary");
            }
            if (jsonObject.has("currency")) {
                currency = jsonObject.getString("currency");
            }
            if (jsonObject.has("description")) {
                description = jsonObject.getString("description");
            }
            if (jsonObject.has("userId")) {
                userId = jsonObject.getString("userId");
            }
            if (jsonObject.has("jobId")) {
                jobId = jsonObject.getString("jobId");
            }
            if (jsonObject.has("userImage")) {
                userImage = jsonObject.getString("userImage");
            }
            if (jsonObject.has("userName")) {
                userName = jsonObject.getString("userName");
            }


        } catch (JSONException jsonException) {
            Logging.log(jsonException.getMessage());
        }

    }



    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getNoOfRaters() {
        return noOfRaters;
    }

    public void setNoOfRaters(int noOfRaters) {
        this.noOfRaters = noOfRaters;
    }

    public int getNoOfReports() {
        return noOfReports;
    }

    public void setNoOfReports(int noOfReports) {
        this.noOfReports = noOfReports;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNoOfWorkers() {
        return noOfWorkers;
    }

    public void setNoOfWorkers(int noOfWorkers) {
        this.noOfWorkers = noOfWorkers;
    }

    public boolean isApplied() {
        return isApplied;
    }

    public void setApplied(boolean applied) {
        isApplied = applied;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(salary);
        dest.writeString(currency);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeLong(longitude);
        dest.writeLong(latitude);
        dest.writeLong(rating);
        dest.writeString(userId);
        dest.writeString(jobId);
        dest.writeInt(noOfWorkers);
        dest.writeByte((byte) (isApplied ? 1 : 0));
        dest.writeByte((byte) (isReported ? 1 : 0));
        dest.writeInt(noOfRaters);
        dest.writeInt(noOfReports);
        dest.writeString(userImage);
        dest.writeString(userName);
    }
}
