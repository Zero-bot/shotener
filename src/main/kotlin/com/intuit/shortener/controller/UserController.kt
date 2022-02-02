package com.intuit.shortener.controller

import com.intuit.shortener.domain.User
import com.intuit.shortener.service.UserService
import com.intuit.shortener.utils.JsonWebTokenUtil
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController {
    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)
    @Autowired
    lateinit var userService: UserService
    val jsonWebTokenUtil: JsonWebTokenUtil = JsonWebTokenUtil()

    @PostMapping("/add")
    fun add(@RequestBody user: User): ResponseEntity<String> {
        return when(userService.emailOrUsernameTaken(user)){
            false -> {
                userService.add(user)
                ResponseEntity<String>("User added successfully", HttpStatus.CREATED)
            }
            true -> {
                ResponseEntity<String>("username/email already taken", HttpStatus.CONFLICT)
            }
        }
    }
}