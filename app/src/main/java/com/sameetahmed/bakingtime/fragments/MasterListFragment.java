package com.sameetahmed.bakingtime.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.adapters.MasterListAdapter;
import com.sameetahmed.bakingtime.model.Recipe;

import java.util.ArrayList;


public class MasterListFragment extends android.support.v4.app.Fragment {
    private static final String LOG_TAG = MasterListFragment.class.getSimpleName();
    private ArrayList<Recipe> mRecipeList;
    private static final String PARCEL_KEY = "parcelKey";


    public MasterListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRecipeList = bundle.getParcelableArrayList(PARCEL_KEY);
        } else {
            Log.i(LOG_TAG, "Failed to get the recipe data");
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_master_list_recipe, container, false);

        RecyclerView recyclerRecipeList = view.findViewById(R.id.recipe_list_recycler);

        MasterListAdapter masterListAdapter = new MasterListAdapter(mRecipeList, getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerRecipeList.setAdapter(masterListAdapter);

        recyclerRecipeList.setLayoutManager(linearLayoutManager);
        recyclerRecipeList.setHasFixedSize(true);

        return view;
    }

}
