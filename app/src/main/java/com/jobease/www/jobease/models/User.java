package com.jobease.www.jobease.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell on 21/07/2017.
 */

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {
    }

    private String image;
    private String name;
    private String birthDate;
    private long rating;
    private int noPosts;
    private int noFlags;
    private String address;
    private long longitude;
    private long latitude;
    private boolean isFlaged;

    public User(Parcel in) {
        image = in.readString();
        name = in.readString();
        birthDate = in.readString();
        rating = in.readLong();
        noPosts = in.readInt();
        noFlags = in.readInt();
        address = in.readString();
        longitude = in.readLong();
        latitude = in.readLong();
        isFlaged = in.readByte() != 0;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
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

    public boolean isFlaged() {
        return isFlaged;
    }

    public void setFlaged(boolean flaged) {
        isFlaged = flaged;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(birthDate);
        dest.writeLong(rating);
        dest.writeInt(noPosts);
        dest.writeInt(noFlags);
        dest.writeString(address);
        dest.writeLong(longitude);
        dest.writeLong(latitude);
        dest.writeByte((byte) (isFlaged ? 1 : 0));
    }
}
