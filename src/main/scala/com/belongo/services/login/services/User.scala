package com.belongo.services.login.services

import slick.driver.PostgresDriver.api._

/**
 * Created by simipro on 01/07/15.
 */
class User(tag:Tag) extends Table[BelongoUser](tag, "BelongoUser") {
  def user_id = column[String]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email")
  def password = column[String]("password")
  def * = (user_id.?, email, password) <> (BelongoUser.tupled, BelongoUser.unapply)
}

object User {
  val users = TableQuery[User]

  def findByEmail(email: String) = {
    users.filter(_.email === email)
  }
}