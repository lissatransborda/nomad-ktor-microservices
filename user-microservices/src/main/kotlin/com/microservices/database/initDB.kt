package com.microservices.database

import com.microservices.schemas.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDB() {
    val DB_SERVICE_URL = System.getenv("DB_SERVICE_URL").removePrefix("http://")

    Database.connect("jdbc:postgresql://$DB_SERVICE_URL/db", driver = "org.postgresql.Driver",
        user = "root", password = "root")
    transaction {
        SchemaUtils.create(Users)
    }
}
