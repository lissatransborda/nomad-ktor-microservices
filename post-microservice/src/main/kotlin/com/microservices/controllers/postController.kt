package com.microservices.controllers
import com.microservices.models.Post
import com.microservices.schemas.Posts
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.postRoute() {
    route("/post") {
        findAllPosts()
        findPost()
        create()
        update()
        delete()
    }
}

fun Route.findAllPosts() {
    get {
        try {
            val Posts = transaction {
                Posts.selectAll().map { Posts.toPost(it) }
            }

            return@get call.respond(Posts)
        } catch (erro: Exception) {
            return@get call.respondText("Erro ao buscar postagens", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.findPost() {
    get("{id}") {
        try {
            val id = call.parameters["id"] ?: return@get call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val Post = transaction {
                Posts.select { Posts.id eq id }.firstOrNull()
            } ?: return@get call.respondText("Postagem não encontrada", status = HttpStatusCode.BadRequest)

            return@get call.respond(Posts.toPost(Post))
        } catch (erro: Exception) {
            return@get call.respondText("Erro ao buscar postagem", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.create() {
    post {
        try {
            val Post = call.receive<Post>()

            val insercao = transaction {
                Posts.insert {
                    it[id] = Post.id
                    it[userId] = Post.userId
                    it[text] = Post.text
                }
            }

            if (insercao.equals(0)) {
                return@post call.respondText("Erro ao criar postagem", status = HttpStatusCode.InternalServerError)
            }

            return@post call.respond(Post)
        } catch (erro: Exception) {
            return@post call.respondText("Erro ao criar postagem", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.update() {
    put("{id}") {
        try {
            val id = call.parameters["id"] ?: return@put call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val Post = call.receive<Post>()

            val edicao = transaction {
                Posts.update({ Posts.id eq id }) {
                    it[userId] = Post.userId
                    it[text] = Post.text
                }
            }

            if (edicao.equals(0)) {
                return@put call.respondText("Erro ao modificar postagem", status = HttpStatusCode.InternalServerError)
            }

            return@put call.respond(Post(id, Post.userId, Post.text))
        } catch (erro: Exception) {
            return@put call.respondText("Erro ao atualizar postagem", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.delete() {
    delete("{id}") {
        try {
            val id = call.parameters["id"] ?: return@delete call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val remocao = transaction {
                Posts.deleteWhere { Posts.id eq id }
            }

            if (remocao.equals(0)) {
                return@delete call.respondText("Postagem não encontrada", status = HttpStatusCode.InternalServerError)
            }

            return@delete call.respondText("Postagem apagada")
        } catch (erro: Exception) {
            return@delete call.respondText("Erro ao apagar postagem", status = HttpStatusCode.InternalServerError)
        }
    }
}
