package com.belongo.services.login.services

import java.util

import collection.JavaConversions._
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

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
  def getId():String = user.id

  override def getAuthorities: util.Collection[_ <: GrantedAuthority] = List(new Authorithies)
}

class Authorithies extends GrantedAuthority {
  override def getAuthority: String = {
    "read_write"
  }
}