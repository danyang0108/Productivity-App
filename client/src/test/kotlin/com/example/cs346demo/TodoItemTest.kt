package com.example.cs346demo

import com.example.client.TodoApplication
import com.example.client.TodoItem
import com.example.client.model.Project
import javafx.application.Application
import org.testng.annotations.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
internal class todoItemTest {
    private val sampleTodoItem: TodoItem = TodoItem(task_title = "CS346 Demo", task_description = "Present Sprint",
            task_dueDisplay = "2022-10-28 9:00", task_priority = "High", task_project = Project( project_name = "Project 1"),
            task_projectDisplay = "Project 1", task_status = "", task_tags = mutableSetOf<Int>(1), task_tagsDisplay = "")

    init{
    }
    @BeforeTest
    fun setup() {
        Application.launch(TodoApplication::class.java)
    }
    @Test
    fun verifyTodoItemConstruction() {
        val expectedTitle = "CS346 Demo"
        val expectedDescription = "Present Sprint"
        val expectedDueDate = "2022-10-28 9:00"
        val expectedPriority = "High"
        val expectedProjectColor = "#27c250"
        val expectedProjectName = "Project 1"
        val expectedProjectDisplay = "Project 1"
        val expectedTags = mutableSetOf<Int>(1)
        val expectedTagsDisplay = ""
        assertEquals(expectedTitle, sampleTodoItem.task_title)
        assertEquals(expectedDescription, sampleTodoItem.task_description)
        assertEquals(expectedDueDate, sampleTodoItem.task_dueDisplay)
        assertEquals(expectedPriority, sampleTodoItem.task_priority)
        sampleTodoItem.task_project?.let { assertEquals(expectedProjectName, it.project_name) }
        assertEquals(expectedProjectDisplay, sampleTodoItem.task_projectDisplay)
        assertEquals(expectedTags, sampleTodoItem.task_tags)
        assertEquals(expectedTagsDisplay, sampleTodoItem.task_tagsDisplay)
    }
}