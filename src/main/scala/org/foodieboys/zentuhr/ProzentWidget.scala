package org.foodieboys.zentuhr

import java.util._

import android.app.{Service, AlarmManager, PendingIntent}
import android.appwidget.{AppWidgetManager, AppWidgetProvider}
import android.content.{ComponentName, Intent, Context}
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews

object ProzentWidget {
//  val TOUCH_WIDGET = "TouchWidget"
  var timer:Timer = null
  var timerTask:TimerTask = null
  def cancelTimer(): Unit = {
    if(timerTask != null) {
      timerTask.cancel()
    }
    if(timer != null) {
      Log.i("Ur", "Timär Cancel")
      timer.cancel()
    }
  }

  def createTimerTask(context:Context, task:() => Unit) = new TimerTask { def run() {task()} }
  def initTimer(context:Context, task:() => Unit): Unit = {
    cancelTimer()
    timer = new Timer()
    timerTask = createTimerTask(context, task)
    timer.scheduleAtFixedRate(timerTask , 1000, 1000/5)
  }
}

class ProzentWidget extends AppWidgetProvider {
  import ProzentWidget._
  Log.i ("Ur", "Baääääääh" +this)

  override def onReceive(context: Context, intent: Intent): Unit = {
    Log.i("Ur", "receive: " + intent.getAction)
    intent.getAction match {
//      case TOUCH_WIDGET =>
//        val widgetId = intent.getData().getLastPathSegment.toInt
//        updateAppWidget(context, AppWidgetManager.getInstance(context), widgetId)
      case AppWidgetManager.ACTION_APPWIDGET_DISABLED =>
        Log.i("Ur", "Disable Alf")
        timerTask.cancel()
        Log.i("Ur", "Timär Cancel")
        timer.cancel()
      case _ =>
    }

    super.onReceive(context, intent)
  }



  override def onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: Array[Int]) {
    Log.i("Ur", "onPudate")

      initTimer(context, () => updateAllAppWidgets(context))

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

  override def onDisabled(context: Context) = {
    cancelTimer()
    super.onDisabled(context)
  }

  def updateAllAppWidgets (context: Context): Unit = {
//    Log.i("Ur", "updateAlf")
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val wIds = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName, classOf[ProzentWidget].getName))
    // Log.i("Ur", "wIds" + wIds.toList)
    for (id <- wIds) {
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

