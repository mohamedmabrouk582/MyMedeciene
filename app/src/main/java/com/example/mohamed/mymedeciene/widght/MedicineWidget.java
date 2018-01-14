package com.example.mohamed.mymedeciene.widght;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.SplashActivity;

/**
 * Implementation of App Widget functionality.
 */
@SuppressWarnings("unused")
public class MedicineWidget extends AppWidgetProvider {
    private static final String ACTION_TOAST = "toast";

    @Override
    public void onReceive(Context context, Intent intent) {

        //noinspection ConstantConditions
        if (intent.getAction().equals(ACTION_TOAST)) {
            Intent intent1 = new Intent(context, SplashActivity.class);
            context.startActivity(intent1);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);
            appWidgetManager.updateAppWidget(widgetId, mView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.medicine_widget
        );

        Intent intent1 = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
        mView.setOnClickPendingIntent(R.id.main_layout, pendingIntent);

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter(widgetId, R.id.widgetCollectionList, intent);

        return mView;
    }
}

