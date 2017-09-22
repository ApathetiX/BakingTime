package com.sameetahmed.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.adapters.MasterListAdapter;
import com.sameetahmed.bakingtime.model.Ingredient;

import java.util.ArrayList;


public class WidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private ArrayList<Ingredient> mIngredientsList;
        public static final String GET_INGREDIENTS = "ingredients";


        public WidgetRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mIngredientsList = intent.getParcelableExtra(GET_INGREDIENTS);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String json = preferences.getString(MasterListAdapter.SHARED_PREFS_KEY, "");
            if (!json.equals("")) {
                Gson gson = new Gson();
                mIngredientsList = gson.fromJson(json, new TypeToken<ArrayList<Ingredient>>() {
                }.getType());
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mIngredientsList == null) return 0;
            return mIngredientsList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),
                    R.layout.listview_widget_ingredients);

            remoteView.setTextViewText(R.id.widget_ingredient_name, mIngredientsList.get(i).getIngredient());
            remoteView.setTextViewText(R.id.widget_measurement, mIngredientsList.get(i).getMesurement());
            remoteView.setTextViewText(R.id.widget_quantity, Double.toString(mIngredientsList.get(i).getQuantity()));

            return remoteView;

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
