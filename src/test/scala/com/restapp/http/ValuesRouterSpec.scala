package com.restapp.http

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import com.restapp.domain.{AuthorizationService, Content, ContentRepository}
import com.restapp.http.routers.ValuesRouter
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.Future

class ValuesRouterSpec extends RouterSpec with MockitoSugar {
  val repository = mock[ContentRepository]
  val authorizationService = mock[AuthorizationService]
  val router = new ValuesRouter(repository, authorizationService)

  val routes = router.routes

  "retrieves a value" in {
    val value = Content(Some("\"path/for/value\""), "a value")
    when(repository.findByKey("path/for/value")).thenReturn(Future(Some(value)))

    Get("/values/path/for/value") ~> routes ~> check {
      responseAs[String] shouldBe "a value"
    }
  }

  "returns 404 when value is not found" in {
    when(repository.findByKey("path/for/value")).thenReturn(Future(None))

    Get("/values/path/for/value") ~> routes ~> check {
      status shouldBe StatusCodes.NotFound
    }
  }
1
  "returns 401 when user is not authorized to create a value" in {
    when(authorizationService.isAuthorized("unauthorized_token")).thenReturn(Future(Some(false)))

    Post("/values/path/for/value") ~> addHeader("Token", "unauthorized_token") ~> routes ~> check {
      status shouldBe StatusCodes.Unauthorized
    }
  }

  "creates a new value" in {
    when(authorizationService.isAuthorized("authorized_token")).thenReturn(Future(Some(true)))
    val body = HttpEntity(ContentTypes.`text/plain(UTF-8)`, "a value")

    Post("/values/path/for/value", body) ~> addHeader("Token", "authorized_token") ~> routes ~> check {
      status shouldBe StatusCodes.Created
      responseAs[String] shouldBe "a value"
    }
  }
}
