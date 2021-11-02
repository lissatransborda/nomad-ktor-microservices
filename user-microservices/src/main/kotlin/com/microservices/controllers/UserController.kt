package com.microservices.controllers
import com.microservices.models.User
import com.microservices.schemas.Users
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.userRoute() {
    route("/user") {
        findAllUsers()
        findUser()
        create()
        update()
        delete()
    }
}

fun Route.findAllUsers() {
    get {
        try {
            val Users = transaction {
                Users.selectAll().map { Users.toUser(it) }
            }

            return@get call.respond(Users)
        } catch (erro: Exception) {
            return@get call.respondText("Erro ao buscar usuários", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.findUser() {
    get("{id}") {
        try {
            val id = call.parameters["id"] ?: return@get call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val User = transaction {
                Users.select { Users.id eq id }.firstOrNull()
            } ?: return@get call.respondText("Usuário não encontrado", status = HttpStatusCode.BadRequest)

            return@get call.respond(Users.toUser(User))
        } catch (erro: Exception) {
            return@get call.respondText("Erro ao buscar usuário", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.create() {
    post {
        try {
            val User = call.receive<User>()

            val insercao = transaction {
                Users.insert {
                    it[id] = User.id
                    it[name] = User.name
                    it[password] = User.password
                }
            }

            if (insercao.equals(0)) {
                return@post call.respondText("Erro ao criar usuário", status = HttpStatusCode.InternalServerError)
            }

            return@post call.respond(User)
        } catch (erro: Exception) {
            return@post call.respondText("Erro ao criar usuário", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.update() {
    put("{id}") {
        try {
            val id = call.parameters["id"] ?: return@put call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val User = call.receive<User>()

            val edicao = transaction {
                Users.update({ Users.id eq id }) {
                    it[name] = User.name
                    it[password] = User.password
                }
            }

            if (edicao.equals(0)) {
                return@put call.respondText("Erro ao modificar usuário", status = HttpStatusCode.InternalServerError)
            }

            return@put call.respond(User(id, User.name, User.password))
        } catch (erro: Exception) {
            return@put call.respondText("Erro ao atualizar usuário", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.delete() {
    delete("{id}") {
        try {
            val id = call.parameters["id"] ?: return@delete call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val remocao = transaction {
                Users.deleteWhere { Users.id eq id }
            }

            if (remocao.equals(0)) {
                return@delete call.respondText("Usuário não encontrado", status = HttpStatusCode.InternalServerError)
            }

            return@delete call.respondText("Usuário apagado")
        } catch (erro: Exception) {
            return@delete call.respondText("Erro ao apagar usuário", status = HttpStatusCode.InternalServerError)
        }
    }
}
