package com.example

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Channel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object RabbitMQ {
    private const val QUEUE_NAME = "attendance_events"
    private lateinit var connection: Connection
    private lateinit var channel: Channel

    fun init() {
        val factory = ConnectionFactory()

        // Use the Heroku CloudAMQP URL
        val uri = System.getenv("CLOUDAMQP_URL") ?: "amqp://guest:guest@localhost"
        factory.setUri(uri)

        connection = factory.newConnection()
        channel = connection.createChannel()
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        println(" RabbitMQ initialized with queue: $QUEUE_NAME")
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