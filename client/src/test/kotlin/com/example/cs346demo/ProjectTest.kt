package com.example.cs346demo

import com.example.client.model.Project
import kotlin.test.Test
import kotlin.test.assertEquals
internal class projectTest {
    private val sampleProject: Project = Project(project_name = "Project 1")

    @Test
    fun verifyProjectConstruction() {
        val expectedName = "Project 1"
        assertEquals(expectedName, sampleProject.project_name)
    }
}
