package com.reagroup.appliedscala

import cats.effect.{IO, IOApp}
import com.comcast.ip4s.port
import com.reagroup.appliedscala.config.Config
import org.http4s.ember.client.EmberClientBuilder

object Main extends IOApp.Simple {

  override def run: IO[Unit] = {
    for {
      _ <- IO(println("Starting server"))
      _ <- startServer()
    } yield ()
  }

  private def runServerWith(config: Config): IO[Unit] = {
    EmberClientBuilder.default[IO].build.use { httpClient =>
      val app = new AppRuntime(config, httpClient).routes
      AppServer(port"9200", app).use { _ =>
        // The server will keep running until the process is interrupted
        IO.never
      }
    }
  }

  private def startServer(): IO[Unit] = {
    for {
      config <- Config.fromEnvironment()
      _ <- runServerWith(config)
    } yield ()
  }

}
