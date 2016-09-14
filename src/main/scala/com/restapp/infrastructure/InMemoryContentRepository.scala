package com.restapp.infrastructure

import com.restapp.domain.{Value, ContentRepository}

import scala.collection.immutable.HashMap
import scala.concurrent.Future

class InMemoryContentRepository() extends ContentRepository {
  val values = HashMap(
    "im/exists" -> "[\"one\", \"two\", \"three\"]\n",
    "other/path" -> "a content with random values\nnew line\n"
  )

  override def findByKey(key: String): Future[Option[Value]] = {
    val value = Value(Some(key), values.getOrElse(key, ""))
    Future.successful(Some(value))
  }
}
