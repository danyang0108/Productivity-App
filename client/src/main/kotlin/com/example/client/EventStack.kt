package com.example.client

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TableView

enum class UndoEventType{
  CREATE_TODO,
  DELETE_TODO
}

data class StackParam(val todo: TodoItem, val type: UndoEventType, val projId: Int?)

object EventStack{
  var service = TodoDAO()
  var stack = ArrayDeque<StackParam>()
  var redo_stack = ArrayDeque<StackParam>()
  val projects = service.getALlProject()
  val todos = service.getAllTodoList(projects)
  val undone = service.getUnDoneTodoList(projects)
  val root: Parent = FXMLLoader.load(TodoApplication::class.java.getResource("tableView.fxml"))
  val scene = Scene(root)
  @FXML
  var tableBtn: Button = scene.lookup("#DoneViewBtn") as Button

  public fun addToStack(type: UndoEventType, a: TodoItem, projId: Int? ){
    stack.add(StackParam(a,type, projId))
  }

  public fun popFromStack(){
    if(stack.size > 0){
      val e: StackParam = stack.removeLast()
      if(e.type == UndoEventType.CREATE_TODO){
        println("do delete")
        service.deleteTodo(e.todo)
        undone.remove(e.todo)
        redo_stack.add(StackParam(e.todo, UndoEventType.DELETE_TODO, e.projId))
//        var ct = TodoController()
//        todoTv.items = undone
      }else if (e.type == UndoEventType.DELETE_TODO){
        if(e.projId != null) {
          println("do create")
          service.addTodo(e.todo, e.projId)
          undone.add(e.todo)
          redo_stack.add(StackParam(e.todo, UndoEventType.CREATE_TODO, e.projId))
        }
      }
    }else{
      println("nothing in stack")
    }
  }

  public fun popFromRedoStack(){
    if(redo_stack.size > 0){
      val e: StackParam = redo_stack.removeLast()
      if(e.type == UndoEventType.CREATE_TODO){
        println("do delete")
        service.deleteTodo(e.todo)
        undone.remove(e.todo)
//        var ct = TodoController()
//        todoTv.items = undone

      }else if (e.type == UndoEventType.DELETE_TODO){
        if(e.projId != null) {
          println("do create")
          service.addTodo(e.todo, e.projId)
          undone.add(e.todo)
//          var ct = TodoController()
//          todoTv.items = undone
        }
      }
    }else{
      println("nothing in redo_stack")
    }
  }
}
