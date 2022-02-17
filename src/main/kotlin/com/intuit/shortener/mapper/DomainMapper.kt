package com.intuit.shortener.mapper

import com.intuit.shortener.domain.Domains
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DomainMapper {
    fun add(domain: Domains)
    fun get(username: String): List<Domains>
}