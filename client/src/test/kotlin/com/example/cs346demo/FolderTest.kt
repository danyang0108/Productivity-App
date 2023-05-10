package com.example.cs346demo

import com.example.client.model.Folder
import kotlin.test.Test
import kotlin.test.assertEquals
internal class folderTest {
    private val sampleFolder: Folder = Folder(folder_name = "Folder 1")

    @Test
    fun verifyFolderConstruction() {
        val expectedName = "Folder 1"
        assertEquals(expectedName, sampleFolder.folder_name)
    }
}