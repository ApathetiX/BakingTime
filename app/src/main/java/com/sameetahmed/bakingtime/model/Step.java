package com.sameetahmed.bakingtime.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable{
    private int mId;

    private String mShortDesc;

    private String mDesc;

    private String mVideoUrl;

    private String mImage;

    public Step(int id, String shortDesc, String desc, String videoUrl, String imageThumb) {
        mId = id;
        mShortDesc = shortDesc;
        mDesc = desc;
        mVideoUrl = videoUrl;
        mImage = imageThumb;
    }

    public int getId() {
        return mId;
    }

    public String getShortDesc() {
        return mShortDesc;
    }

    public String getDesc() {
        return mDesc;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mShortDesc);
        dest.writeString(mDesc);
        dest.writeString(mVideoUrl);
        dest.writeString(mImage);
    }

    protected Step(Parcel in) {
        mId = in.readInt();
        mShortDesc = in.readString();
        mDesc = in.readString();
        mVideoUrl = in.readString();
        mImage = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public String toString() {
        return super.toString();
    }
}
