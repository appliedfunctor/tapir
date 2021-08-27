package com.ajsworton.tapir

import cats.implicits.catsSyntaxOptionId
import sttp.tapir.Schema

package object routes {
  implicit val throwableSchema: Schema[Throwable] = Schema.string[String].map[Throwable](new Throwable(_).some)(_.getMessage)
}
