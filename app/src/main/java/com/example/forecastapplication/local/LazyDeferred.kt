package com.example.forecastapplication.local

import kotlinx.coroutines.*

fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(context = Dispatchers.Main, start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}