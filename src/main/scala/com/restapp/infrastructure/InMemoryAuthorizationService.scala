package com.restapp.infrastructure

import com.restapp.domain.AuthorizationService

import scala.concurrent.Future

class InMemoryAuthorizationService() extends AuthorizationService {
  override def isAuthorized(token: String): Future[Option[Boolean]] = {
    Future.successful(Some(true))
  }
}
