package com.microservices.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val password: String,
)