package com.example.client

import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


class UpcomingController {
  // scene switching
  var stage: Stage? = null
  var scene: Scene? = null
  var root: Parent? = null
  var service: TodoDAO = TodoDAO()

  private fun openNewTab(view: String, event: ActionEvent) {
    root = FXMLLoader.load(TodoApplication::class.java.getResource(view))
    stage = ((event.source as Node).scene.window as Stage?)
    val windowSize = service.getLastSessionWindowSize()
    scene = Scene(root, windowSize[0].toDouble(), windowSize[1].toDouble())
    stage!!.scene = scene
      Utils.registerKeyShortcuts(scene!!)
    stage!!.show()

  }

  fun switchToDashboard(event: ActionEvent){
    openNewTab("tableView.fxml", event)
  }

  fun switchToDone(event: ActionEvent){
    openNewTab("doneView.fxml", event)
  }

  fun switchToProject(event: ActionEvent){
    openNewTab("projectView.fxml", event)
  }

  fun switchToTag(event: ActionEvent){
    openNewTab("tagView.fxml", event)

  }

}
