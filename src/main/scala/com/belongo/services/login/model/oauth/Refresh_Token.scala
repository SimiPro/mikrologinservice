package com.belongo.services.login.model.oauth

import slick.driver.PostgresDriver.api._



/**
 * Created by Simon on 26.06.2015.
 */
class Refresh_TokenTable(tag:Tag) extends Table[Refresh_Token](tag, "oauth_refresh_token") {
  def id = column[String]("token_id", O.PrimaryKey)
  def token = column[Array[Byte]]("token")
  def authentication = column[Array[Byte]]("authentication")
  def * = (id, token, authentication) <> (Refresh_Token.tupled, Refresh_Token.unapply)
}

object Refresh_TokenTable {
  val refresh_tokens = TableQuery[Refresh_TokenTable]
}

case class Refresh_Token(id:String, token:Array[Byte], authentication:Array[Byte])