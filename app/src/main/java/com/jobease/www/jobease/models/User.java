package com.jobease.www.jobease.models;


import com.jobease.www.jobease.Utilities.Logging;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dell on 21/07/2017.
 */


public class User {

    private String uid;
    private String image;
    private String name;
    private String birthDate;
    private String userPhone;
    private long rating;
    private int noPosts;
    private int noFlags;
    private int noApplies;
    private String address;
    private double longitude;
    private double latitude;
    private boolean isFlaged;

    public User() {
    }

    public User(JSONObject jsonObject) {
        try {
            if (jsonObject.has("uid")) {
                uid = jsonObject.getString("uid");
            }
            if (jsonObject.has("address")) {
                address = jsonObject.getString("address");
            }
            if (jsonObject.has("image")) {
                image = jsonObject.getString("image");
            }
            if (jsonObject.has("name")) {
                name = jsonObject.getString("name");
            }
            if (jsonObject.has("birthDate")) {
                birthDate = jsonObject.getString("birthDate");
            }
            if (jsonObject.has("userPhone")) {
                userPhone = jsonObject.getString("userPhone");
            }
            if (jsonObject.has("rating")) {
                rating = jsonObject.getLong("rating");
            }
            if (jsonObject.has("longitude")) {
                longitude = jsonObject.getDouble("longitude");
            }
            if (jsonObject.has("latitude")) {
                latitude = jsonObject.getDouble("latitude");
            }
            if (jsonObject.has("noPosts")) {
                noPosts = jsonObject.getInt("noPosts");
            }
            if (jsonObject.has("noFlags")) {
                noFlags = jsonObject.getInt("noFlags");
            }
            if (jsonObject.has("noApplies")) {
                noApplies = jsonObject.getInt("noApplies");
            }
            if (jsonObject.has("isFlaged")) {
                isFlaged = jsonObject.getBoolean("isFlaged");
            }
        } catch (JSONException e) {
            Logging.log(e.getMessage());
        }
    }

    public int getNoApplies() {
        return noApplies;
    }

    public void setNoApplies(int noApplies) {
        this.noApplies = noApplies;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public int getNoPosts() {
        return noPosts;
    }

    public void setNoPosts(int noPosts) {
        this.noPosts = noPosts;
    }

    public int getNoFlags() {
        return noFlags;
    }

    public void setNoFlags(int noFlags) {
        this.noFlags = noFlags;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isFlaged() {
        return isFlaged;
    }

    public void setFlaged(boolean flaged) {
        isFlaged = flaged;
    }


}
