package com.belongo.services.login.services

import javax.persistence.{GeneratedValue, Id, Entity}

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

import scala.beans.BeanProperty

/**
 * Created by simipro on 3/13/15.
 */
trait UserRepository extends JpaRepository[BelongoUser, String]{

  def findByEmail(email:String):BelongoUser

}

@Entity
class BelongoUser {


  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @BeanProperty
  var id:String = _

  @BeanProperty
  var email:String = _

  @BeanProperty
  var password:String = _

  @BeanProperty
  var token:String = _
}
