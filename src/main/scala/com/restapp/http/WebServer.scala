package com.restapp.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.restapp.http.routers.{HelloRouter, UsersRouter, ContentsRouter}
import com.restapp.infrastructure.utils.Config
import com.restapp.infrastructure._

import scala.io.StdIn

object WebServer extends Config {
  def main(args: Array[String]) {
    val dbMigration = new DatabaseMigration(databaseUrl, databaseUser, databasePassword)
    dbMigration.migrate()

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val helloRouter = new HelloRouter()
    val usersRouter = new UsersRouter(new FakeUserRepository())

    val slickDatabase = new SlickDatabase()
    val valueRepository = new SlickContentRepository(slickDatabase)
    val valuesRouter = new ContentsRouter(valueRepository, new InMemoryAuthorizationService())

    val application = new Application(helloRouter, usersRouter, valuesRouter)

    val bindingFuture = Http().bindAndHandle(application.routes, httpInterface, httpPort)

    println(s"Server online at http://${httpInterface}:${httpPort}/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
