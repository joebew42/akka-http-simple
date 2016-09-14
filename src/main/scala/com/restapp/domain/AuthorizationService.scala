package com.restapp.domain

import scala.concurrent.Future

trait AuthorizationService {
  def isAuthorized(token: String): Future[Option[Boolean]]
}
