package com.intuit.shortener.mapper

import com.intuit.shortener.domain.User
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {

    @Insert("INSERT INTO public.\"user\"(username, email, password) VALUES (#{username}, #{email}, #{password});")
    fun add(user: User)


    @Select("SELECT username, email, password FROM public.\"user\" u WHERE u.username = #{username} limit 1;")
    fun get(@Param("username") username: String): User?

    @Select("Select exists (Select 1 from public.user where email=#{email})")
    fun emailExists(@Param("email") email: String): Boolean

    @Select("Select exists (Select 1 from public.user where username=#{username})")
    fun usernameExists(@Param("username") username: String): Boolean
}