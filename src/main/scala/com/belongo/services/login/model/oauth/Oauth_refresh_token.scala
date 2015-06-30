package com.belongo.services.login.model.oauth

import java.sql.Blob
import javax.persistence.{Id, Entity}

import scala.beans.BeanProperty

/**
 * Created by Simon on 26.06.2015.
 */
@Entity
class Oauth_refresh_token {
  @Id
  @BeanProperty
  var token_id:String = _
  @BeanProperty
  var token:Array[Byte] = _
  @BeanProperty
  var authentication:Array[Byte] = _
}
