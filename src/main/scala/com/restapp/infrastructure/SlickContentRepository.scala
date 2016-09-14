package com.restapp.infrastructure

import com.restapp.domain.{Content, ContentRepository}

import scala.concurrent.Future

class SlickContentRepository(val slickDatabase: SlickDatabase) extends ContentRepository {

  import slickDatabase._
  import slickDatabase.driver.api._

  class Values(tag: Tag) extends Table[Content](tag, "values") {
    def key = column[Option[String]]("key", O.PrimaryKey)
    def value = column[String]("value")
    def * = (key, value) <> ((Content.apply _).tupled, Content.unapply)
  }

  protected val values = TableQuery[Values]

  override def findByKey(key: String): Future[Option[Content]] = {
    database.run(values.filter(_.key === key).result.headOption)
  }

}
