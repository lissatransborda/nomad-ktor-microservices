package com.microservices.schemas

import com.microservices.models.Post
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Posts: Table() {
    val id: Column<String> = char("id", 36)
    val userId: Column<String> = char("user_id", 36)
    val text: Column<String> = varchar("text", 400)
    override val primaryKey = PrimaryKey(id, name = "PK_Posts_Id")

    fun toPost(row: ResultRow) = Post(id = row[id], userId = row[userId], text = row[text])
}