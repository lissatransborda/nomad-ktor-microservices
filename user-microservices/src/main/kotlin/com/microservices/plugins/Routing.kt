package com.microservices.plugins

import com.microservices.controllers.userRoute
import io.ktor.routing.*
import io.ktor.application.*

fun Application.configureRouting() {

    routing {
        userRoute()
    }
}
