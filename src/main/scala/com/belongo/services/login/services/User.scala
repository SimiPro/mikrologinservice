package com.belongo.services.login.services


import slick.driver.PostgresDriver.api._

/**
 * Created by simipro on 01/07/15.
 */
class User(tag:Tag) extends Table[BelongoUser](tag, "BelongoUser") {
  def id = column[String]("id",  O.PrimaryKey)
  def email = column[String]("email")
  def password = column[String]("password")
  def * = (email, password,id.?) <> (BelongoUser.tupled, BelongoUser.unapply)
}

object User {
  val users = TableQuery[User]

  def findByEmail(email: String) = {
    users.filter(_.email === email)
  }
}