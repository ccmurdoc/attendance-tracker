package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
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
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    DatabaseFactory.init()
    configureRouting()
    RabbitMQ.init()
}
