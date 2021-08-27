package com.ajsworton.tapir.routes

import cats.effect.{Concurrent, ContextShift, Timer}
import cats.implicits._
import com.ajsworton.tapir.HelloWorld
import com.ajsworton.tapir.routes.TapirError._
import org.http4s.HttpRoutes
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.openapi.Server
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.redoc.http4s.RedocHttp4s
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.http4s.SwaggerHttp4s

class TapirRoutes[F[_]](handler: TapirHandler[F])(implicit cs: ContextShift[F], concurrent: Concurrent[F], timer: Timer[F]) {

  def endpoints = List(
    helloWorldEndpointWithLogic
  )

  def tapirErrors: EndpointOutput.OneOf[TapirError, TapirError] =
    oneOf[TapirError](
      statusMapping(StatusCode.Conflict, emptyOutput.map(_ => CreateConflictError)(_ => ())),
      statusMapping(StatusCode.InternalServerError, jsonBody[ExceptionError]),
      statusMapping(StatusCode.InternalServerError, jsonBody[InternalError]),
      statusMapping(StatusCode.NotFound, emptyOutput.map(_ => NotFound)(_ => ()))
    )

  val baseEndpoint: Endpoint[Unit, TapirError, Unit, Any] = endpoint.errorOut(tapirErrors)

  val helloWorldEndpoint: Endpoint[String, TapirError, HelloWorld.Greeting, Any] =
    baseEndpoint.get
      .in("hello" / path[String]("name"))
      .out(jsonBody[HelloWorld.Greeting])

  val helloWorldEndpointWithLogic = helloWorldEndpoint.serverLogic(handler.hello)

  private val docs =
    OpenAPIDocsInterpreter
      .serverEndpointsToOpenAPI(
        endpoints,
        "Tapir Example",
        Option(getClass.getPackage.getImplementationVersion).getOrElse("1.0.0")
      )
      .servers(
        List(
          Server("https://qa.tapir.ajsworton.com", "QA Server".some),
          Server("https://stg.tapir.ajsworton.com", "STG Server".some),
          Server("https://prd.tapir.ajsworton.com", "PRD Server".some)
        )
      )


  def swaggerRoutes: HttpRoutes[F] = new SwaggerHttp4s(docs.toYaml).routes
  def redocRoutes: HttpRoutes[F]   = new RedocHttp4s(title = "Tapir Exemplar", yaml = docs.toYaml).routes
  def tapirRoutes: HttpRoutes[F]   = Http4sServerInterpreter.toRoutes(helloWorldEndpointWithLogic)

}
