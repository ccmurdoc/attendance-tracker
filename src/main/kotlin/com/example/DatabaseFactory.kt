package com.example

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Students : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val attended = bool("attended")
    override val primaryKey = PrimaryKey(id)
}

object DatabaseFactory {
    fun init() {
        val database = Database.connect("jdbc:sqlite:attendance.db", driver = "org.sqlite.JDBC")
        transaction(database) {
            SchemaUtils.create(Students)
        }
    }
}
