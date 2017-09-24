package com.sameetahmed.bakingtime.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.ui.StepMasterListActivity;
import com.sameetahmed.bakingtime.adapters.IngredientsListAdapter;
import com.sameetahmed.bakingtime.model.Ingredient;
import com.sameetahmed.bakingtime.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecipeDetailFragment extends Fragment {
    private static final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();
    private Recipe mRecipe;
    private List<Ingredient> mIngredients;
    private static final String PARCEL_KEY = "parcelKey";
    private ImageView mRecipeImage;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            mRecipe = bundle.getParcelable(PARCEL_KEY); // Get's the recipe details for this recipe
        } else {
            Log.i(LOG_TAG, getString(R.string.failed_recipe_detail));
        }

        // Stores the ingredients of the current recipe in the ingredients list
        mIngredients = mRecipe.getIngredientList();

        IngredientsListAdapter ingredientsListAdapter = new IngredientsListAdapter(mIngredients, getActivity());

        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mRecipeImage = (ImageView) view.findViewById(R.id.recipe_image);

        setRecipeImage();

        RecyclerView ingredientsRecyclerView = (RecyclerView) view.findViewById(R.id.ingredients_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        ingredientsRecyclerView.setAdapter(ingredientsListAdapter);

        ingredientsRecyclerView.setLayoutManager(linearLayoutManager);

        ingredientsRecyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ingredientsRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        ingredientsRecyclerView.addItemDecoration(dividerItemDecoration);

        TextView recipeName = (TextView) view.findViewById(R.id.tv_recipe_name);
        TextView numOfIngredients = (TextView) view.findViewById(R.id.tv_qty_ingredients);
        TextView servings = (TextView) view.findViewById(R.id.tv_servings);

        recipeName.setText(mRecipe.getName());
        numOfIngredients.setText(getString(R.string.num_ingredients) + mIngredients.size());
        servings.setText(getString(R.string.serves) + mRecipe.getServings());

        Button viewDirections = (Button) view.findViewById(R.id.button_view_directions);

        viewDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StepMasterListActivity.class);
                intent.putExtra(PARCEL_KEY, mRecipe);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setRecipeImage() {
        int recipeId = mRecipe.getId();

        switch (recipeId) {
            case 1:
                Picasso.with(getActivity())
                        .load(R.drawable.nutella)
                        .into(mRecipeImage);
                break;

            case 2:
                Picasso.with(getActivity())
                        .load(R.drawable.brownie)
                        .into(mRecipeImage);
                break;

            case 3:
                Picasso.with(getActivity())
                        .load(R.drawable.yellow_cake)
                        .into(mRecipeImage);
                break;

            case 4:
                Picasso.with(getActivity())
                        .load(R.drawable.cheesecake)
                        .into(mRecipeImage);
                break;
        }
    }
}
