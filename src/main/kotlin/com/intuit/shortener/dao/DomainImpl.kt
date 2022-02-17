package com.intuit.shortener.dao

import com.intuit.shortener.domain.Domains
import com.intuit.shortener.mapper.DomainMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DomainImpl: DomainDao {
    val log = LoggerFactory.getLogger(DomainImpl::class.java)

    @Autowired
    lateinit var domainMapper: DomainMapper

    override fun add(domain: Domains) {
        log.info("Adding domain $domain")
        domainMapper.add(domain)
    }

    override fun get(username: String): List<Domains> {
        return domainMapper.get(username)
    }
}