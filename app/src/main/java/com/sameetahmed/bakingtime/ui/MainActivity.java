package com.sameetahmed.bakingtime.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sameetahmed.bakingtime.R;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mRecipeList = savedInstanceState.getParcelableArrayList(SAVE_RECIPE_KEY);
        } else {
            if (isOnline()) {
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
                    recipeProperties.getInt("id"), // Sets the id of the recipe
                    recipeProperties.getString("name"), // Sets the name of the recipe,
                    null,
                    null,
                    recipeProperties.getString("image"), // Get's the imageUrl of the recipe which is always null
                    recipeProperties.getInt("servings") // Sets the servings of the recipe

            );

            ArrayList<Ingredient> ingredientList = new ArrayList<>();
            JSONArray ingredientsArray = recipeProperties.getJSONArray("ingredients");
            for (int j = 0; j < ingredientsArray.length(); j++) {
                JSONObject ingredientProperties = ingredientsArray.getJSONObject(j);
                Ingredient ingredient = new Ingredient(
                        ingredientProperties.getDouble("quantity"),
                        ingredientProperties.getString("measure"),
                        ingredientProperties.getString("ingredient")
                );
                ingredientList.add(ingredient); // Add each ingredient to the list of ingredients
            }

            ArrayList<Step> stepList = new ArrayList<>();
            JSONArray stepsArray = recipeProperties.getJSONArray("steps");
            for (int k = 0; k < stepsArray.length(); k++) {
                JSONObject stepsProperties = stepsArray.getJSONObject(k);
                Step step = new Step(
                        stepsProperties.getInt("id"),
                        stepsProperties.getString("shortDescription"),
                        stepsProperties.getString("description"),
                        stepsProperties.getString("videoURL")
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
                    Log.i(LOG_TAG, "Failed to connect to the API");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(LOG_TAG, "Successfully connected to the API!");
                    try {
                        parseJSON(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Bundle bundle = new Bundle();
                    if (mRecipeList != null) {
                        bundle.putParcelableArrayList(PARCEL_KEY, mRecipeList); // Store the recipe list in a bundle
                    }

                    MasterListFragment masterListFragment = new MasterListFragment();
                    masterListFragment.setArguments(bundle); // Passing the bundle w/Recipes to fragment

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_master_container, masterListFragment)
                            .commit();

                }
            });
        } else {
            Log.i(LOG_TAG, "No internet connection available :(");
        }
        return mRecipeList;
    }
}
