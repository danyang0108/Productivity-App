package com.example.server

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Connect {
    var conn: Connection? = null
    fun connect(): Connection? {
        if (conn == null) {
            try {
                val url = "jdbc:h2:file:C:/Users/danya/IdeaProjects/cs346_demo/server/data"
                val username = "todo"
                val pass = "todo"
                conn = DriverManager.getConnection(url, username, pass)
                println("Connection to SQLite has been established")
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return conn
    }


    fun close(conn: Connection?) {
        try {
            conn?.close()
            println("Connection closed.")
        } catch (ex: SQLException) {
            println(ex.message)
        }
    }
}