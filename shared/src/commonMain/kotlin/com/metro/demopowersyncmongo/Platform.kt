package com.metro.demopowersyncmongo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform