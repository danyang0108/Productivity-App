package com.example.client

import com.example.client.model.Project
import com.example.client.model.Tag
import javafx.collections.FXCollections.observableArrayList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.VBox
import org.controlsfx.control.PopOver


class TagsController {
  val service: TodoDAO = TodoDAO()
  var client: Client = Client()
  val tags = service.getALlTags()
  val projects = service.getALlProject()
  val todos = service.getUnDoneTodoList(projects)
  var filteredTodo =  observableArrayList<TodoItem>()
  val selectedTags = mutableSetOf<Tag>()
  var tagMap = mutableMapOf<String, Tag>()
  var projectMap = mutableMapOf<String, Project>()
  var projectBtnList = observableArrayList<Button>()
  var projectOption = observableArrayList<String>()
  @FXML
  var tagsCheckboxList= observableArrayList<CheckBox>()
  @FXML
  lateinit var createTagBtn: Button

  init {
    filteredTodo.addAll(todos)
    for (proj in projects) {
      projectBtnList.add(Button(proj.project_name))
      projectOption.add(proj.project_name)
      projectMap[proj.project_name] = proj
    }
    for (tag in tags) {
      val checkBox = CheckBox(tag.tag_name)
      checkBox.onAction = EventHandler<ActionEvent>{
        if(checkBox.isSelected){
          selectedTags.add(tag)
        }else{
          selectedTags.remove(tag)
        }
        filterTodo()
      }
      addContextMenu(tag, checkBox)
      tagsCheckboxList.add(checkBox)
      tagMap[tag.tag_name] = tag
    }
  }

  private fun addContextMenu(tag: Tag, checkBox: CheckBox){
    val cm = ContextMenu()
    cm.userData = checkBox
    val menuItem1 = MenuItem("delete")
    menuItem1.onAction = EventHandler<ActionEvent>{
      service.deleteTag(tag.tag_name)
      client.deleteTag(tag)
      val mi = it.target as MenuItem
      val contextMenu = mi.parentPopup as ContextMenu
      tagsCheckboxList.remove(contextMenu.userData)
    }
    cm.items.add(menuItem1)
    checkBox.contextMenu = cm
  }

  private fun filterTodo(){
    filteredTodo.clear()
    filteredTodo.clear()
    for(todo in todos){
      if(containsAllTags(todo)){
        filteredTodo.add(todo)
      }
    }
  }

  private fun containsAllTags(todo: TodoItem):Boolean{
    var res = true
    selectedTags.forEach { if(!todo.task_tags.contains(it.tag_key)){
      res = false
    }  }
    return res
  }


  @FXML
  fun showTagPopup(){
    val lblNewTag = Label("New Tag")
    val txtNewTag = TextField("Untitled Tag")
    val btnNewTag = Button("Create")
    btnNewTag.onAction = EventHandler<ActionEvent>{
      if(!tagMap.containsKey(txtNewTag.text)){
        val tag = Tag(txtNewTag.text)
        tagMap[tag.tag_name] = tag
        service.addTag(tag)
        client.postTag(tag)
        tags.add(tag)
        val checkBox = CheckBox(tag.tag_name)
        checkBox.onAction = EventHandler<ActionEvent>{
          if(checkBox.isSelected){
            selectedTags.add(tag)
          }else{
            selectedTags.remove(tag)
          }
          filterTodo()
        }
        addContextMenu(tag, checkBox)
        tagsCheckboxList.add(checkBox)
        tagMap[tag.tag_name] = tag
      }
    }
    val newTagBox = VBox(lblNewTag,txtNewTag,btnNewTag)
    newTagBox.setPrefSize(150.0,150.0)
    val tagspopOver = PopOver(newTagBox)
    tagspopOver.isAutoFix = false
    tagspopOver.arrowLocation = PopOver.ArrowLocation.RIGHT_TOP
    tagspopOver.show(createTagBtn)
  }

  @FXML
  private var createProjectBtn: Button = Button()
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

  fun switchToProject(event: ActionEvent){
      Utils.openNewTab("projectView.fxml", event)
  }

}
