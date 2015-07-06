package com.belongo.services.login.services

import java.util

import com.belongo.services.login.config.db.UUIDGenerator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import scala.collection.JavaConversions._

/**
 * Created by Simon on 25.06.2015.
 */
class BelongoUserDetail(user: BelongoUser) extends UserDetails {
  override def isEnabled: Boolean = true
  override def getPassword: String = user.password
  override def isAccountNonExpired: Boolean = true
  override def isCredentialsNonExpired: Boolean = true
  override def isAccountNonLocked: Boolean = true
  override def getUsername: String = user.email
  def getId():String = user.id.get

  override def getAuthorities: util.Collection[_ <: GrantedAuthority] = List(new Authorithies)
}

class Authorithies extends GrantedAuthority {
  override def getAuthority: String = {
    "read_write"
  }
}

case class BelongoUser(email:String, password:String,id: Option[String] = Option(UUIDGenerator.generateId()))