package com.belongo.services.login.config

import java.security.Principal

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

/**
 * Created by Simon on 22.06.2015.
 */
@EnableResourceServer
@RestController
class AuthServerApplication {

  @RequestMapping(Array("/user"))
  def user(user:Principal):Principal = {
    println(user.toString)
    user
  }

}
