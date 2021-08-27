package com.ajsworton.tapir

import java.util.concurrent.{ExecutorService, Executors}

import scala.concurrent.ExecutionContext

object Execution {

  def mainService: ExecutorService =
    Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors * 2)

  def mainContext: ExecutionContext = ExecutionContext.fromExecutor(mainService)

}
