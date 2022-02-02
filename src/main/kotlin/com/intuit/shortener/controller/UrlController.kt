package com.intuit.shortener.controller

import com.intuit.shortener.domain.AddDomainRequest
import com.intuit.shortener.domain.Domains
import com.intuit.shortener.domain.Url
import com.intuit.shortener.domain.UrlShortenerRequest
import com.intuit.shortener.service.UrlService
import com.intuit.shortener.utils.JsonWebTokenUtil
import com.intuit.shortener.utils.UrlHashUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.net.URISyntaxException
import javax.servlet.http.HttpServletRequest

@Component
@RestController
@RequestMapping("/api/url")
class UrlController {

    val log = LoggerFactory.getLogger(UrlController::class.java)

    @Autowired
    lateinit var urlService: UrlService

    @Autowired
    lateinit var httpServletRequest: HttpServletRequest

    @Autowired
    lateinit var jsonWebTokenUtil: JsonWebTokenUtil

    @PostMapping("/domain")
    fun addDomain(@RequestBody addDomainRequest: AddDomainRequest){
        urlService.addDomain(addDomainRequest)
    }

    @GetMapping("/domain/{username}")
    fun getDomain(@PathVariable("username")username: String): List<Domains> {
        return urlService.getDomain(username)
    }

    @PostMapping("/shorten")
    fun shorten(@RequestBody urlShortenerRequest: UrlShortenerRequest): String{
        val currentUser = jsonWebTokenUtil.getCurrentUser(httpServletRequest)
        log.info("currentUser: $currentUser")
        val uri: URI?
        try {
            uri = URI(urlShortenerRequest.url)
        }catch (ex: URISyntaxException){
            return "Not a valid URL"
        }

        val allowedDomains = urlService.getDomain(currentUser).filter {
            uri.host.equals(it) || checkDomainMatches(uri.host, it.domain)
        }

        if(allowedDomains.isNotEmpty()) {
            val urlObj = Url(id = -1, url = urlShortenerRequest.url, username = currentUser, null, null)
            log.info("Before: $urlObj")
            urlService.addUrl(urlObj)
            log.info("After: $urlObj")
            val hash = UrlHashUtils.idToShortUrl(urlObj.id)
            urlService.addHash(urlObj.id, hash)
            return "http://localhost:8080/s/$hash"
        }
        return "Domain not whitelisted"
    }

    fun checkDomainMatches(first: String, next: String): Boolean {
        if(first == next) return true
        val firstHost = if(first.startsWith("www.")) first.substring(4) else first
        val secondHost = if(next.startsWith("www.")) next.substring(4) else next
        val firstSplited = firstHost.split('.').reversed()
        val secondSplited = secondHost.split('.').reversed()
        var matches = 0
        secondSplited.forEachIndexed() { index: Int, domain: String ->
            if(firstSplited[index] == domain)
                matches+=1
            else
                return@forEachIndexed
        }
        return matches >=2

    }
}