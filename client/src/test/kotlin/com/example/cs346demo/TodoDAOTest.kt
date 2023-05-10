package com.example.cs346demo

import com.example.client.Connect
import com.example.client.TodoApplication
import com.example.client.TodoDAO
import com.example.client.TodoItem
import com.example.client.model.Project
import javafx.application.Application
import org.testng.annotations.BeforeTest
import java.sql.Connection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class todoDAOTest {
    private val conn: Connection? = Connect.connect()
    private val sampleTodoDAO: TodoDAO = TodoDAO()
    private val sampleProject: Project = Project( project_name = "Project 0")
    private val sampleTodoItem: TodoItem = TodoItem(task_title = "CS346 Demo", task_description = "Present Sprint",
            task_dueDisplay = "2022-10-28 9:00", task_priority = "High", task_project = Project( project_name = "Project 1"),
            task_projectDisplay = "Project 1", task_status = "", task_tags = mutableSetOf<Int>(1), task_tagsDisplay = "")

    @BeforeTest
    fun setup() {
        Application.launch(TodoApplication::class.java)
    }

    @Test
    fun addTodoItemToDB() {
        val expectedTodoItems = sampleTodoDAO.getNumOfTodoItems()
        assertEquals(expectedTodoItems, sampleTodoDAO.getNumOfTodoItems())
        sampleTodoDAO.addTodoItem(sampleTodoItem, projectID = 1)
        assertEquals(expectedTodoItems + 1, sampleTodoDAO.getNumOfTodoItems())
    }

    @Test
    fun updateTodoItemInDB() {
        sampleTodoDAO.addTodoItem(sampleTodoItem, projectID = 1)
        val lastTodoItem = sampleTodoDAO.getLastTodoItem(sampleTodoDAO.getALlProject())
        assertEquals("CS346 Demo", lastTodoItem.task_title)
        assertEquals("Present Sprint", lastTodoItem.task_description)
        assertEquals("2022-10-28 9:00", lastTodoItem.task_dueDisplay)
        assertEquals("High", lastTodoItem.task_priority)

        val sampleTodoItemID = sampleTodoDAO.getTodoID(lastTodoItem.task_title)
        sampleTodoDAO.updateTodoDescription(lastTodoItem.task_title, "Updated Description")
        sampleTodoDAO.updateTodoDue(lastTodoItem.task_title, "2023-10-28 9:00")
        sampleTodoDAO.updateTodoTitle(sampleTodoItemID, "Updated Title")

        val updatedLastTodoItem = sampleTodoDAO.getLastTodoItem(sampleTodoDAO.getALlProject())
        assertEquals("Updated Title", updatedLastTodoItem.task_title)
        assertEquals("Updated Description", updatedLastTodoItem.task_description)
        assertEquals("2023-10-28 9:00", updatedLastTodoItem.task_dueDisplay)
        sampleTodoDAO.deleteTodo(updatedLastTodoItem)
    }

    @Test
    fun deleteTodoItemToDB() {
        val expectedTodoItems = sampleTodoDAO.getNumOfTodoItems()
        assertEquals(expectedTodoItems, sampleTodoDAO.getNumOfTodoItems())
        sampleTodoDAO.deleteTodo(sampleTodoItem)
        assertEquals(expectedTodoItems - 1, sampleTodoDAO.getNumOfTodoItems())
    }

    @Test
    fun verifyGetAllTodoList() {
        val expectedTodoItems = sampleTodoDAO.getNumOfTodoItems()
        val todoList = sampleTodoDAO.getAllTodoList(sampleTodoDAO.getALlProject())
        assertEquals(expectedTodoItems, todoList.size)
    }

    /*@Test
    fun _addAndDeleteProjectToDB() {
        val expectedProjects = sampleTodoDAO.getNumOfProjects()
        assertEquals(expectedProjects, sampleTodoDAO.getNumOfProjects())

        sampleTodoDAO.addProject(sampleProject)
        assertEquals(expectedProjects + 1, sampleTodoDAO.getNumOfProjects())

        sampleTodoDAO.deleteProject(sampleProject.project_name)
        assertEquals(expectedProjects, sampleTodoDAO.getNumOfProjects())
    }*/

    @Test
    fun verifyGetAllProject() {
        val expectedProject = sampleTodoDAO.getNumOfProjects()
        val projectList = sampleTodoDAO.getALlProject()
        assertEquals(expectedProject, projectList.size)
    }

    @Test
    fun verifyGetAllTag() {
        val expectedTag = sampleTodoDAO.getNumOfTags()
        val tagList = sampleTodoDAO.getALlTags()
        assertEquals(expectedTag, tagList.size)
    }

    @Test
    fun verifyDBConnection() {
        assertNotEquals(null, this.conn)
    }
}