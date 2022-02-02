package com.intuit.shortener.service

import com.intuit.shortener.dao.UserDao
import com.intuit.shortener.dao.UserDaoImpl
import com.intuit.shortener.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    lateinit var userDao: UserDaoImpl

    fun add(user: User){

        val hashedPassword = BCryptPasswordEncoder().encode(user.password)
        user.password = hashedPassword
        userDao.add(user)
    }

    fun get(username: String){

    }

    fun usernameExists(username: String): Boolean{
        return userDao.usernameExists(username)
    }

    fun emailExists(email: String): Boolean{
        return userDao.emailExists(email)
    }

    fun emailOrUsernameTaken(user: User): Boolean{
        return (usernameExists(user.username) || emailExists(user.email))
    }
}