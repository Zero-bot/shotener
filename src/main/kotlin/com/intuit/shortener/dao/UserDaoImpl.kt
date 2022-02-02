package com.intuit.shortener.dao

import com.intuit.shortener.domain.User
import com.intuit.shortener.mapper.UserMapper
import org.apache.ibatis.annotations.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserDaoImpl: UserDao {
    @Autowired
    lateinit var userMapper: UserMapper

    override fun add(user: User) {
        userMapper.add(user)
    }

    override fun get(username: String): User? {
        return userMapper.get(username)
    }

    override fun usernameExists(username: String): Boolean {
        return userMapper.usernameExists(username)
    }

    override fun emailExists(email: String): Boolean {
        return userMapper.emailExists(email)
    }
}