package com.example.cs346demo

import com.example.client.model.Tag
import kotlin.test.Test
import kotlin.test.assertEquals
internal class tagTest {
    private val sampleTag: Tag = Tag(tag_key = 1, tag_name = "tag1")

    @Test
    fun verifyTagConstruction() {
        val expectedKey = 1
        val expectedName = "tag1"
        assertEquals(expectedKey, sampleTag.tag_key)
        assertEquals(expectedName, sampleTag.tag_name)
    }
}