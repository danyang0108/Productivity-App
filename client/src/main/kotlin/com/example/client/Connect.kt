package com.example.client

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Connect {
var conn: Connection? = null
    fun connect(): Connection? {
      if(conn == null){
        try {
          val url = "jdbc:sqlite:todoList.sqlite"
          conn = DriverManager.getConnection(url)
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
