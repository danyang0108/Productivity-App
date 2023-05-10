package com.example.client

import com.example.client.model.Project
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.controlsfx.control.PopOver
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
  val service: TodoDAO = TodoDAO()
  // handle keyboard shortcuts
  val kc_Dashboard: KeyCodeCombination = KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN)
  val kc_Done: KeyCodeCombination = KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN)
  val kc_Tag: KeyCodeCombination = KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN)
  val kc_Project: KeyCodeCombination = KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN)
  val kc_Upcoming: KeyCodeCombination = KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN)
  val kc_undo: KeyCodeCombination = KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN)
  val kc_redo: KeyCodeCombination = KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN)
  val kc_pasteTask: KeyCodeCombination = KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN)
  val kc_moveTaskUp: KeyCodeCombination = KeyCodeCombination(KeyCode.W, KeyCombination.SHIFT_DOWN)
  val kc_moveTaskDown: KeyCodeCombination = KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN)
  lateinit var scene: Scene

  fun registerKeyShortcuts(scene: Scene){
    val doneBtn: Button = scene.lookup("#DoneViewBtn") as Button
    val dashboardBtn: Button = scene.lookup("#DashboardViewBtn") as Button
    val tagBtn: Button = scene.lookup("#TagsViewBtn") as Button
    val projBtn: Button = scene.lookup("#ProjectsViewBtn") as Button
    val upcomingBtn: Button = scene.lookup("#UpcomingViewBtn") as Button
    val moveUpBtn: Button? = scene.lookup("#moveUpBtn") as Button?
    val moveDownBtn: Button? = scene.lookup("#moveDownBtn") as Button?

    scene.accelerators[kc_Done] = Runnable { doneBtn.fire() }
    scene.accelerators[kc_Tag] = Runnable { tagBtn.fire() }
    scene.accelerators[kc_Project] = Runnable { projBtn.fire() }
    scene.accelerators[kc_Upcoming] = Runnable { upcomingBtn.fire() }
    scene.accelerators[kc_Dashboard] = Runnable { dashboardBtn.fire() }
    scene.accelerators[kc_undo] = Runnable { EventStack.popFromStack(); dashboardBtn.fire() }
    scene.accelerators[kc_redo] = Runnable { EventStack.popFromRedoStack(); dashboardBtn.fire()}
    scene.accelerators[kc_moveTaskUp] = Runnable { moveUpBtn?.fire() }
    scene.accelerators[kc_moveTaskDown] = Runnable { moveDownBtn?.fire() }

  }

  fun switchToDashboard(){
    val dashboardBtn: Button = scene?.lookup("#DashboardViewBtn") as Button
    dashboardBtn.fire()
  }

  fun registerPasteShortcut(scene: Scene){
    val pasteTaskBtn: Button = scene.lookup("#pasteButton") as Button
    scene.accelerators[kc_pasteTask] = Runnable { pasteTaskBtn.fire() }
  }
  fun LocalDateToDisplay(date: LocalDateTime?): String{
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    if(date != null){
      if(date.dayOfYear == LocalDateTime.now().dayOfYear){
        return "Today " + date.format(timeFormatter)
      }else if (date.dayOfYear - LocalDateTime.now().dayOfYear == 1){
        return "Tomorrow " + date.format(timeFormatter)
      }
//      val monthStr: String = date.month.getDisplayName(TextStyle.SHORT, Locale.US)
//      val dateStr: String = date.dayOfMonth.toString()
      return date.format(dateFormatter)
    }
    return "Someday"
  }

  fun TagsParser(tags: String?): MutableSet<Int>{
    if (tags != null) {
      val res = mutableSetOf<Int>()
      val lst = tags.split(",").map{ if(it.isNotEmpty()){it.toInt()}else{-1} }
      for(i in lst){
        if(i!=-1){
          res.add(i)
        }
      }
      return res
    }else{
      return mutableSetOf()
    }
  }

  fun TagsParser(tags: MutableSet<Int>): String{
    var res = ""
    for(i in tags){
      res+="${i},"
    }
    return res
  }

  fun TagsDisplayParser(tags: String?): MutableSet<String>{
    if (tags != null) {
      val res = mutableSetOf<String>()
      val lst = tags.split(", ").map{ if(it.isNotEmpty()){it}else{""} }
      for(i in lst){
        if(i!=""){
          res.add(i)
        }
      }
      return res
    }else{
      return mutableSetOf()
    }
  }

  fun TagsDisplayParser(tags: MutableSet<String>): String{
    var res = ""
    for(i in tags){
      res+="${i},"
    }
    return res
  }

  fun openNewTab(view: String, event: ActionEvent) {
    val root:Parent = FXMLLoader.load(TodoApplication::class.java.getResource(view))
    val stage = ((event.source as Node).scene.window as Stage?)
    var windowSize = service.getLastSessionWindowSize()
    while(windowSize.size==0){
      windowSize = service.getLastSessionWindowSize()
    }
    scene = Scene(root, windowSize[0].toDouble(), windowSize[1].toDouble())
    stage!!.scene = scene
    registerKeyShortcuts(scene)
    if (view.contains("tableView")){
      registerPasteShortcut(scene)
    }
    stage.show()
  }

  fun showProjectPopup(projectMap: MutableMap<String, Project>, projects: ObservableList<Project>, projectBtnList:ObservableList<Button>, projectOption:ObservableList<String>, createProjectBtn: Button){
    val lblNewProject = Label("New Project")
    val txtNewProject = TextField("Untitled Project")
    val btnNewProject = Button("Submit")
    btnNewProject.onAction = EventHandler<ActionEvent>{
      if(!projectMap.containsKey(txtNewProject.text)){
        val proj = Project(txtNewProject.text)
        projectMap[proj.project_name] = proj
        service.addProject(proj)
        projects.add(proj)
        projectBtnList.add(Button(proj.project_name))
        projectOption.add(proj.project_name)
      }
    }
    val newProjectBox = VBox(lblNewProject,txtNewProject,btnNewProject)
    newProjectBox.setPrefSize(200.0,150.0)
    val popOver = PopOver(newProjectBox)
    popOver.show(createProjectBtn)
  }
}
