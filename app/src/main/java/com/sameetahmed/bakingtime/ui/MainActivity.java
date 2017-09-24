package com.sameetahmed.bakingtime.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.SimpleIdlingResource;
import com.sameetahmed.bakingtime.fragments.MasterListFragment;
import com.sameetahmed.bakingtime.model.Ingredient;
import com.sameetahmed.bakingtime.model.Recipe;
import com.sameetahmed.bakingtime.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static ArrayList<Recipe> mRecipeList = new ArrayList<>(); // This is the main list of recipes that will be passed around
    private static final String PARCEL_KEY = "parcelKey";
    private static final String SAVE_RECIPE_KEY = "saveRecipes";
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static OkHttpClient mClient = null;
    @Nullable private SimpleIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // For testing only
        getIdlingResource();

        if (savedInstanceState != null) {
            mRecipeList = savedInstanceState.getParcelableArrayList(SAVE_RECIPE_KEY);
        } else {
            if (isOnline()) {
                // Performs network request & gets recipes
                // Passes recipe data to fragments
                mRecipeList = connectToAPI();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_RECIPE_KEY, mRecipeList);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        // if running on emulator return true always.
        return android.os.Build.MODEL.equals("google_sdk");
    }

    private void parseJSON(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject recipeProperties = jsonArray.getJSONObject(i);
            Recipe recipe = new Recipe(
                    recipeProperties.getInt(getString(R.string.recipe_id)), // Sets the id of the recipe
                    recipeProperties.getString(getString(R.string.recipe_name)), // Sets the name of the recipe,
                    null,
                    null,
                    recipeProperties.getString(getString(R.string.recipe_image)), // Get's the imageUrl of the recipe which is always null
                    recipeProperties.getInt(getString(R.string.recipe_servings)) // Sets the servings of the recipe

            );

            ArrayList<Ingredient> ingredientList = new ArrayList<>();
            JSONArray ingredientsArray = recipeProperties.getJSONArray(getString(R.string.ingredients));
            for (int j = 0; j < ingredientsArray.length(); j++) {
                JSONObject ingredientProperties = ingredientsArray.getJSONObject(j);
                Ingredient ingredient = new Ingredient(
                        ingredientProperties.getDouble(getString(R.string.quantity)),
                        ingredientProperties.getString(getString(R.string.measure)),
                        ingredientProperties.getString(getString(R.string.ingredient))
                );
                ingredientList.add(ingredient); // Add each ingredient to the list of ingredients
            }

            ArrayList<Step> stepList = new ArrayList<>();
            JSONArray stepsArray = recipeProperties.getJSONArray(getString(R.string.steps));
            for (int k = 0; k < stepsArray.length(); k++) {
                JSONObject stepsProperties = stepsArray.getJSONObject(k);
                Step step = new Step(
                        stepsProperties.getInt(getString(R.string.step_id)),
                        stepsProperties.getString(getString(R.string.step_short_desc)),
                        stepsProperties.getString(getString(R.string.step_desc)),
                        stepsProperties.getString(getString(R.string.step_video)),
                        stepsProperties.getString(getString(R.string.thumbnail))
                );
                stepList.add(step);
            }

            recipe.setIngredientList(ingredientList); // Add the ingredients to the recipe
            recipe.setStepsList(stepList); // Add the steps to the recipe
            mRecipeList.add(recipe);
        }
    }

    private ArrayList<Recipe> connectToAPI() {
        if (mClient == null) {
            mClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .build();

            Call call = mClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(LOG_TAG, getString(R.string.failed_api));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(LOG_TAG, getString(R.string.success_api));
                    try {
                        parseJSON(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Bundle bundle = new Bundle();
                    if (mRecipeList != null) {
                        bundle.putParcelableArrayList(PARCEL_KEY, mRecipeList); // Store the recipe list in a bundle
                    }
                    // Add the fragment and pass the recipe details to display the recipe list
                    MasterListFragment masterListFragment = new MasterListFragment();
                    masterListFragment.setArguments(bundle); // Passing the bundle w/Recipes to fragment

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_master_container, masterListFragment)
                            .commit();

                }
            });
        } else {
            Log.i(LOG_TAG, getString(R.string.no_internet));
        }
        return mRecipeList;
    }

    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

}
