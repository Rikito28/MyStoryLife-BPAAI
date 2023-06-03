package com.riski.mystorylife.helper

open class Event<out T>(private val content: T) {
    @Suppress("VisibilityPrivate")
    var handled = false
    private set

    fun getIfNotHandled(): T? {
        return if (handled) {
            null
        } else {
            handled = true
            content
        }
    }
}