package org.foodieboys.zentuhr

import java.util.{TimerTask, Timer}

class Scheduler(delay: Long, period: Long) {
  private var timer: Option[Timer] = None
  private var timerTask: Option[TimerTask] = None

  def start(task: () => Unit): Unit = {
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



