package com.udacity.stockhawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockAdapter;

import timber.log.Timber;

/**
 * Created by andreas on 26/05/17.
 */

public class StockWidgetIntentService extends IntentService {

    public StockWidgetIntentService() {
        super("StockWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                StockWidgetProvider.class));

        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.widget_medium_view;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            Timber.d("Handled Widget Intent");
            setRemoteAdapter(getApplicationContext(), views);

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list_view,
                new Intent(context, StockWidgetViewsService.class));
    }

}
