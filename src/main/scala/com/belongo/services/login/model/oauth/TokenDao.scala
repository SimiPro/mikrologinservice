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


  def save(toki: Token): Unit = db.run(tokens += toki)
  def save(toki: Refresh_Token):Unit = db.run(refresh_tokens += toki)
  def removeRefreshToken(token:String) = refresh_tokens.filter(_.token === token).delete
  def removeAccessToken(token:String) = db.run(tokens.filter(_.id === token).delete)

  def getAccessToken(authentication_id: String): Option[Token] = {
    val result = db.run(tokens.filter(_.auth_key === authentication_id).result.headOption)
    Await.result(result,timeOut)
  }

  def findByToken(s: String): Option[Token] = {
    val result = db.run(tokens.filter(_.id === s).result.headOption)
    Await.result(result, timeOut)
  }

  def findRefreshToken(hashedToken: String): Option[Refresh_Token] = {
    val result = db.run(refresh_tokens.filter(_.token === hashedToken).result.headOption)
    Await.result(result, timeOut)
  }

}
