package com.sameetahmed.bakingtime.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sameetahmed.bakingtime.MyInterface;
import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.model.Step;
import com.sameetahmed.bakingtime.ui.StepDetailActivity;

import java.util.ArrayList;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.ViewHolder> {
    private ArrayList<Step> mStepList;
    private Context mContext;
    private static final String PARCEL_KEY = "parcelKey";
    private static final String STEP_LIST_PARCEL_KEY = "parcelkey";
    private MyInterface mListener;

    public StepsListAdapter(ArrayList<Step> steps, Context context, MyInterface listener) {
        this.mStepList = steps;
        this.mContext = context;
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mStep;
        public View mStepView;

        public ViewHolder(View itemView) {
            super(itemView);
            mStep = itemView.findViewById(R.id.tv_master_step_list);
            mStepView = itemView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_master_steps, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Step step = mStepList.get(position);



        holder.mStep.setText("Step: " + step.getId() + "\n" + step.getShortDesc());
        holder.mStepView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.handleClick(position);
                }
                Step stepParcel = step;
                Intent intent = new Intent(mContext, StepDetailActivity.class);
                intent.putExtra(PARCEL_KEY, stepParcel);
                intent.putParcelableArrayListExtra(STEP_LIST_PARCEL_KEY, mStepList);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mStepList == null) return 0;
        return mStepList.size();
    }
}
