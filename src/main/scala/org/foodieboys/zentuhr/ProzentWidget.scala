package org.foodieboys.zentuhr

import java.util.{Calendar, Date, GregorianCalendar}

import android.app.PendingIntent
import android.appwidget.{AppWidgetManager, AppWidgetProvider}
import android.content.{ComponentName, Intent, Context}
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews

object ProzentWidget {
  val TOUCH_WIDGET = "TouchWidget"
}

class ProzentWidget extends AppWidgetProvider {

  import ProzentWidget._

  override def onReceive(context: Context, intent: Intent): Unit = {
    super.onReceive(context, intent)
    Log.i("Ur", "receive: " + intent.getAction)
    intent.getAction match {
      case TOUCH_WIDGET =>
        val widgetId = intent.getData().getLastPathSegment.toInt
        updateAppWidget(context, AppWidgetManager.getInstance(context), widgetId)
      case _ =>
    }
  }

  override def onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: Array[Int]) {
    def registerWidgetTouch(id: Int) = {
      val intent = new Intent(context, classOf[ProzentWidget])
      intent.setAction(TOUCH_WIDGET)
      intent.setData(Uri.parse(s"zentuhr://widgets/$id"))

      val pendingIntent = PendingIntent.getBroadcast(context, 0 , intent, 0)
      val views = new RemoteViews(context.getPackageName, R.layout.prozent_widget)
      views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)
      appWidgetManager.updateAppWidget(id, views)
    }

    // There may be multiple widgets active, so update all of them
    for (id <- appWidgetIds) {
      registerWidgetTouch(id)
      updateAppWidget(context, appWidgetManager, id)
    }
  }


  override def onEnabled(context: Context) {
    // Enter relevant functionality for when the first widget is created
  }

  override def onDisabled(context: Context) {
    // Enter relevant functionality for when the last widget is disabled
  }

  def updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    Log.i("Ur", s"groovy, Widget $appWidgetId wurde erneuert")

    val calendar = new GregorianCalendar()
    calendar.setTime(new Date())

    val daySeconds = calendar.get(Calendar.HOUR_OF_DAY) * 3600000 +
      calendar.get(Calendar.MINUTE) * 60000 +
      calendar.get(Calendar.SECOND) * 1000 +
      calendar.get(Calendar.MILLISECOND)

    val percent = daySeconds / 864000.0



    val views = new RemoteViews(context.getPackageName, R.layout.prozent_widget)
    views.setTextViewText(R.id.appwidget_text, "%6.3f%%" format percent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
  }
}

