package com.intuit.shortener.mapper

import com.intuit.shortener.domain.AddDomainRequest
import com.intuit.shortener.domain.Domains
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface DomainMapper {
    fun add(addDomainRequest: AddDomainRequest)
    fun get(username: String): List<Domains>
}