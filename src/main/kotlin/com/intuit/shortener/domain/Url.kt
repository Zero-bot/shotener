package com.intuit.shortener.domain

import java.util.Date

data class Url(val id: Int = -1, val url: String, val username: String, val hash: String?, val last_accessed: Date?)
