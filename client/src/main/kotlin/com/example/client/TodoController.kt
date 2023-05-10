package com.example.client

import com.example.client.Utils.LocalDateToDisplay
import com.example.client.model.Project
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import org.controlsfx.control.CheckComboBox
import java.time.LocalDateTime
class TodoController {
  var dueDate: LocalDateTime?= null
  var projectBtnList = FXCollections.observableArrayList<Button>()
  var projectOption = FXCollections.observableArrayList<String>()
  var projectMap = mutableMapOf<String, Project>()
  var service = TodoDAO()
  var client = Client()
  @FXML
  var projectsDropDown: ComboBox<String> = ComboBox()
  var testDoneList = client.getDone()
  var testProjectList = client.getProject()
  var testTag = client.getTag()
  val projects = service.getALlProject()
  var undone = service.getUnDoneTodoList(projects)
  var done = service.getDoneTodoList(projects)
  val tags = service.getALlTags()

  @FXML
  var todoTv: TableView<TodoItem> = TableView<TodoItem>()

    init {
      println(testDoneList)
      println(testProjectList[0].project_name)
      println(testTag)
      for (proj in projects) {
        projectBtnList.add(Button(proj.project_name))
        projectOption.add(proj.project_name)
        projectsDropDown.getItems().add(proj.project_name)
        projectMap[proj.project_name] = proj
      }
    }

  @FXML
  var tagsDropDown: CheckComboBox<String> = CheckComboBox()
  fun initialize() {
    for (tag in tags) {
      tagsDropDown.items.addAll(tag.tag_name)
    }
    tagsDropDown.title = "Select Tags:"
  }
    @FXML
    private fun addTodo() {
      var projName: String? = projectsDropDown.selectionModel.selectedItem
      if(projectsDropDown.selectionModel.selectedItem == null){
        projName = "Untitled Project"
      }
      val proj = projectMap[projName]
      val projID = service.getProjectID(projName!!)
      val displayName: String = proj?.project_name ?: "Uncategorized"
      val selectedTags = mutableSetOf<Int>()
      var tagsDisplay = ""
      for(tag in tags){
        val checkedTags = tagsDropDown.checkModel.checkedItems
        if(checkedTags.contains(tag.tag_name)){
          selectedTags.add(tag.tag_key)
          tagsDisplay += "${tag.tag_name}, "
        }
      }
      if(tagsDisplay.length >=2){
        tagsDisplay = tagsDisplay.dropLast(2)
      }
      val todo = TodoItem(newTaskTitle.text.ifEmpty { "New Task" }, newTaskDescription.text, LocalDateToDisplay(dueDate), "Low", proj, displayName, "Open", task_tags = selectedTags, task_tagsDisplay = tagsDisplay)
      service.addTodo(todo, projID)
      client.postTodo(todo)
      EventStack.addToStack(UndoEventType.CREATE_TODO, todo, projID)
      undone.add(todo)
    }

    @FXML
    private fun moveTodoRowUp(){
      if(anyTaskSelected()) {
        val selectedRow = getSelectedTask()
        var index = 0
        for (todo in undone) {
          if (todo.task_key == selectedRow.task_key) {
            if (index - 1 >= 0) {
              undone.set(index, undone[index - 1])
              undone.set(index - 1, selectedRow)
            }
            break
          }
          index++
        }
        val projectIDs = service.getUndoneTodoProjectID()
        service.deleteUndoneTodoList()
        index = 0
        for (todo in undone) {
          service.addTodo(todo, projectIDs[index])
          client.postTodo(todo)
          index++
        }
      }
    }

    @FXML
    private fun moveTodoRowDown(){
      if(anyTaskSelected()){
        val selectedRow = getSelectedTask()
        var index = 0
        for (todo in undone){
          if (todo.task_key == selectedRow.task_key){
            if (index+1 < undone.size) {
              undone.set(index, undone[index + 1])
              undone.set(index + 1, selectedRow)
            }
            break
          }
          index++
        }
        val projectIDs = service.getUndoneTodoProjectID()
        service.deleteUndoneTodoList()
        index = 0
        for (todo in undone){
          service.addTodo(todo, projectIDs[index])
          client.postTodo(todo)
          index++
        }
      }
    }
    fun getSelectedTask(): TodoItem {
        return todoTv.selectionModel.selectedItem
    }

