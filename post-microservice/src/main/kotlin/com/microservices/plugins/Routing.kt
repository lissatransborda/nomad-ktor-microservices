package com.microservices.plugins

import com.microservices.controllers.postRoute
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {
    routing {
        postRoute()
    }
}
