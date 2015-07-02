package com.belongo.services.login.model.oauth

import slick.driver.PostgresDriver.api._



/**
 * Created by Simon on 26.06.2015.
 */
class Refresh_TokenTable(tag:Tag) extends Table[Refresh_Token](tag, "oauth_refresh_token") {
  def token = column[String]("token_id", O.PrimaryKey)
  def auth_key = column[String]("auth_key")

  def * = (token, auth_key) <> (Refresh_Token.tupled, Refresh_Token.unapply)
}

object Refresh_TokenTable {
  val refresh_tokens = TableQuery[Refresh_TokenTable]
}

case class Refresh_Token(token:String, auth_key:String)