package com.sameetahmed.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.sameetahmed.bakingtime.R;
import com.sameetahmed.bakingtime.adapters.MasterListAdapter;
import com.sameetahmed.bakingtime.model.Ingredient;
import com.sameetahmed.bakingtime.model.Recipe;

import java.util.ArrayList;


public class RecipeWidget extends AppWidgetProvider {
    private Recipe mRecipe;
    public static ArrayList<Ingredient> mIngredientsList;
    private static final String ACTION_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), RecipeWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        if (ACTION_UPDATE.equals(intent.getAction())) {
            mRecipe = intent.getParcelableExtra(MasterListAdapter.WIDGET_PARCEL_KEY);
        }

        if (mRecipe != null) {
            mIngredientsList = mRecipe.getIngredientList();
        }

        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, WidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.putExtra(WidgetRemoteViewsService.WidgetRemoteViewsFactory.GET_INGREDIENTS, mIngredientsList);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);

            rv.setRemoteAdapter(R.id.list_widget_ingredients, intent);

            if (mRecipe != null) {
                rv.setTextViewText(R.id.widget_label, mRecipe.getName());
            }

            appWidgetManager.updateAppWidget(appWidgetId, rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_widget_ingredients);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

