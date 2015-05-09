package org.foodieboys.prozentur;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Implementation of App Widget functionality.
 */
public class EinUr extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);
        long daySeconds = calendar.get(Calendar.HOUR_OF_DAY) * 3600000 + calendar.get(Calendar.MINUTE) * 60000 + calendar.get(Calendar.SECOND) * 1000 + calendar.get(Calendar.MILLISECOND);
        double percent = daySeconds / 864000.0;

        Log.i("Ur", "jetzt");


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ein_ur);
        views.setTextViewText(R.id.appwidget_text, String.format("%5.2f%%", percent));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

