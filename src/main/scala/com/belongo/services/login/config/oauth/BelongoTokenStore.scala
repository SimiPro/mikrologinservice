package com.belongo.services.login.config.oauth

import java.security.MessageDigest
import java.util
import java.util.Date

import com.belongo.services.login.model.oauth.{Refresh_Token, Token, TokenDao}
import com.belongo.services.login.services.{BelongoUserDetail, BelongoUser}
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
    token match {
      case Some(token) => {
        val defaultToken = new DefaultOAuth2AccessToken(token.id)
        defaultToken.setTokenType(token.tokenType)
        defaultToken.setExpiration(token.expiration)
        defaultToken.setRefreshToken(createDefaultRefreshOAuthToken(token.refresh_token))
        defaultToken
      }
      case None => null
    }
  }

  def createDefaultRefreshOAuthToken(refresh_token:Option[Refresh_Token]): OAuth2RefreshToken = {
    refresh_token match {
      case None => null
      case Some(token) => createDefaultRefreshOAuthToken(token.token)
    }
  }

  def createDefaultRefreshOAuthToken(refresh_token:String):OAuth2RefreshToken = {
    new DefaultOAuth2RefreshToken(refresh_token)
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

  override def removeAccessToken(token: OAuth2AccessToken): Unit = {
    tokenRepo.removeAccessToken(hashToken(token.getValue))
  }

  override def findTokensByClientIdAndUserName(clientId: String, userName: String): util.Collection[OAuth2AccessToken] = ???

  override def storeAccessToken(token: OAuth2AccessToken, authentication: OAuth2Authentication): Unit = {
    val key = authenticationKeyGenerator.extractKey(authentication)
    val expires_in = token.getExpiresIn
    val scope = token.getScope
    val token_type = token.getTokenType
    val id = hashToken(token.getValue)
    val user_id = authentication.getUserAuthentication.getPrincipal.asInstanceOf[BelongoUserDetail].getId()
    val client_id = authentication.getOAuth2Request.getClientId
    val toki = Token(id,
      user_id,
      key,
      client_id,
      hashToken(token.getRefreshToken.getValue),
      token_type,
      new java.sql.Timestamp(System.currentTimeMillis + (expires_in * 1000L)))

    tokenRepo.save(toki)
  }

  override def readRefreshToken(tokenValue: String): OAuth2RefreshToken = {
    val refresh_token = tokenRepo.findRefreshToken(hashToken(tokenValue))
    createDefaultRefreshOAuthToken(refresh_token)
  }

  override def readAuthenticationForRefreshToken(token: OAuth2RefreshToken): OAuth2Authentication = ???
}
