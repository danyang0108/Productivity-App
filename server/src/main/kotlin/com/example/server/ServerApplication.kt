package com.example.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import javax.persistence.*

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)
}

@RestController
@RequestMapping("/todos")
class TodoResource(val service: TodoService) {
	@GetMapping
	fun incompleteList(): List<TodoItem> = service.findTodo()

	@GetMapping("/done")
	fun doneList(): List<TodoItem> = service.findDone()

	@PostMapping
	fun postTodo(@RequestBody todo: TodoItem) = service.postTodo(todo)

	@DeleteMapping("/{id}")
	fun deleteTodo(@PathVariable id: String) = service.deleteTodo(id)

	@PutMapping("/{id}")
	fun putTodo(@PathVariable id: String, @RequestBody todo: TodoItem) = service.putTodo(id, todo)
}

@RestController
@RequestMapping("/projects")
class ProjectResource(val service: ProjectService) {

	@GetMapping
	fun projectList(): List<Project> = service.findProject()

	@GetMapping("/{id}")
	fun getSelectedProject(@PathVariable id: String): MutableList<TodoItem> = service.selectedProject(id)

	@PostMapping
	fun postProject(@RequestBody proj: Project) {
		service.postProject(proj)
	}

	@DeleteMapping("/{id}")
	fun deleteProject(@PathVariable id: String) {
		service.deleteProject(id)
	}

}

@RestController
@RequestMapping("/tags")
class TagResource(val service: TagService) {
	@GetMapping
	fun tagList(): List<Tag> = service.findTag()

	@PostMapping
	fun postTag(@RequestBody tag: Tag) = service.postTag(tag)

	@DeleteMapping("/{id}")
	fun deleteTag(@PathVariable id: String) = service.deleteTag(id)
}

data class TodoItem(@Id @GeneratedValue(strategy= GenerationType.IDENTITY) var task_key: String, var task_title: String, val task_description: String, var task_dueDisplay: String, var task_priority: String, var task_status: String, var project: Project?, var task_tags: MutableSet<Int>, var task_tagsDisplay: String?)

@Entity
@Table(name = "PROJECT")
data class Project(@Id @GeneratedValue(strategy= GenerationType.IDENTITY) var project_key: String, var project_name: String)

@Entity
@Table(name = "TAG")
data class Tag(@Id @GeneratedValue(strategy= GenerationType.IDENTITY) var tag_key: String, var tag_name: String)

@Service
class TodoService {

	@Autowired
	val dao = DAO()

	fun findTodo(): MutableList<TodoItem> = dao.getTodo()

	fun findDone(): MutableList<TodoItem> = dao.getDone()

	fun postTodo(todo: TodoItem)  = dao.addTodo(todo)

	fun deleteTodo(id: String) = dao.deleteTodo(id)

	fun putTodo(id: String, todo: TodoItem) = dao.updateTodo(id, todo)
}

@Service
class ProjectService {

	@Autowired
	val dao = DAO()

	fun findProject(): MutableList<Project> = dao.getProject()

	fun selectedProject(id: String): MutableList<TodoItem> = dao.getSelectedProjectList(id)

	fun postProject(proj: Project) = dao.addProject(proj)

	fun deleteProject(id: String) = dao.deleteProject(id)

}

@Service
class TagService {

	@Autowired
	val dao = DAO()

	fun findTag(): MutableList<Tag> = dao.getTag()

	fun postTag(tag: Tag) {
		dao.addTag(tag)
	}

	fun deleteTag(id: String) = dao.deleteTag(id)
}
