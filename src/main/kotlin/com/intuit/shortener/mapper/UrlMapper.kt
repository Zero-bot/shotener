package com.intuit.shortener.mapper

import com.intuit.shortener.domain.Url
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Select

@Mapper
interface UrlMapper {
    @Options(useGeneratedKeys=true, keyProperty="id")
    @Insert("INSERT INTO public.\"url_shortener\"(url, username) VALUES (#{url}, #{username});")
    fun addUrl(url: Url)

    @Insert("UPDATE public.url_shortener SET hash=#{hash} WHERE id=#{id}")
    fun addHash(id: Int, hash: String)

    @Select("SELECT url from public.url_shortener WHERE id=#{id} limit 1")
    fun getUrl(id: Int): String

}