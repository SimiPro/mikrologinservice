package com.belongo.services.login.config.oauth

import java.security.MessageDigest
import java.util
import java.util.Date

import com.belongo.services.login.model.oauth.{Refresh_Token, Token, TokenDao}
import com.belongo.services.login.services.BelongoUser
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.common.{DefaultOAuth2RefreshToken, DefaultOAuth2AccessToken, OAuth2AccessToken, OAuth2RefreshToken}
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.{DefaultAuthenticationKeyGenerator, AuthenticationKeyGenerator, TokenStore}
import org.springframework.stereotype.Component
import slick.driver.PostgresDriver.api._



/**
 * Created by Simon on 01.07.2015.
 */

@Component
class BelongoTokenStore extends TokenStore {

  @Autowired
  var db:Database = _

  @Autowired
  var tokenRepo:TokenDao = _

  val authenticationKeyGenerator: AuthenticationKeyGenerator = new DefaultAuthenticationKeyGenerator

  override def removeRefreshToken(token: OAuth2RefreshToken): Unit = removeRefreshToken(token.getValue)

  def removeRefreshToken(token:String): Unit = {
    db.run(tokenRepo.removeRefreshToken(hashToken(token)))
  }

  def hashToken(token:String):String = {
    token match {
      case null => null
      case _ => {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(token.getBytes("UTF-8"))
        new String(bytes.map(_.toChar))
      }
    }
  }

  override def readAuthentication(token: OAuth2AccessToken): OAuth2Authentication = ???

  override def readAuthentication(token: String): OAuth2Authentication = ???

  override def removeAccessTokenUsingRefreshToken(refreshToken: OAuth2RefreshToken): Unit = ???

  override def getAccessToken(authentication: OAuth2Authentication): OAuth2AccessToken = {
    val key = authenticationKeyGenerator.extractKey(authentication)
    val token  = tokenRepo.getAccessToken(key)
    createDefaultOauthToken(token)
  }

  def createDefaultOauthToken(token:Option[Token]): OAuth2AccessToken = {
    token isNull () otherwise ()
    token match {
      case Some =>
      case None =>
    }
    val defaultToken = new DefaultOAuth2AccessToken(token.id)
    defaultToken.setTokenType(token.tokenType)
    defaultToken.setExpiration(token.expiration)
    defaultToken
  }

  def createDefaultRefreshOauthToken(refresh_token:Refresh_Token): OAuth2RefreshToken = {
    val default_refresh_token = new DefaultOAuth2RefreshToken(refresh_token.token)
    default_refresh_token
  }

  override def storeRefreshToken(refreshToken: OAuth2RefreshToken, authentication: OAuth2Authentication): Unit = {
    val refresh_token = Refresh_Token(hashToken(refreshToken.getValue), authenticationKeyGenerator.extractKey(authentication))
    tokenRepo.save(refresh_token)
  }

  override def findTokensByClientId(clientId: String): util.Collection[OAuth2AccessToken] = ???

  override def readAccessToken(tokenValue: String): OAuth2AccessToken =  {
    val token = tokenRepo.findByToken(hashToken(tokenValue))
    createDefaultOauthToken(token)
  }

  override def removeAccessToken(token: OAuth2AccessToken): Unit = ???

  override def findTokensByClientIdAndUserName(clientId: String, userName: String): util.Collection[OAuth2AccessToken] = ???

  override def storeAccessToken(token: OAuth2AccessToken, authentication: OAuth2Authentication): Unit = {
    val key = authenticationKeyGenerator.extractKey(authentication)
    val expires_in = token.getExpiresIn
    val scope = token.getScope
    val token_type = token.getTokenType
    val id = hashToken(token.getValue)
    val user_id = authentication.getUserAuthentication.asInstanceOf[BelongoUser].id
    val client_id = authentication.getOAuth2Request.getClientId
    val toki = Token(id,
      user_id.get,
      key,
      client_id,
      hashToken(token.getRefreshToken.getValue),
      token_type,
      new Date(expires_in))

    tokenRepo.save(toki)

  }

  override def readRefreshToken(tokenValue: String): OAuth2RefreshToken = {
    val refresh_token = tokenRepo.findRefreshToken(hashToken(tokenValue))
    create
  }

  override def readAuthenticationForRefreshToken(token: OAuth2RefreshToken): OAuth2Authentication = ???
}
