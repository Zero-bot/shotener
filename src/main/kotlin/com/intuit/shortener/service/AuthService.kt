package com.intuit.shortener.service

import com.intuit.shortener.dao.UserDao
import com.intuit.shortener.dao.UserDaoImpl
import com.intuit.shortener.domain.JsonWebToken
import com.intuit.shortener.domain.LoginRequest
import com.intuit.shortener.utils.JsonWebTokenUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService {

    val logger: Logger = LoggerFactory.getLogger(AuthService::class.java)
    @Autowired
    lateinit var userDao: UserDaoImpl


    fun authenticate(loginRequest: LoginRequest): Boolean {
        logger.info(loginRequest.toString())
        val currentUser = userDao.get(loginRequest.username) ?: return false
        return BCryptPasswordEncoder().matches(loginRequest.password, currentUser.password)
    }
}