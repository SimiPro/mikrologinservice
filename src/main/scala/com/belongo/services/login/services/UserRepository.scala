package com.belongo.services.login.services


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import slick.driver.PostgresDriver.api._

import scala.concurrent.Future


/**
 * Created by simipro on 3/13/15.
 */
trait UserRepository {
  def save(s: BelongoUser): Unit


  def findByEmail(email:String):Future[Option[BelongoUser]]
}

@Component
class UserRepoImpl() extends UserRepository {
  @Autowired
  var db:Database = _

  override def findByEmail(email: String): Future[Option[BelongoUser]] = {
    val result = db.run(
      User.findByEmail(email).result.headOption
    )
    result
  }

  override def save(s: BelongoUser): Unit = {
    db.run(User.users += s)
  }
}

object User {
  val users = TableQuery[User]

  def findByEmail(email: String) = {
    users.filter(_.email === email)
  }
}

class User(tag:Tag) extends Table[BelongoUser](tag, "BelongoUser") {
  def user_id = column[String]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email")
  def password = column[String]("password")
  def * = (user_id.?, email, password) <> (BelongoUser.tupled, BelongoUser.unapply)
}

case class BelongoUser(id: Option[String], email:String, password:String)