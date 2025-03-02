
package com.example

import com.example.models.Student
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.metrics.micrometer.*
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.micrometer.prometheus.PrometheusConfig
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import com.example.RabbitMQ

import org.jetbrains.exposed.sql.*
import com.rabbitmq.client.Channel
import org.jetbrains.exposed.sql.transactions.transaction
import com.rabbitmq.client.ConnectionFactory
import kotlinx.serialization.Serializable

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    install(MicrometerMetrics) {
        registry = prometheusRegistry
    }

    DatabaseFactory.init()
    RabbitMQ.init()

    routing {
        get("/") {
            call.respondText("Ktor Backend is Running!")
        }

        get("/metrics") {
            call.respondText(prometheusRegistry.scrape(), ContentType.Text.Plain)
        }

        get("/send") {
            RabbitMQ.publishAttendance("Test attendance event")
            call.respondText("Message sent to RabbitMQ!")
        }

        // Added the missing `/students` API routes
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


