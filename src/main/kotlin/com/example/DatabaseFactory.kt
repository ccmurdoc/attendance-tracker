package com.example

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object Students : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val attended = bool("attended")
    override val primaryKey = PrimaryKey(id)
}

object DatabaseFactory {
    fun init() {
        val databaseUrl = System.getenv("DATABASE_URL") ?:
        "postgres://u364mlq88bmd:p79335d4abcf939f7a9959cd2a4ef9edfd5045c513675b2634aea1825af57ed64@cd5gks8n4kb20g.cluster-czrs8kj4isg7.us-east-1.rds.amazonaws.com:5432/d2v52ltivj1hsi"

        val db = if (databaseUrl.startsWith("postgres://")) {
            //  Convert PostgreSQL URL to JDBC format
            val uri = URI(databaseUrl)
            val userInfo = uri.userInfo.split(":")
            val username = userInfo[0]
            val password = userInfo[1]
            val dbUrl = "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}?sslmode=require"

            Database.connect(dbUrl, driver = "org.postgresql.Driver", user = username, password = password)
        } else {
            // Use SQLite for local development (optional)
            Database.connect("jdbc:sqlite:attendance.db", driver = "org.sqlite.JDBC")
        }

        transaction(db) {
            SchemaUtils.create(Students)
        }
    }
}