    fun anyTaskSelected(): Boolean{
      return !todoTv.selectionModel.isEmpty
    }

  @FXML
  fun pasteTask(){
    val task = TodoItem(getSelectedTask().task_title,getSelectedTask().task_description,getSelectedTask().task_dueDisplay, getSelectedTask().task_priority, getSelectedTask().task_project,getSelectedTask().task_projectDisplay,getSelectedTask().task_status,getSelectedTask().task_key,getSelectedTask().task_tags,getSelectedTask().task_tagsDisplay)
    var index = 0
    for (todo in undone) {
      if (todo.task_key == task.task_key) {
        break
      }
      index++
    }
    val projectID = service.getProjectID(index)
    service.addTodo(task,projectID)
    client.postTodo(task)
      EventStack.addToStack(UndoEventType.CREATE_TODO, task, projectID)
    undone.add(task)
  }

  @FXML
  fun editTitle(event: TableColumn.CellEditEvent<TodoItem, String>) {
    service.updateTodoTitle(service.getTodoID(getSelectedTask().task_title), event.newValue)
    getSelectedTask().task_title = event.newValue
    client.updateTodo(getSelectedTask())
  }

  @FXML
  fun editDescription(event: TableColumn.CellEditEvent<TodoItem, String>) {
    service.updateTodoDescription(getSelectedTask().task_title, event.newValue)
    getSelectedTask().task_description = event.newValue
    client.updateTodo(getSelectedTask())
  }

  @FXML
  fun editDue(event: TableColumn.CellEditEvent<TodoItem, String>) {
    service.updateTodoDue(getSelectedTask().task_title, event.newValue)
    getSelectedTask().task_dueDisplay = event.newValue
    client.updateTodo(getSelectedTask())
  }

  @FXML
  fun editPriority(event: TableColumn.CellEditEvent<TodoItem, String>) {
    service.updateTodoPriority(getSelectedTask().task_title, event.newValue)
    getSelectedTask().task_priority = event.newValue
    client.updateTodo(getSelectedTask())
  }

  @FXML
  fun editProject(event: TableColumn.CellEditEvent<TodoItem, String>) {
    val projectID = service.getProjectID(getSelectedTask().task_projectDisplay)
    service.updateTodoProject(getSelectedTask().task_title, projectID)
    getSelectedTask().task_projectDisplay = event.newValue
    client.updateTodo(getSelectedTask())
  }

    @FXML
    private fun deleteTodo() {
      val todo = getSelectedTask()
      val projName = todoTv.selectionModel.selectedItem.task_project?.project_name
      var projId:Int? = null
      if (projName != null) {
        projId = service.getProjectID(projName)
      }
      service.deleteTodo(todo)
      client.deleteTodo(todo)
        EventStack.addToStack(UndoEventType.DELETE_TODO, todo, projId = projId)
      undone.remove(todo)
    }

  @FXML
  private var dueDatePicker: DateTimePicker = DateTimePicker()

  @FXML
  private var newTaskTitle: TextField = TextField()
  @FXML
  private var newTaskDescription: TextField = TextField()

  fun getDate(){
    dueDate = dueDatePicker.dateTimeValue.value
  }

  // managing Projects
  @FXML
  private var createProjectBtn: Button = Button()

  @FXML
  private fun showProjectPopup(){
      Utils.showProjectPopup(projectMap, projects, projectBtnList, projectOption, createProjectBtn)
  }

  // scene switching
  fun switchToUpcoming(event: ActionEvent){
      Utils.openNewTab("upcomingView.fxml", event)
  }

  fun switchToDone(event: ActionEvent){
      Utils.openNewTab("doneView.fxml", event)
  }

  fun switchToProject(event: ActionEvent){
      Utils.openNewTab("projectView.fxml", event)
  }

  fun switchToTag(event: ActionEvent){
      Utils.openNewTab("tagView.fxml", event)
  }

  fun switchToDashboard(event: ActionEvent){
      Utils.openNewTab("tableView.fxml", event)
  }

}
