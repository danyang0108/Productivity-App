package com.example.client


import com.example.client.model.Project
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView

class ProjectController {
    @FXML
    var projectsCb: ComboBox<String> = ComboBox()
    @FXML
    var todoTv: TableView<TodoItem> = TableView()
    var service = TodoDAO()
    var client = Client()
    val projects = service.getALlProject()
    val projectName = FXCollections.observableArrayList<String>()
    var projectBtnList = FXCollections.observableArrayList<Button>()
    var projectOption = FXCollections.observableArrayList<String>()
    var projectMap = mutableMapOf<String, Project>()
    @FXML
    private var createProjectBtn: Button = Button()

    init {
        for (proj in projects) {
            projectName.add(proj.project_name)
        }
    }
    @FXML
    fun handleSearchTodo() {
        val selectProject: String = projectsCb.selectionModel.selectedItem
        todoTv.items = service.getTodoProject(selectProject, projects)
    }

    @FXML
    fun handleDeleteProject() {
        val selectProject: String = projectsCb.selectionModel.selectedItem
        service.deleteProject(selectProject)
        for (project in projects) {
            if (project.project_name == selectProject) {
                client.deleteProject(project.project_name)
            }
        }
        projectName.remove(selectProject)
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

  fun switchToDone(event: ActionEvent){
      Utils.openNewTab("doneView.fxml", event)
  }

  fun switchToTag(event: ActionEvent){
      Utils.openNewTab("tagView.fxml", event)
  }
}
