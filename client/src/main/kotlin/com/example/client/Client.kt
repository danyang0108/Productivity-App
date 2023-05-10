package com.example.client

import org.json.JSONObject
import com.example.client.model.Project
import com.example.client.model.Tag
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.json.JSONArray
import org.json.JSONTokener
import tornadofx.observableListOf
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class Client {

    fun getDone(): ObservableList<todo> {
        var done = FXCollections.observableArrayList<todo>()
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/todos"))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val jsonArray = JSONTokener(response.body()).nextValue() as JSONArray
            for (i in 0 until jsonArray.length()) {
                val task_key = jsonArray.getJSONObject(i).getInt("task_key")
                val task_title = jsonArray.getJSONObject(i).getString("task_title")
                val task_description = jsonArray.getJSONObject(i).getString("task_description")
                val task_dueDisplay = jsonArray.getJSONObject(i).getString("task_dueDisplay")
                val task_priority = jsonArray.getJSONObject(i).getString("task_priority")
                val task_status = jsonArray.getJSONObject(i).getString("task_status")
                val project = jsonArray.getJSONObject(i).getJSONObject("project")
                val project_key = project.get("project_key")
                val project_name = project.get("project_name")
                val task_tags = jsonArray.getJSONObject(i).getJSONArray("task_tags")
                val task_tag: MutableSet<Int> = hashSetOf(1)
                val task_tagsDisplay = jsonArray.getJSONObject(i).getString("task_tagsDisplay")
                val item = todo(task_title, task_description, task_dueDisplay,task_priority, Project(project_name as String),project_name, task_status, task_key, task_tag, task_tagsDisplay)
                done.add(item)
            }

        return done
    }

    fun getTodoInProject(todo: TodoItem): String {
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:8080/project/${todo.task_key}"))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

    fun postTodo(todo: TodoItem): String {

        val string = "{'task_title': '${todo.task_title}', 'task_description': '${todo.task_description}', " +
                "'task_dueDisplay': '${todo.task_dueDisplay}', 'task_priority': '${todo.task_priority}', " +
                "'task_project': '${todo.task_project}','task_projectDisplay': '${todo.task_projectDisplay}', " +
                "'task_status': '${todo.task_status}', 'task_key': '${todo.task_key}', " +
                "'task_tags': '${todo.task_tags}', 'task_tagsDisplay': '${todo.task_tagsDisplay}'}"

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:8080/todo"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

    fun deleteTodo(todo: TodoItem): String {

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:8080/todo/${todo.task_key}"))
            .header("Content-Type", "application/json")
            .DELETE()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()

    }

    fun updateTodo(todo: TodoItem): String {

        val string = "{'task_title': '${todo.task_title}', 'task_description': '${todo.task_description}', " +
                "'task_dueDisplay': '${todo.task_dueDisplay}', 'task_priority': '${todo.task_priority}', " +
                "'task_project': '${todo.task_project}','task_projectDisplay': '${todo.task_projectDisplay}', " +
                "'task_status': '${todo.task_status}', 'task_key': '${todo.task_key}', " +
                "'task_tags': '${todo.task_tags}', 'task_tagsDisplay': '${todo.task_tagsDisplay}'}"

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:8080/todo/${todo.task_key}"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(string))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()

    }

    fun getProject(): ObservableList<Project> {
        var projectList = FXCollections.observableArrayList<Project>()
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/projects"))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val jsonArray = JSONTokener(response.body()).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val project_key = jsonArray.getJSONObject(i).getString("project_key")
            val project_name = jsonArray.getJSONObject(i).getString("project_name")
            val proj = Project(project_name as String)
            projectList.add(proj)
        }
        return projectList
    }

    fun postProject(project: Project): String {
        val string = "{'project_name': '${project.project_name}', " +
                "'project_key': '${project.project_key}'}"

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:8080/project"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

    fun deleteProject(project: String): String {

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:8080/project/${project}"))
            .header("Content-Type", "application/json")
            .DELETE()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

    fun postTag(tag: Tag): String {

        val string = "{'tag_name': '${tag.tag_name}', 'tag_key': '${tag.tag_key}'}"

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:8080/tag"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

    fun getTag(): ObservableList<Tag> {
        var tagList = FXCollections.observableArrayList<Tag>()
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/tags"))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val jsonArray = JSONTokener(response.body()).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val tag_key = jsonArray.getJSONObject(i).getString("tag_key")
            val tag_name = jsonArray.getJSONObject(i).getString("tag_name")
            val tag = Tag(tag_name as String)
            tagList.add(tag)
        }

        return tagList
    }

    fun deleteTag(tag: Tag): String {

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:8080/tag/${tag.tag_key}"))
            .header("Content-Type", "application/json")
            .DELETE()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }
}