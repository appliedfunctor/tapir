package com.ajsworton.tapir

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.ExecutionContext

object Main extends IOApp {

  implicit val ec: ExecutionContext = Execution.mainContext

  def run(args: List[String]): IO[ExitCode] =
    Server.resource[IO].use(_ => IO.never.as(ExitCode.Success))
}
