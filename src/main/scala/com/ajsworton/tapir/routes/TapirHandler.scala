package com.ajsworton.tapir.routes

import cats.Functor
import cats.implicits._
import com.ajsworton.tapir.HelloWorld
import com.ajsworton.tapir.HelloWorld.Greeting

class TapirHandler[F[_]: Functor](H: HelloWorld[F]) {
  def hello(name: String): F[Either[TapirError, Greeting]] = H.hello(HelloWorld.Name(name)).map(Right(_))
}
