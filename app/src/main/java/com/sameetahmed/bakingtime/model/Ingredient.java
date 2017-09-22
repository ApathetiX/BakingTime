package com.sameetahmed.bakingtime.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{
    private double mQuantity;

    private String mMeasurement;

    private String mIngredient;

    public Ingredient(double quantity, String mesurement, String ingredient) {
        mQuantity = quantity;
        mMeasurement = mesurement;
        mIngredient = ingredient;
    }

    public Ingredient() {}

    public double getQuantity() {
        return mQuantity;
    }

    public String getMesurement() {
        return mMeasurement;
    }

    public String getIngredient() {
        return mIngredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mQuantity);
        dest.writeString(mMeasurement);
        dest.writeString(mIngredient);
    }

    protected Ingredient(Parcel in) {
        mQuantity = in.readDouble();
        mMeasurement = in.readString();
        mIngredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public String toString() {
        return super.toString();
    }
}
