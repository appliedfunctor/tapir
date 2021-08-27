package com.ajsworton.tapir

import cats.Monad
import cats.effect.{ConcurrentEffect, ContextShift, Resource, Timer}
import com.ajsworton.tapir.routes.{TapirHandler, TapirRoutes}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext

object Server {

  def resource[F[_]: Monad: ConcurrentEffect](implicit ec: ExecutionContext, cs: ContextShift[F], timer: Timer[F]): Resource[F, Unit] = {
    val handler     = new TapirHandler[F](HelloWorld.impl)
    val tapirRoutes = new TapirRoutes[F](handler)
    val routes = Router(
      ""         -> tapirRoutes.tapirRoutes,
      "/redoc"   -> tapirRoutes.redocRoutes,
      "/swagger" -> tapirRoutes.swaggerRoutes
    )
    val httpApp = routes.orNotFound

    val HttpAppWithLogging = Logger.httpApp(true, true)(httpApp)

    for {
      _ <- BlazeServerBuilder[F](ec)
        .bindHttp(7788, "0.0.0.0")
        .withHttpApp(HttpAppWithLogging)
        .resource
    } yield ()
  }
}
