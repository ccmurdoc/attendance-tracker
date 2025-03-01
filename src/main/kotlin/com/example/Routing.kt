package com.example

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class Student(val id: Int? = null, val name: String, val attended: Boolean)

fun Application.configureRouting() {
    routing {
        get("/students") {
            val students = transaction {
                Students.selectAll().map {
                    Student(it[Students.id], it[Students.name], it[Students.attended])
                }
            }
            call.respond(students)
        }

        post("/students") {
            val student = call.receive<Student>()
            transaction {
                Students.insert {
                    it[name] = student.name
                    it[attended] = student.attended
                }
            }
            call.respond(HttpStatusCode.Created, "Student added")
        }
    }
}
