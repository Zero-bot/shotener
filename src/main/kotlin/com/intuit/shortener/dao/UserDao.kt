package com.intuit.shortener.dao

import com.intuit.shortener.domain.User

interface UserDao {
    fun add(user: User)
    fun get(username: String): User?
    fun usernameExists(username: String): Boolean
    fun emailExists(email: String): Boolean
}