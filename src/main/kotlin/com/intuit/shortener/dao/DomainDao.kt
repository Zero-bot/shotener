package com.intuit.shortener.dao

import com.intuit.shortener.domain.AddDomainRequest
import com.intuit.shortener.domain.Domains

interface DomainDao {
    fun add( addDomainRequest: AddDomainRequest)
    fun get(username: String): List<Domains>
}