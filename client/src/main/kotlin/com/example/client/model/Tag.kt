package com.example.client.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(var tag_name: String, var tag_key: Int = (0..100).shuffled().last())