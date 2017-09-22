package com.sameetahmed.bakingtime.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.model.Recipe;
import com.sameetahmed.bakingtime.ui.RecipeDetailActivity;
import com.sameetahmed.bakingtime.widget.RecipeWidget;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.ViewHolder> {
    private ArrayList<Recipe> mRecipesList;
    private Context mContext;
    public static final String DETAIL_PARCEL_KEY = "parcelKey";
    public static final String WIDGET_PARCEL_KEY = "widgetParcelKey";
    public static final String SHARED_PREFS_KEY = "SHARED_PREFS_KEY";

    public MasterListAdapter(ArrayList<Recipe> recipes, Context context) {
        this.mRecipesList = recipes;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mRecipeName;
        public TextView mRecipeServing;
        public ImageView mRecipeImage;
        public View mRecipeView;

        public ViewHolder(View itemView) {
            super(itemView);
            mRecipeName = (TextView) itemView.findViewById(R.id.tv_recipe_name);
            mRecipeServing = (TextView) itemView.findViewById(R.id.tv_servings);
            mRecipeImage = (ImageView) itemView.findViewById(R.id.recipe_card_image);
            mRecipeView = itemView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recipe_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Recipe recipe = mRecipesList.get(position);
        holder.mRecipeName.setText(recipe.getName());
        holder.mRecipeServing.setText("Servings: " + Integer.toString(recipe.getServings()));

        switch (position) {
            case 0:
                Picasso.with(mContext)
                        .load(R.drawable.nutella)
                        .into(holder.mRecipeImage);
                break;
            case 1:
                Picasso.with(mContext)
                    .load(R.drawable.brownie)
                    .into(holder.mRecipeImage);
                break;
            case 2:
                Picasso.with(mContext)
                        .load(R.drawable.yellow_cake)
                        .into(holder.mRecipeImage);
                break;
            case 3:
                Picasso.with(mContext)
                        .load(R.drawable.cheesecake)
                        .into(holder.mRecipeImage);
                break;
        }

        // When recipe card is clicked, open the detail activity
        holder.mRecipeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipeParcel = recipe;
                updateWidgetIngredients(recipe);
                Intent detailIntent = new Intent(mContext, RecipeDetailActivity.class);
                Intent widgetIntent = new Intent(mContext, RecipeWidget.class);
                widgetIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                widgetIntent.putExtra(WIDGET_PARCEL_KEY, recipeParcel);
                detailIntent.putExtra(DETAIL_PARCEL_KEY, recipeParcel);
                mContext.startActivity(detailIntent);
                mContext.sendBroadcast(widgetIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (mRecipesList == null) return 0;
        return mRecipesList.size();
    }

    private void updateWidgetIngredients(Recipe recipe) {
        Gson gson = new Gson();
        String json = gson.toJson(recipe.getIngredientList());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFS_KEY, json).apply();
    }
}
