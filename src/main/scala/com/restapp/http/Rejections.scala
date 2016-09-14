package com.restapp.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Directives, RejectionHandler}

import Directives._
import StatusCodes._

trait Rejections {
  val rejectionHandler = RejectionHandler.newBuilder()
    .handle { case AuthorizationFailedRejection =>
      complete(Unauthorized)
    }
    .result()
}
