package com.ajsworton.tapir

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    TapirServer.stream[IO].compile.drain.as(ExitCode.Success)
}
