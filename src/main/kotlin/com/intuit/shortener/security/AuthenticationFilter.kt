package com.intuit.shortener.security

import com.intuit.shortener.utils.JsonWebTokenUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationFilter: Filter {

    @Autowired
    val jsonWebTokenUtil: JsonWebTokenUtil = JsonWebTokenUtil()
    val log: Logger = LoggerFactory.getLogger(AuthenticationFilter::class.java)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        log.info("Inside auth filter")
        request as HttpServletRequest
        response as HttpServletResponse
        if(isAuthenticationRequest(request) || isShortenerRequest(request))  {
            log.info("Authentication/Shortener request")
            chain?.doFilter(request, response)
            return
        }
        val authHeader: String? = request.getHeader("Authorization")
        if(authHeader == null || authHeader.isEmpty()){
            log.info("Authorization header is missing for ${request.requestURI}")

            response.sendError(401, "UnAuthorized request")
            chain?.doFilter(request, response)
            return
        }
        val jwtToken = authHeader.substring(7)
        val username = jsonWebTokenUtil.getEmail(jwtToken)
        log.info("Current user: $username")
        if(username!= null && jsonWebTokenUtil.isValidToken(jwtToken, username)){

            log.info("Current user: $username")
            chain?.doFilter(request, response)
            return
        }
        response.sendError(401, "Authorization token is invalid or expired")
//        chain?.doFilter(request, response)
    }

    fun isAuthenticationRequest(request: HttpServletRequest): Boolean {
        log.info(request.requestURI)
        return request.requestURI.equals("/api/authenticate")
    }

    fun isShortenerRequest(request: HttpServletRequest): Boolean{
        return request.requestURI.startsWith("/s/")
    }
}