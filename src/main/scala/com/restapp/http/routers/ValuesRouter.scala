package com.restapp.http.routers

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Directive0}
import akka.http.scaladsl.server.Directives._
import com.restapp.domain.{AuthorizationService, ValueRepository}
import com.restapp.http.{Rejections, Serializers}

class ValuesRouter(repository: ValueRepository, authorizationService: AuthorizationService) extends Serializers with Rejections {
  def authorize: Directive0 = {
    headerValueByName("Token").flatMap { token =>
      onSuccess(authorizationService.isAuthorized(token)).flatMap {
        case Some(true) => pass
        case _ => reject(AuthorizationFailedRejection)
      }
    }
  }

  val routes =
    pathPrefix("values" / Remaining) { remainingPath =>
      get {
        val value = repository.findByKey(remainingPath)

        onSuccess(value) {
          case Some(value) => complete(value)
          case None => complete(StatusCodes.NotFound)
        }
      } ~
        post {
          handleRejections(rejectionHandler) {
            authorize {
              entity(as[String]) { body =>
                complete(StatusCodes.Created, body)
              }
            }
          }
        }
    }
}
