package com.example.cs346demo

import com.example.client.Connect
import com.example.client.TodoApplication
import com.example.client.TodoController
import com.example.client.TodoDAO
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.control.Button
import java.sql.Connection
import java.time.LocalDateTime
import kotlin.test.*

internal class TodoControllerTest {

    private lateinit var todocontroller : TodoController
    private val conn: Connection? = Connect.connect()
    private val sampleTodoDAO: TodoDAO = TodoDAO()
    init {
    }

    @BeforeTest
    fun setup() {
        Application.launch(TodoApplication::class.java)
        todocontroller = TodoController()
    }

    @Test
    fun testConstruction() {
        assertEquals(todocontroller.undone.size + todocontroller.done.size, sampleTodoDAO.getNumOfTodoItems())
        assertEquals(todocontroller.projects.size, sampleTodoDAO.getNumOfProjects())
        assertEquals(todocontroller.tags.size, sampleTodoDAO.getNumOfTags())

        assertEquals(todocontroller.projectBtnList.size, sampleTodoDAO.getNumOfProjects())
        assertEquals(todocontroller.projectOption.size, sampleTodoDAO.getNumOfProjects())
        assertEquals(todocontroller.projectsDropDown.items.size, sampleTodoDAO.getNumOfProjects())
        assertEquals(todocontroller.projectMap.size, sampleTodoDAO.getNumOfProjects())

        for (proj in todocontroller.projects) {
            assertContains(todocontroller.projectOption, proj.project_name)
            assertContains(todocontroller.projectsDropDown.items, proj.project_name)
            assertEquals(todocontroller.projectMap[proj.project_name], proj)
        }
    }
}