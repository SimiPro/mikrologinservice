package com.belongo.services.login.main

import com.belongo.services.login.{OAuthConfig}


import org.springframework.boot.SpringApplication


/**
 * Created by Simon on 19.02.2015.
 */

object Application {

  def main(args: Array[String]) {
    SpringApplication.run(classOf[OAuthConfig], args:_*)
  }

}