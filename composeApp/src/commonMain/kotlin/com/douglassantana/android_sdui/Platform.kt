package com.douglassantana.android_sdui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform