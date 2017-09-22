package com.sameetahmed.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.fragments.StepDetailFragment;
import com.sameetahmed.bakingtime.model.Step;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = StepDetailActivity.class.getSimpleName();
    private Step mStep;
    private ArrayList<Step> mStepsList;
    public static final String STEP_PARCEL_KEY = "parcelKey";
    public static final String STEP_LIST_PARCEL_KEY = "parcelkey";
    private static final String STEP_ID_PARCEL_KEY = "idkey";
    private int mStepId;
    private StepDetailFragment mStepDetailFragment;
    private static final String SAVE_STATE_KEY = "saveKey";
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);


        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle(SAVE_STATE_KEY);
            mStep = savedInstanceState.getParcelable(STEP_PARCEL_KEY);
            mStepsList = savedInstanceState.getParcelableArrayList(STEP_LIST_PARCEL_KEY);
            mStepId = savedInstanceState.getInt(STEP_ID_PARCEL_KEY);
            createFragment();

        } else {
            mBundle = new Bundle();

            Intent intent = getIntent();
            if (intent != null) {
                mStep = intent.getParcelableExtra(STEP_PARCEL_KEY); // Get's the Step from the Adapter
                mStepsList = intent.getParcelableArrayListExtra(STEP_LIST_PARCEL_KEY); // Get's the steps list
            }

            mStepId = mStep.getId();

            createFragment();
        }
    }

    private void createFragment() {
        mBundle.putParcelable(STEP_PARCEL_KEY, mStep); // Puts the passed in mStep into a mBundle
        mBundle.putParcelableArrayList(STEP_LIST_PARCEL_KEY, mStepsList);

        mStepDetailFragment = new StepDetailFragment();
        mStepDetailFragment.setArguments(mBundle); // Passes the recipe to the detail fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_step_detail, mStepDetailFragment)
                .commit();
    }

    public void nextStep(View view) {
        view.findViewById(R.id.fab_next);
        if (mStep.getId() <= mStepsList.size()) {
            try {
                mStep = mStepsList.get(mStepId + 1);
                mStepId++;
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "Can't go past the last index");
            }
        }
        createFragment();
    }

    public void previousStep(View view) {
        view.findViewById(R.id.fab_previous);
        if (mStep.getId() > 0) {
            mStep = mStepsList.get(mStepId - 1);
            mStepId--;
        }
        createFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(SAVE_STATE_KEY, mBundle);
        outState.putParcelable(STEP_PARCEL_KEY, mStep);
        outState.putParcelableArrayList(STEP_LIST_PARCEL_KEY, mStepsList);
        outState.putInt(STEP_ID_PARCEL_KEY, mStepId);

    }
}
