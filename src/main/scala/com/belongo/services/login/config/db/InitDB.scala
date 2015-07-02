package com.belongo.services.login.config.db

import javax.annotation.PostConstruct


import com.belongo.services.login.services.BelongoUser
import com.belongo.services.login.AsyncConfig._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import slick.driver.PostgresDriver.api._




/**
 * Created by Simon on 01.07.2015.
 */
@Configuration
class InitDB {

  @Autowired
  var db:Database = _

  import com.belongo.services.login.model.oauth.TokenTable._
  import com.belongo.services.login.model.oauth.Refresh_TokenTable._
  import com.belongo.services.login.services.User._

  @PostConstruct
  def createDB(): Unit = {
    val setup = DBIO.seq(
      ( tokens.schema ++
        refresh_tokens.schema ++
        users.schema).create,

      users += BelongoUser(null, "simi", "pro")
    )

    val result =  db.run(setup)
    result onSuccess {
      case r => {
        println("DB CREATED: " + r)
      }
    }
    result onFailure {
      case f => println("DB FAILED!!! : " + f)
    }

  }

}
