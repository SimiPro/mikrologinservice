package com.belongo.services.login.model.oauth

import slick.driver.PostgresDriver.api._


/**
 * Created by Simon on 26.06.2015.
 */
class TokenTable(tag: Tag)
  extends Table[Token] (tag, "oauth_access_token") {
  def id = column[String]("token_id", O.PrimaryKey)
  def user_id = column[String]("user_id")
  def client_id = column[String]("client_id")
  def refresh_token_id = column[String]("refresh_token_id")
  def * = (id , user_id, user_id, client_id, refresh_token_id) <> (Token.tupled, Token.unapply)

  def user = foreignKey("USER_FK", user_id, Users.)


}

case class Token(
                  id:String,
                  user_id:String,
                  client_id:String,
                  refresh_token:String)

object TokenTable {
  val tokens = TableQuery[TokenTable]
}
