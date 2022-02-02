package com.intuit.shortener.controller

import com.intuit.shortener.service.UrlService
import com.intuit.shortener.utils.JsonWebTokenUtil
import com.intuit.shortener.utils.UrlHashUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.servlet.http.HttpServletRequest

@RequestMapping
@RestController
class ShortenerController {
    val log: Logger = LoggerFactory.getLogger(UrlController::class.java)

    @Autowired
    lateinit var urlService: UrlService

    @Autowired
    lateinit var httpServletRequest: HttpServletRequest

    @Autowired
    lateinit var jsonWebTokenUtil: JsonWebTokenUtil

    @GetMapping("/s/{hash}")
    fun getUrlForHash(@PathVariable("hash") hash: String): ResponseEntity<Any>{
        val id = UrlHashUtils.shortUrlToId(hash)
        val redirectUrl = urlService.getUrl(id)
        val headers = HttpHeaders()
        headers.location = URI(redirectUrl)
        return ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY)
    }
}