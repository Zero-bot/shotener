package com.intuit.shortener.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@EnableWebSecurity
@Configuration
class SecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint? = null

    override fun configure(http: HttpSecurity?) {
        http?.csrf()?.disable()?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
}