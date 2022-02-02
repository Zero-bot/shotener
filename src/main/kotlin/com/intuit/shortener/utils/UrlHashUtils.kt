package com.intuit.shortener.utils

class UrlHashUtils {
    companion object {
        fun idToShortUrl(n: Int): String {
            var index = n
            val allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray()
            val shortUrl = StringBuilder()
            while (index > 0){
                shortUrl.append(allowedChars[index % 62])
                index /= 62
            }
            return shortUrl.reverse().toString()
        }

        fun shortUrlToId(shortUrl: String): Int {
            var id = 0

            shortUrl.forEachIndexed{  _, item ->
                if('a'.code <= item.code &&  item.code <= 'z'.code){
                       id = id * 62 + item.code - 'a'.code
                }
                if('A'.code < item.code && item.code <= 'Z'.code){
                    id = id * 62 + item.code - 'A'.code + 26
                }
                if('0'.code < item.code && item.code <= '9'.code){
                    id = id * 62 + item.code - '0'.code + 52
                }
            }
            return id
        }
    }
}

fun main(){
    val url = UrlHashUtils.idToShortUrl(1).also { println(it)}

    val id = UrlHashUtils.shortUrlToId(url).also { println(it)}
}