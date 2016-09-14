package com.restapp.domain

import scala.concurrent.Future

trait ContentRepository {
  def findByKey(key: String): Future[Option[Value]]
}
