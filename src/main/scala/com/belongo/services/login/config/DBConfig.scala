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
    val dbUri = new URI(System.getenv("DATABASE_URL"))
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

    return basicDataSource
  }

}
