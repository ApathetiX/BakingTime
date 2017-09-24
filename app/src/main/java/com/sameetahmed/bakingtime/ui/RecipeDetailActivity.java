package com.sameetahmed.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.adapters.MasterListAdapter;
import com.sameetahmed.bakingtime.fragments.RecipeDetailFragment;
import com.sameetahmed.bakingtime.model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {
    private Recipe mRecipe;
    public static final String PARCEL_KEY = "parcelKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ActionBar actionBar = getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            mRecipe = intent.getParcelableExtra(MasterListAdapter.DETAIL_PARCEL_KEY); // Get's the recipe from the Adapter
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCEL_KEY, mRecipe); // Puts the passed in mRecipe into a bundle

        if (savedInstanceState == null) {

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle); // Passes the recipe to the detail fragment

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
        }
    }

}
