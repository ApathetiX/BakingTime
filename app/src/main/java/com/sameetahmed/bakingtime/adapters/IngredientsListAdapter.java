package com.sameetahmed.bakingtime.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.model.Ingredient;

import java.util.List;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder> {
    private List<Ingredient> mIngredientsList;
    private Context mContext;

    public IngredientsListAdapter(List<Ingredient> ingredients, Context context) {
        this.mIngredientsList = ingredients;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mIngredientName;
        public TextView mMeasurement;
        public TextView mQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            mIngredientName = itemView.findViewById(R.id.tv_ingredient_name);
            mMeasurement = itemView.findViewById(R.id.tv_measurement);
            mQuantity = itemView.findViewById(R.id.tv_quantity);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_ingredients, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Ingredient ingredient = mIngredientsList.get(position);

        holder.mIngredientName.setText(ingredient.getIngredient());
        holder.mMeasurement.setText("Measurement: "  + ingredient.getMesurement());
        holder.mQuantity.setText("Quantity: " + ingredient.getQuantity());
    }

    @Override
    public int getItemCount() {
        if (mIngredientsList == null) return 0;
        return mIngredientsList.size();
    }
}
