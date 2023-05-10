package com.example.client

import com.example.client.model.Project
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button

class DoneController {
    var service = TodoDAO()
    val projects = service.getALlProject()
    val done = service.getDoneTodoList(projects)
    var projectBtnList = FXCollections.observableArrayList<Button>()
    var projectOption = FXCollections.observableArrayList<String>()
    var projectMap = mutableMapOf<String, Project>()
    @FXML
    private var createProjectBtn: Button = Button()

    init {
        for (proj in projects) {
            projectBtnList.add(Button(proj.project_name))
            projectOption.add(proj.project_name)
            projectMap[proj.project_name] = proj
        }
    }

    @FXML
    private fun showProjectPopup(){
        Utils.showProjectPopup(projectMap, projects, projectBtnList, projectOption, createProjectBtn)
    }

  fun switchToDashboard(event: ActionEvent){
      Utils.openNewTab("tableView.fxml", event)
  }

  fun switchToUpcoming(event: ActionEvent){
      Utils.openNewTab("upcomingView.fxml", event)
  }

  fun switchToTag(event: ActionEvent){
      Utils.openNewTab("tagView.fxml", event)
  }

  fun switchToProject(event: ActionEvent){
      Utils.openNewTab("projectView.fxml", event)
  }
}
