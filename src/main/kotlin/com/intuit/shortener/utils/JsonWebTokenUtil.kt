package com.intuit.shortener.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.HashMap

@Component
@Configuration
@PropertySource(value = ["classpath:application.properties"])
class JsonWebTokenUtil{

    val log: Logger = LoggerFactory.getLogger(JsonWebTokenUtil::class.java)


    var secret:String = System.getenv("secret")


    companion object {
        const val VALIDITY: Long = 1 * 60 * 60 * 1000
        val signatureAlgorithm = SignatureAlgorithm.HS512


    }

    fun createToken(email: String): String {
        if(email.isEmpty()) throw Exception("Invalid user object")
        val claims = mapOf("email" to email)
        return doCreateToken(kotlin.collections.HashMap(), email) ?: throw Exception("Jwts builder returned empty token")

    }

    fun isValidToken(token: String, email: String): Boolean {
        return email == getEmail(token) && !isTokenExpired(token)
    }


    fun getEmail(token: String): String? {
        val claims = getClaims(token)?: return null
        return claims.subject
    }



    private fun isTokenExpired(token: String): Boolean {
        val expiry = getTokenExpiration(token)?: return true
        return expiry.before(Date(System.currentTimeMillis()))
    }

    private fun getClaims(token: String): Claims?{
        return try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
        }catch (ex: Exception){
            null
        }
    }

    private fun getTokenExpiration(token: String): Date? {
        val claims = getClaims(token) ?: return null
        return claims.expiration
    }

    private fun doCreateToken(claims: Map<String, Any>, email: String): String? {
        log.info("secret: $secret")
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(email)
            .setIssuer("shortener_service")
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + VALIDITY ))
            .signWith(signatureAlgorithm, secret)
            .compact()
    }

    fun getCurrentUser(request: HttpServletRequest): String {
        val authHeader: String? = request.getHeader("Authorization")
        if (authHeader == null || authHeader.isEmpty()) {
            throw IOException()
        }
        val jwtToken = authHeader.substring(7)
        return getEmail(jwtToken) ?: throw IOException()
    }
}