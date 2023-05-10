package com.example.client

import com.example.client.model.Project
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox

data class TodoItem(var task_title: String, var task_description: String, var task_dueDisplay: String, var task_priority: String, var task_project: Project?, var task_projectDisplay: String, var task_status: String, var task_key: Int = (0..100).shuffled().last(), var task_tags: MutableSet<Int>, var task_tagsDisplay: String?){
var task_select: CheckBox = CheckBox()
var list = FXCollections.observableArrayList("Low", "Medium", "High")
var priority_type: ComboBox<String> = ComboBox(list)

  init {
    task_select.onAction = EventHandler {
      val service = TodoDAO()
      val status: String

      if(task_select.isSelected){
        status = "Closed"
      }else{
        status = "Open"
      }
      service.updateTodoStatus(task_title, status)
    }

    if (task_priority == "3. Low") {
      priority_type.selectionModel.select(0)
    } else if (task_priority == "2. Medium") {
      priority_type.selectionModel.select(1)
    } else if (task_priority == "1. High") {
      priority_type.selectionModel.select(2)
    }

    priority_type.onAction = EventHandler {
      val service = TodoDAO()
      var priority = ""

      if (priority_type.value == "Low") {
        priority = "3. Low"
        task_priority = "3. Low"
      } else if (priority_type.value == "Medium") {
        priority = "2. Medium"
        task_priority = "2. Medium"
      } else if (priority_type.value == "High") {
        priority = "1. High"
        task_priority = "1. High"
      }
      service.updateTodoPriority(task_title, priority)
      Utils.switchToDashboard()
    }
  }
}