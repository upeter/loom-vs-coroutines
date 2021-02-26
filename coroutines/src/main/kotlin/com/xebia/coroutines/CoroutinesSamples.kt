package com.xebia.coroutines

import javafx.application.Application.launch
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import kotlinx.coroutines.slf4j.*
import kotlin.coroutines.suspendCoroutine
import kotlin.time.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

suspend fun example1BasicCoroutine() = coroutineScope {
    launch {
        delay(500)
        println("basic coroutine")
    }
}


@ExperimentalTime
suspend fun example2StructuredConcurrency() = coroutineScope {
    launch {
        println("other async task")
    }
    launch {
        withTimeout(5.seconds.toLongMilliseconds()) {
            val op1 = async { "task sub 1" }
            val op2 = async { "task sub 2" }
            println("${op1.await()} - ${op2.await()}")
        }
    }
}


suspend fun example3Queue() = coroutineScope {
    val channel = Channel<String>()
    launch {
        println(channel.receive())
    }
    println(channel.send("hello queue"))
}


suspend fun  example4Context() = coroutineScope {
    launch {
        MDC.put("request_id", "kotlin rocks")
        launch(MDCContext()) {
            logger.info("hallo")
            launch {
                logger.info("hallo sub 1")
                throw RuntimeException("error sub 1")
                launch {
                    logger.info("hallo sub sub")
                }
            }
            MDC.put("request_id", "kotlin rocks again")
            launch(MDCContext()) {
                logger.info("hallo sub 2")
            }
        }
    }
}

@ExperimentalTime
fun main(): Unit = runBlocking {
    example1BasicCoroutine()
    example2StructuredConcurrency()
    example3Queue()

}

val logger = LoggerFactory.getLogger("Coroutines")