package com.belongo.services.login.services

import java.util

import scala.collection.JavaConversions._

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService}
import org.springframework.stereotype.Component

/**
 * Created by simipro on 3/13/15.
 */
@Component
class UserService extends UserDetailsService {
  val log = Logger.getLogger(classOf[UserService])

  @Autowired
  var repo:UserRepository = _

  override def loadUserByUsername(s: String): UserDetails = {
    log.info("loading user: " + s)
    val user = repo.findByEmail(s)
    if (user == null) {
      return null
    }
    new BelongoUserDetail(user)
  }
}




