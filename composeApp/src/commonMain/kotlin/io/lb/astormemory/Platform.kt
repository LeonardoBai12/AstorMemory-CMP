package io.lb.astormemory

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform