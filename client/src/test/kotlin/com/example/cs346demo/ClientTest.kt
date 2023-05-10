//package com.example.cs346demo
package com.example.client

import org.junit.jupiter.api.Test
import org.testng.annotations.BeforeTest
import com.example.client.TodoItem
import kotlin.test.assertEquals


internal class ClientTest {
    private val client: Client = Client()

    @BeforeTest
    fun setup() {
    }

    @Test
    fun getDoneList(){
        var doneList = client.getDone()
        var expectedSize = 1
        var doneItem = doneList[0]
        val task_title = "Task 1"
        val task_description = "Test 1"
        val task_dueDisplay = "2022-10-28 10:00"
        val task_priority = "1. High"
        val task_status = "Open"
        val project_key = "1"
        val project_name = "Project 1"
        assertEquals(expectedSize,doneList.size)
        assertEquals(task_description, doneItem.task_description)
        assertEquals(task_dueDisplay, doneItem.task_dueDisplay)
        assertEquals(task_priority, doneItem.task_priority)
        assertEquals(task_status, doneItem.task_status)
        doneItem.task_project?.let { assertEquals(project_name, it.project_name) }
    }

    @Test
    fun getProjectList(){
        var projList = client.getProject()
        var expectedSize = 2
        var proj = projList[0]
        val project_name = "Project 1"
        assertEquals(expectedSize,projList.size)
        assertEquals(project_name,proj.project_name)
    }

    @Test
    fun getTagList(){
        var tagList = client.getTag()
        var expectedSize = 2
        var tag = tagList[0]
        val tag_name = "Tag 1"
        assertEquals(expectedSize,tagList.size)
        assertEquals(tag_name, tag.tag_name)
    }
}