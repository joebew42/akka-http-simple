package com.restapp.infrastructure

import com.restapp.domain.{Content, ContentRepository}

import scala.collection.immutable.HashMap
import scala.concurrent.Future

class InMemoryContentRepository() extends ContentRepository {
  val values = HashMap(
    "im/exists" -> "[\"one\", \"two\", \"three\"]\n",
    "other/path" -> "a content with random values\nnew line\n"
  )

  override def findByKey(key: String): Future[Option[Content]] = {
    val value = Content(Some(key), values.getOrElse(key, ""))
    Future.successful(Some(value))
  }
}
