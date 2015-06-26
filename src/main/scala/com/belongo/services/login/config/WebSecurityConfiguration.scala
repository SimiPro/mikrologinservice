package com.belongo.services.login.config

import javax.annotation.PostConstruct
import javax.servlet.FilterRegistration

import com.belongo.services.login.services.{BelongoUser, UserService}
import org.springframework.beans.factory.annotation.{Value, Autowired}
import org.springframework.boot.context.embedded.FilterRegistrationBean
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
    } else {
      am.userDetailsService(userService)
    }
  }

  @Bean
  override def authenticationManagerBean():AuthenticationManager = {
    super.authenticationManagerBean()
  }

  @PostConstruct
  def initDB(): Unit ={
    // Create admin user
    if (!debug) {
      if (userService.loadUserByUsername("simi") == null) {
        val user = new BelongoUser()
        user.setEmail("simi")
        user.setPassword("pro")
        userService.repo.save(user)
      }
    }
  }

  @Bean
  def corsFilterChain(@Autowired() cors:CORSFilter): Unit = {
    // set cors header before filter chain else we cant handle a failed response on client side
    val registration = new FilterRegistrationBean(cors)
    registration.setOrder(0)
    registration.setName("CORSFilterChain")
    registration
  }

}
