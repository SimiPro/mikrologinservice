package com.belongo.services.login.config.db

/**
 * Created by Simon on 06.07.2015.
 */
object UUIDGenerator {

  def generateId():String = {
    java.util.UUID.randomUUID.toString
  }
}
