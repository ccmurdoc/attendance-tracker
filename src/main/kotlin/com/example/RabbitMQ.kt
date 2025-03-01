package com.example

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object RabbitMQ {
    private const val QUEUE_NAME = "attendance_events"
    private lateinit var connection: Connection
    private lateinit var channel: Channel

    fun init() {
        val factory = ConnectionFactory().apply {
            host = "localhost"  // Change this if running on Heroku or cloud
        }
        connection = factory.newConnection()
        channel = connection.createChannel()
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        println("RabbitMQ initialized and queue declared.")
    }

    fun publishAttendance(message: String) {
        channel.basicPublish("", QUEUE_NAME, null, message.toByteArray(Charsets.UTF_8))
        println(" [x] Sent '$message' to RabbitMQ")
    }

    fun close() {
        channel.close()
        connection.close()
    }
}
