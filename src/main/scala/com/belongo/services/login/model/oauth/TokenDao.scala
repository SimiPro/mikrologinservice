package com.belongo.services.login.model.oauth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.stereotype.Component

import slick.driver.PostgresDriver.api._
import com.belongo.services.login.AsyncConfig._

import scala.concurrent.Await


/**
 * Created by Simon on 01.07.2015.
 */
@Component
class TokenDao {


  import Refresh_TokenTable._
  import TokenTable._

  @Autowired
  var db:Database = _



  def removeRefreshToken(token:String) = refresh_tokens.filter(_.id === token).delete
  def getAccessToken(authentication_id: String): Token = {
    val result = db.run(
      tokens.filter(_.authentication_id === authentication_id).result.headOption
    )
    val token = Await.result(result,timeOut)
    token.getOrElse(null) match {
      case null => return null
      case a:Token => return a
    }

  }


}
