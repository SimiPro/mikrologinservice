package com.belongo.services.login.model.oauth

import java.util.Date


import slick.driver.PostgresDriver.api._


/**
 * Created by Simon on 26.06.2015.
 */
class TokenTable(tag: Tag)
  extends Table[Token] (tag, "oauth_access_token") {
  def id = column[String]("token_id", O.PrimaryKey)
  def user_id = column[String]("user_id")
  def auth_key = column[String]("auth_key")
  def client_id = column[String]("client_id")
  def refresh_token_id = column[String]("refresh_token_id")
  //TODO: IMPL SCOPE def scope = column[Set[String]]("scope")
  def * = (id , user_id, user_id, client_id, refresh_token_id) <> (Token.tupled, Token.unapply)

  def user = foreignKey("USER_FK", user_id, Users.users)


}

case class Token(
                  id:String,
                  user_id:String,
                auth_key:String,
                  client_id:String,
                  refresh_token:String,
                  tokenType: String,
                  expiration: Date
                  ) {

}

object TokenTable {
  val tokens = TableQuery[TokenTable]
}
