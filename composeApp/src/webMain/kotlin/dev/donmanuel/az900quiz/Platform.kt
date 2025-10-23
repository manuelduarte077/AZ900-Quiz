package dev.donmanuel.az900quiz

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform