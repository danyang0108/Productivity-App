package com.example.client

import com.example.client.model.Project
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox

data class todo(var task_title: String, var task_description: String, var task_dueDisplay: String, var task_priority: String, var task_project: Project?, var task_projectDisplay: String, var task_status: String, var task_key: Int = (0..100).shuffled().last(), var task_tags: MutableSet<Int>, var task_tagsDisplay: String?){

}