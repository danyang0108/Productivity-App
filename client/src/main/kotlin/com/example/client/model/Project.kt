package com.example.client.model

import kotlinx.serialization.Serializable

@Serializable
class Project (var project_name: String, var project_key: Int = (0..100).shuffled().last()
) {

}
