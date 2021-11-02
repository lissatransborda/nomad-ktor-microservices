package com.microservices

import com.microservices.database.initDB
import io.ktor.application.*
import com.microservices.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    initDB()

    configureRouting()
    configureSerialization()
    configureHTTP()
}
