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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun addDomain(@RequestBody addDomainRequest: AddDomainRequest): ResponseEntity<String> {

        val currentUser = jsonWebTokenUtil.getCurrentUser(httpServletRequest)
        log.info("Request for adding domain received from $currentUser: $addDomainRequest")
        if(isValidUrl(addDomainRequest.domain).not()){
            log.error("Not a valid domain: ${addDomainRequest.domain}")
            return ResponseEntity.status(HttpStatus.OK).body("Not a valid URI")
        }
        val domain = Domains(username = currentUser, domain = addDomainRequest.domain)
        urlService.addDomain(domain)
        return ResponseEntity.status(HttpStatus.OK).body("URL added successfully")
    }

    @GetMapping("/domain/{username}")
    fun getDomain(@PathVariable("username")username: String): List<Domains> {
        return urlService.getDomain(username)
    }

    @PostMapping("/shorten")
    fun shorten(@RequestBody urlShortenerRequest: UrlShortenerRequest): ResponseEntity<String>{
        val currentUser = jsonWebTokenUtil.getCurrentUser(httpServletRequest)
        log.info("Request for adding domain received from $currentUser: $urlShortenerRequest")
        val uri = URI(urlShortenerRequest.url)

        if(isValidUrl(urlShortenerRequest.url).not()) {
            return ResponseEntity.status(HttpStatus.OK).body("Not a valid URI")
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
            return ResponseEntity.status(HttpStatus.OK).body("http://localhost:8080/s/$hash")
        }
        log.error("Domain or subdomains of ${urlShortenerRequest.url} is not whitelisted")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Domain not whitelisted")
    }

    fun checkDomainMatches(first: String, next: String): Boolean {
        if(first == next) return true
        val firstHost = if(first.startsWith("www.")) first.substring(4) else first
        val secondHost = if(next.startsWith("www.")) next.substring(4) else next
        val firstSplited = firstHost.split('.').reversed()
        val secondSplited = secondHost.split('.').reversed()
        var matches = 0
        secondSplited.forEachIndexed { index: Int, domain: String ->
            if(firstSplited[index] == domain)
                matches+=1
            else
                return@forEachIndexed
        }
        return matches >=2

    }

    fun isValidUrl(url: String): Boolean {
        return try {
            val uri = URI(url)
            return (uri.scheme.lowercase() in listOf("https", "http")) && (uri.query.length + uri.path.length < 2000)
        }catch (ex: URISyntaxException){
            false
        }
    }
}