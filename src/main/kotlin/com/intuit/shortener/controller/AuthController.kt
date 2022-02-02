package com.intuit.shortener.controller

import com.intuit.shortener.domain.LoginRequest
import com.intuit.shortener.domain.User
import com.intuit.shortener.service.AuthService
import com.intuit.shortener.service.UserService
import com.intuit.shortener.utils.JsonWebTokenUtil
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/")
@Slf4j
class AuthController {
    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @Autowired
    lateinit var jsonWebTokenUtil: JsonWebTokenUtil

    @Autowired
    lateinit var authService: AuthService

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody loginRequest: LoginRequest): ResponseEntity<String> {
        return when(authService.authenticate(loginRequest)){
            true -> ResponseEntity<String>(jsonWebTokenUtil.createToken(loginRequest.username), HttpStatus.OK)
            false -> ResponseEntity<String>("Invalid credentials", HttpStatus.UNAUTHORIZED)
        }
    }
}