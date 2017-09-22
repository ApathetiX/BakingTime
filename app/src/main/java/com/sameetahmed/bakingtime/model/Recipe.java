package com.sameetahmed.bakingtime.model;



import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable {
    private int mId;
    private String mName;
    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Step> mStepsList;
    private String mImageUrl;
    private int mServings;

    public Recipe(int id, String name, ArrayList<Ingredient> ingredientList, ArrayList<Step> stepsList, String imageUrl, int servings) {
        mId = id;
        mName = name;
        mIngredientList = ingredientList;
        mStepsList = stepsList;
        mImageUrl = imageUrl;
        mServings = servings;
    }

    public Recipe() {} // Empty constructor

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return mIngredientList;
    }

    public ArrayList<Step> getStepsList() {
        return mStepsList;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public int getServings() {
        return mServings;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        mIngredientList = ingredientList;
    }

    public void setStepsList(ArrayList<Step> stepsList) {
        mStepsList = stepsList;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public void setServings(int servings) {
        mServings = servings;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeTypedList(mIngredientList);
        dest.writeTypedList(mStepsList);
        dest.writeString(mImageUrl);
        dest.writeInt(mServings);
    }

    protected Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mIngredientList = in.createTypedArrayList(Ingredient.CREATOR);
        mStepsList = in.createTypedArrayList(Step.CREATOR);
        mImageUrl = in.readString();
        mServings = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
