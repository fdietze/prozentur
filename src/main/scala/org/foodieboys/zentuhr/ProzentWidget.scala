package org.foodieboys.zentuhr

import java.util._

import android.appwidget.{AppWidgetManager, AppWidgetProvider}
import android.content._
import android.os.PowerManager
import android.util.Log
import android.widget.RemoteViews

object ProzentWidget {
  //  val TOUCH_WIDGET = "TouchWidget"
  val scheduler = new Scheduler(500, 864)
}

class ProzentWidget extends AppWidgetProvider {

  import ProzentWidget._

  Log.i("Ur", "Baääääääh" + this)


  override def onReceive(context: Context, intent: Intent): Unit = {
    import AppWidgetManager._
    import Intent._
    Log.i("Ur", "receive: " + intent.getAction)
    intent.getAction match {
      //      case TOUCH_WIDGET =>
      //        val widgetId = intent.getData().getLastPathSegment.toInt
      //        updateAppWidget(context, AppWidgetManager.getInstance(context), widgetId)
      case _ =>
    }

    intent.getAction match {
      // TODO: BATTERY_LOW/OK
      // TODO: ACTION_TIMEZONE_CHANGED	 Broadcast Action: The timezone has changed.
      // TODO: ACTION_TIME_CHANGED	 Broadcast Action: The time was set.
      // TODO: ACTION_TIME_TICK Broadcast Action: The current time has changed.

      case ACTION_APPWIDGET_ENABLED | ACTION_APPWIDGET_UPDATE | ACTION_USER_PRESENT =>
        Log.i("Ur", "Los gehts")
        scheduler.start(() => updateAllAppWidgets(context))
      case ACTION_APPWIDGET_DISABLED =>
        Log.i("Ur", "Vorbei")
        scheduler.stop()
      case _ =>
    }

    super.onReceive(context, intent)
  }


  override def onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: Array[Int]) {
    Log.i("Ur", "onPudate")

    //      def registerWidgetTouch(id: Int) = {
    //      val intent = new Intent(context, classOf[ProzentWidget])
    //      intent.setAction(TOUCH_WIDGET)
    //      intent.setData(Uri.parse(s"zentuhr://widgets/$id"))
    //
    //      val pendingIntent = PendingIntent.getBroadcast(context, 0 , intent, 0)
    //      val views = new RemoteViews(context.getPackageName, R.layout.prozent_widget)
    //      views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)
    //      appWidgetManager.updateAppWidget(id, views)
    //    }
    //
    //    // register touch events for all widgets
    //    for (id <- appWidgetIds) {
    //      registerWidgetTouch(id)
    //    }

    updateAllAppWidgets(context)
  }

  def updateAllAppWidgets(context: Context): Unit = {
    val powerManager = context.getSystemService(Context.POWER_SERVICE).asInstanceOf[PowerManager]
    if(powerManager.isPowerSaveMode || !powerManager.isInteractive)
      scheduler.stop()

    //    Log.i("Ur", "updateAlf")
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val wIds = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName, classOf[ProzentWidget].getName))
    // Log.i("Ur", "wIds" + wIds.toList)
    for(id <- wIds) {
      updateAppWidget(context, appWidgetManager, id)
    }
  }

  def updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    //    Log.i("Ur", s"groovy, Widget $appWidgetId wurde erneuert")

    val calendar = new GregorianCalendar()
    calendar.setTime(new Date())

    val daySeconds = calendar.get(Calendar.HOUR_OF_DAY) * 3600000 +
      calendar.get(Calendar.MINUTE) * 60000 +
      calendar.get(Calendar.SECOND) * 1000 +
      calendar.get(Calendar.MILLISECOND)

    val percent = daySeconds / 864000.0



    val views = new RemoteViews(context.getPackageName, R.layout.prozent_widget)
    views.setTextViewText(R.id.appwidget_text, "%7.4f%%" format percent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
  }
}

