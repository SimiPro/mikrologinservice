package com.belongo.services.login.model.oauth

import java.sql.Blob
import javax.persistence.{Id, Entity}

import scala.beans.BeanProperty

/**
 * Created by Simon on 26.06.2015.
 */
@Entity
class Oauth_access_token {
  @Id
  @BeanProperty
  var token_id:String = _
  @BeanProperty
  var token:Array[Byte] = _
  @BeanProperty
  var authentication_id:String = _
  @BeanProperty
  var user_name:String = _
  @BeanProperty
  var client_id:String = _
  @BeanProperty
  var authentication:Array[Byte] = _
  @BeanProperty
  var refresh_token:String = _

}
