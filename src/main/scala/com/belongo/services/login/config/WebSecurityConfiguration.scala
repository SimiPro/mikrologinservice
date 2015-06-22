package com.belongo.services.login.config

import com.belongo.services.login.services.UserService
import org.springframework.beans.factory.annotation.{Value, Autowired}
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.{WebSecurityConfigurerAdapter, EnableWebSecurity}

/**
* Created by Simon on 30.03.2015.
*/
@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  var userService:UserService = _

  @Value("${DEBUG}")
  var debug:Boolean = _

  @Autowired
  def globalUserDetails(am: AuthenticationManagerBuilder): Unit ={
    // if debug you can log in with user simi pw pro
    if (debug) {
      am.inMemoryAuthentication()
        .withUser("simi").password("pro").roles("ADMIN")
        .and()
        .withUser("simipro").password("pro").roles("USER")
    }else {
      am.userDetailsService(userService)
    }
  }

  @Bean
  override def authenticationManagerBean():AuthenticationManager = {
    super.authenticationManagerBean()
  }

}
