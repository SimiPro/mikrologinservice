package com.belongo.services.login.services

import java.util

import scala.collection.JavaConversions._

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService}
import org.springframework.stereotype.Component
import com.belongo.services.login.AsyncConfig._
import scala.concurrent.Await

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
    val userFuture = repo.findByEmail(s)
    //TODO: Remove this
    val user =  Await.result(userFuture, timeOut)
    user.getOrElse(null) match {
      case u:BelongoUser => return new BelongoUserDetail(u)
      case null => return null
    }
  }

  def save(s:BelongoUser): Unit = {
    repo.save(s)
  }
}




