package com.sameetahmed.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sameetahmed.bakingtime.MyInterface;
import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.fragments.StepDetailFragment;
import com.sameetahmed.bakingtime.fragments.StepListFragment;
import com.sameetahmed.bakingtime.model.Recipe;
import com.sameetahmed.bakingtime.model.Step;

import java.util.ArrayList;

import static com.sameetahmed.bakingtime.ui.StepDetailActivity.STEP_LIST_PARCEL_KEY;
import static com.sameetahmed.bakingtime.ui.StepDetailActivity.STEP_PARCEL_KEY;

public class StepMasterListActivity extends AppCompatActivity implements MyInterface {
    private static final String LOG_TAG = StepMasterListActivity.class.getSimpleName();
    private Recipe mRecipe;
    private ArrayList<Step> mStepsList;
    private static final String PARCEL_KEY = "parcelKey";
    private Boolean mTabletMode = false;
    private StepDetailFragment mStepDetailFragment;
    private int mStepId;
    private Step mStep;
    private StepListFragment mStepListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_master_list);

        ActionBar actionBar = getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            mRecipe = intent.getParcelableExtra(PARCEL_KEY);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCEL_KEY, mRecipe);

        if (savedInstanceState == null) {

            mStepListFragment = new StepListFragment();
            mStepListFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.master_step_list_container, mStepListFragment)
                    .commit();

            if (findViewById(R.id.step_detail_container) != null) {
                mTabletMode = true;
                mStepListFragment = new StepListFragment().newInstance(this);
                mStepListFragment.setArguments(bundle);

                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.master_step_list_container, mStepListFragment)
                        .commit();

                mStepDetailFragment = new StepDetailFragment();
                Bundle args = new Bundle();
                mStepsList = mRecipe.getStepsList();
                mStep = mStepsList.get(0);
                mStepId = mStep.getId();

                args.putParcelableArrayList(STEP_LIST_PARCEL_KEY, mStepsList);
                args.putParcelable(STEP_PARCEL_KEY, mStep);
                mStepDetailFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_detail_container, mStepDetailFragment).commit();
            }
        }
    }

    @Override
    public void handleClick(int position) {
        if (isTablet() && mStepDetailFragment != null) {
            replaceFragment(position);
        } else {
            launchStepDetailActivity(position);
        }
    }

    private void launchStepDetailActivity(int position) {
        Intent detailIntent = new Intent(this, StepDetailActivity.class);
        detailIntent.putParcelableArrayListExtra(STEP_LIST_PARCEL_KEY, mStepsList);
        detailIntent.putExtra(STEP_PARCEL_KEY, mStepsList.get(position));
        startActivity(detailIntent);
    }

    public boolean isTablet() {
        return mTabletMode;
    }

    private void replaceFragment(int position) {
        Log.i("tab", "replace");
        Bundle args = new Bundle();
        args.putParcelableArrayList(STEP_LIST_PARCEL_KEY, mStepsList);
        args.putParcelable(STEP_PARCEL_KEY, mStepsList.get(position));
        mStepDetailFragment = new StepDetailFragment();
        mStepDetailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_container, mStepDetailFragment).commit();
    }

    public void nextStep(View view) {
        view.findViewById(R.id.fab_next);
        if (mStepId <= mStepsList.size()) {
            try {
                mStep = mStepsList.get(mStepId + 1);
                mStepId++;
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "Can't go past the last index");
            }
        }
        replaceFragment(mStepId);
    }

    public void previousStep(View view) {
        view.findViewById(R.id.fab_previous);
        if (mStepId > 0) {
            mStep = mStepsList.get(mStepId - 1);
            mStepId--;
        }
        replaceFragment(mStepId);
    }
}
