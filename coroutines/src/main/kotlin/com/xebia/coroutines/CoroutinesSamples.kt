package com.xebia.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
    delay(1000)
    println("delayed")

}