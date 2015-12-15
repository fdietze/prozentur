package org.foodieboys.zentuhr

import java.util.{TimerTask, Timer}

class Scheduler(delay: Long, private var period: Long) {
  private var timer: Option[Timer] = None
  private var timerTask: Option[TimerTask] = None
  private var task: () => Unit = _

  def setPeriod (newPeriod: Long): Unit = {
    period = newPeriod
    start(task)
  }

  def start(newTask: () => Unit): Unit = {
    task = newTask
    stop()
    timer = Some(new Timer())
    timerTask = Some(new TimerTask {def run() { task() }})
    for(timer <- timer; timerTask <- timerTask)
      timer.scheduleAtFixedRate(timerTask, delay, period)
  }

  def stop(): Unit = {
    for(timerTask <- timerTask)
      timerTask.cancel()
    for(timer <- timer)
      timer.cancel()
    timerTask = None
    timer = None
  }
}



