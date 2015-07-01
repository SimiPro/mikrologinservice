package com.belongo.services.login

import scala.concurrent.ExecutionContext


import scala.concurrent.duration._
/**
 * Created by Simon on 01.07.2015.
 */
object AsyncConfig {

  implicit val ex = ExecutionContext.Implicits.global
  implicit val timeOut = 3 seconds


}
