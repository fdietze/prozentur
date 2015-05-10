package org.foodieboys.prozentur

import java.util.{Calendar, Date, GregorianCalendar}

import android.appwidget.{AppWidgetManager, AppWidgetProvider}
import android.content.Context
import android.util.Log
import android.widget.RemoteViews


class EineUr extends AppWidgetProvider {

  override def onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: Array[Int]) {
    // There may be multiple widgets active, so update all of them
    for (id <- appWidgetIds)
      updateAppWidget(context, appWidgetManager, id)
  }


  override def onEnabled(context: Context) {
    // Enter relevant functionality for when the first widget is created
  }

  override def onDisabled(context: Context) {
    // Enter relevant functionality for when the last widget is disabled
  }

  def updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

    val calendar = new GregorianCalendar()
    calendar.setTime(new Date())

    val daySeconds = calendar.get(Calendar.HOUR_OF_DAY) * 3600000 +
      calendar.get(Calendar.MINUTE) * 60000 +
      calendar.get(Calendar.SECOND) * 1000 +
      calendar.get(Calendar.MILLISECOND)

    val percent = daySeconds / 864000.0

    Log.i("Ur", "jetzt")


    val views = new RemoteViews(context.getPackageName, R.layout.eine_ur)
    views.setTextViewText(R.id.appwidget_text, "%5.2f%%" format percent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
  }
}

