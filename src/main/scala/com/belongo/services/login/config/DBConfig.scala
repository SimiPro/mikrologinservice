package com.belongo.services.login.config

import java.net.URI

import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.context.annotation.{Bean, Configuration}

/**
 * Created by Simon on 26.06.2015.
 */
@Configuration
class DBConfig {


  @Bean
  def getDatasource(): BasicDataSource = {
    var basicDataSource:BasicDataSource = null
    val uri = System.getenv("DATABASE_URL")
    uri match {
      case null => basicDataSource = setUpInMemoryDb
      case _ =>  basicDataSource = setUpPostgres(uri)
    }
    return basicDataSource
  }


  def setUpInMemoryDb(): BasicDataSource = {
    val basicDataSource = new BasicDataSource
    basicDataSource.setDriverClassName("org.h2.Driver")
    basicDataSource.setUrl("jdbc:h2:mem:")
    basicDataSource.setUsername("sa")
    basicDataSource.setPassword("")
    basicDataSource
  }

  def setUpPostgres(uri:String): BasicDataSource ={
    val dbUri = new URI(uri)
    val username = dbUri.getUserInfo.split(":")(0)
    val password = dbUri.getUserInfo.split(":")(1)
    val dbUrl = "jdbc:postgresql://" +
      dbUri.getHost() + ":" +
      dbUri.getPort() + dbUri.getPath() +
      "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

    val basicDataSource = new BasicDataSource
    basicDataSource.setUrl(dbUrl)
    basicDataSource.setUsername(username)
    basicDataSource.setPassword(password)
    basicDataSource
  }
}
