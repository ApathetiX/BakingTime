package com.sameetahmed.bakingtime.fragments;

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

import com.sameetahmed.bakingtime.MyInterface;
import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.adapters.IngredientsListAdapter;
import com.sameetahmed.bakingtime.adapters.StepsListAdapter;
import com.sameetahmed.bakingtime.model.Ingredient;
import com.sameetahmed.bakingtime.model.Recipe;
import com.sameetahmed.bakingtime.model.Step;

import java.util.ArrayList;


public class StepListFragment extends Fragment {
    private static final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();
    private Recipe mRecipe;
    private ArrayList<Step> mStepList;
    private ArrayList<Ingredient> mIngredientList;
    private static final String PARCEL_KEY = "parcelKey";
    private MyInterface mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRecipe = bundle.getParcelable(PARCEL_KEY); // Get's the recipe details for this recipe
        } else {
            Log.i(LOG_TAG, getString(R.string.failed_recipe_detail));
        }

        mStepList = mRecipe.getStepsList(); // Stores the list of steps in the steps list

        mIngredientList = mRecipe.getIngredientList(); // Stores the list of ingredients

        StepsListAdapter stepsListAdapter = new StepsListAdapter(mStepList, getActivity(), mListener);

        IngredientsListAdapter ingredientsListAdapter = new IngredientsListAdapter(mIngredientList, getActivity());

        View view = inflater.inflate(R.layout.fragment_list_steps_master, container, false);

        RecyclerView stepRecyclerView = view.findViewById(R.id.recycler_step_list);

        RecyclerView ingredientsRecyclerView = view.findViewById(R.id.recycler_ingredients_list);

        ingredientsRecyclerView.setAdapter(ingredientsListAdapter);
        ingredientsRecyclerView.setHasFixedSize(false);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        stepRecyclerView.setAdapter(stepsListAdapter);
        stepRecyclerView.setHasFixedSize(true);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        stepRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return view;
    }

    public static StepListFragment newInstance(MyInterface listener) {
        StepListFragment stepListFragment = new StepListFragment();
        stepListFragment.mListener = listener;
        return stepListFragment;
    }

}
