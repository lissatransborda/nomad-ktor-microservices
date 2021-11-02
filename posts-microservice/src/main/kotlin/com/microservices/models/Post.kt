package com.microservices.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Post(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val text: String,
)