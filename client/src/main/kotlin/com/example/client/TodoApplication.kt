package com.example.client

import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.stage.WindowEvent
import java.sql.Connection

class TodoApplication : Application() {

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(TodoApplication::class.java.getResource("tableView.fxml"))
        val root: Parent = fxmlLoader.load()
        val controller: TodoController = fxmlLoader.getController()
        val conn: Connection? = Connect.connect()
        val service: TodoDAO = TodoDAO()
        val windowSize = service.getLastSessionWindowSize()
        var scene = Scene(root, windowSize[0].toDouble(), windowSize[1].toDouble())
        stage.x = windowSize[2].toDouble()
        stage.y = windowSize[3].toDouble()
        // handle keyboard shortcuts
        Utils.registerKeyShortcuts(scene)
        Utils.registerPasteShortcut(scene)

        stage.title = "Doable"
        stage.icons.add(Image(TodoApplication::class.java.getResourceAsStream("icon.png")))
        stage.scene = scene
        stage.show()
        stage.onCloseRequest = EventHandler<WindowEvent> {
            service.saveLastSessionWindowSize(stage.x.toInt(), stage.y.toInt(), scene.width.toInt(), scene.height.toInt())
        }
    }
}

fun main() {
    Application.launch(TodoApplication::class.java)
}
