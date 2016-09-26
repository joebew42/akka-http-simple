package com.restapp.http

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}

trait RouterSpec extends FlatSpec with Matchers with ScalatestRouteTest with Serializers with ScalaFutures
