package com.ajsworton.tapir.routes

import io.circe.generic.semiauto.deriveCodec
import io.circe.{Codec, Decoder, Encoder}

sealed trait TapirError
object TapirError {
  final case class ExceptionError(error: Throwable) extends TapirError
  object ExceptionError {
    implicit val throwableCodec: Codec[Throwable] =
      Codec.from(
        Decoder.decodeString.map(new Throwable(_)),
        Encoder.encodeString.contramap(_.getMessage)
      )
    implicit val codec: Codec[ExceptionError] = deriveCodec
  }

  final case object NotFound      extends TapirError
  case object CreateConflictError extends TapirError

  final case class InternalError(message: String) extends TapirError
  object InternalError {
    implicit val codec: Codec[InternalError] = deriveCodec
  }

}
