package com.belongo.services.login.config.oauth

import java.security.MessageDigest
import java.util

import com.belongo.services.login.model.oauth.{Token, TokenDao}
import com.belongo.services.login.services.BelongoUser
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.common.{OAuth2AccessToken, OAuth2RefreshToken}
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
    db.run(tokenRepo.removeRefreshToken(extractTokenKey(token)))
  }

  def hashToken(token:String):String = {
    token match {
      case null => return null
      case _ => {
        val digest = MessageDigest.getInstance("MD5")
        val bytes = digest.digest(token.getBytes("UTF-8"))
        return String.format("%032x", bytes.map(_.toChar))
      }
    }
  }

  override def readAuthentication(token: OAuth2AccessToken): OAuth2Authentication = ???

  override def readAuthentication(token: String): OAuth2Authentication = ???

  override def removeAccessTokenUsingRefreshToken(refreshToken: OAuth2RefreshToken): Unit = ???

  override def getAccessToken(authentication: OAuth2Authentication): OAuth2AccessToken = {
    val key = authenticationKeyGenerator.extractKey(authentication)
    val accessToken = tokenRepo.getAccessToken(key)
    if (accessToken != null) {
      removeAccessToken(accessToken)
    }
  }

  override def storeRefreshToken(refreshToken: OAuth2RefreshToken, authentication: OAuth2Authentication): Unit = ???

  override def findTokensByClientId(clientId: String): util.Collection[OAuth2AccessToken] = ???

  override def readAccessToken(tokenValue: String): OAuth2AccessToken = ???

  override def removeAccessToken(token: OAuth2AccessToken): Unit = ???

  override def findTokensByClientIdAndUserName(clientId: String, userName: String): util.Collection[OAuth2AccessToken] = ???

  override def storeAccessToken(token: OAuth2AccessToken, authentication: OAuth2Authentication): Unit = {
    val key = authenticationKeyGenerator.extractKey(authentication)
    var refreshToken = token.getRefreshToken.getValue
    val oauthToken = token.getValue
    val expires_in = token.getExpiresIn
    val scope = token.getScope

    val id = hashToken(token.getValue)
    val user = authentication.getCredentials.asInstanceOf[BelongoUser]
    val toki = Token(id, user.id

    tokenRepo.save(Token(), token.getValue, ))

  }

  override def readRefreshToken(tokenValue: String): OAuth2RefreshToken = ???

  override def readAuthenticationForRefreshToken(token: OAuth2RefreshToken): OAuth2Authentication = ???
}
