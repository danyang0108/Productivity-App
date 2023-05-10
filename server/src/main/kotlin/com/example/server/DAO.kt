package com.example.server

import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.SQLException

@Component
class DAO {
    private val conn: Connection? = Connect.connect()

    fun addTodo(todo: TodoItem) {
        try {
            if (conn != null) {
                val sql = "INSERT INTO TODO (TASK_KEY, TASK_TITLE, TASK_DESCRIPTION, TASK_DUEDISPLAY, TASK_PRIORITY, " +
                        "TASK_STATUS, PROJECT_KEY, TASK_TAGS, TASK_TAGSDISPLAY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                val query = conn.prepareStatement(sql)
                query.setString(1, todo.task_key)
                query.setString(2, todo.task_title)
                query.setString(3, todo.task_description)
                query.setString(4, todo.task_dueDisplay)
                query.setString(5, todo.task_priority)
                query.setString(6, todo.task_status)
                query.setString(7, todo.project?.project_key)
                query.setString(8, Utils.TagsParser(todo.task_tags))
                query.setString(9, todo.task_tagsDisplay)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun getTodo() : MutableList<TodoItem> {
        val todos: MutableList<TodoItem> = mutableListOf()
        try {
            if (conn != null) {
                val sql = "SELECT TASK_KEY, TASK_TITLE, TASK_DESCRIPTION, TASK_DUEDISPLAY, TASK_PRIORITY, TASK_STATUS, t.PROJECT_KEY, PROJECT_NAME, TASK_TAGS, TASK_TAGSDISPLAY FROM TODO t, PROJECT p WHERE t.PROJECT_KEY = p.PROJECT_KEY AND TASK_STATUS = 'Open'"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    todos.add(
                        TodoItem(
                            results.getString("task_key"),
                            results.getString("task_title"),
                            results.getString("task_description"),
                            results.getString("task_duedisplay"),
                            results.getString("task_priority"),
                            results.getString("task_status"),
                            Project(results.getString("project_key"), results.getString("project_name")),
                            task_tags = Utils.TagsParser(results.getString("task_tags")),
                            task_tagsDisplay = results.getString("task_tagsdisplay")
                        ))
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }

        return todos
    }

    fun getDone() : MutableList<TodoItem> {
        val done: MutableList<TodoItem> = mutableListOf()
        try {
            if (conn != null) {
                val sql = "SELECT TASK_KEY, TASK_TITLE, TASK_DESCRIPTION, TASK_DUEDISPLAY, TASK_PRIORITY, TASK_STATUS, t.PROJECT_KEY, PROJECT_NAME, TASK_TAGS, TASK_TAGSDISPLAY FROM TODO t, PROJECT p WHERE t.project_key = p.project_key AND task_status = 'Closed'"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    done.add(
                        TodoItem(
                            results.getString("task_key"),
                            results.getString("task_title"),
                            results.getString("task_description"),
                            results.getString("task_dueDisplay"),
                            results.getString("task_priority"),
                            results.getString("task_status"),
                            Project(results.getString("project_key"), results.getString("project_name")),
                            task_tags = Utils.TagsParser(results.getString("task_tags")),
                            task_tagsDisplay = results.getString("task_tagsDisplay")
                        ))
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return done
    }

    fun deleteTodo(id: String) {
        try {
            if (conn != null) {
                val sql = "DELETE FROM TODO WHERE TASK_KEY = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, id)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun updateTodo(id: String, todo: TodoItem) {
        try {
            if (conn != null) {
                val sql = "UPDATE TODO SET TASK_TITLE = ?, TASK_DESCRIPTION = ?, TASK_DUEDISPLAY = ?, " +
                        "TASK_PRIORITY = ?, TASK_STATUS = ?, PROJECT_KEY = ? WHERE TASK_KEY = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, todo.task_title)
                query.setString(2, todo.task_description)
                query.setString(3, todo.task_dueDisplay)
                query.setString(4, todo.task_priority)
                query.setString(5, todo.task_status)
                query.setString(6, todo.project?.project_key)
                query.setString(7, id)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun addProject(proj: Project) {
        try {
            if (conn != null) {
                val sql = "INSERT INTO PROJECT (project_key, project_name) VALUES (?, ?)"
                val query = conn.prepareStatement(sql)
                query.setString(1, proj.project_key)
                query.setString(2, proj.project_name)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun getProject(): MutableList<Project> {
        val projects: MutableList<Project> = mutableListOf()
        try {
            if (conn != null) {
                val sql = "SELECT * FROM PROJECT"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    projects.add(Project(results.getString("project_key"),
                        results.getString("project_name")))
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return projects
    }

    fun getSelectedProjectList(id: String): MutableList<TodoItem> {
        val todoInProj: MutableList<TodoItem> = mutableListOf()
        try {
            if (conn != null) {
                val sql = "SELECT TASK_KEY, TASK_TITLE, TASK_DESCRIPTION, TASK_DUEDISPLAY, TASK_PRIORITY, TASK_STATUS, t.PROJECT_KEY, PROJECT_NAME, TASK_TAGS, TASK_TAGSDISPLAY FROM TODO t, PROJECT p WHERE t.project_key = p.project_key AND t.project_key = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, id)
                val results = query.executeQuery()
                while (results.next()) {
                    todoInProj.add(
                        TodoItem(
                            results.getString("task_key"),
                            results.getString("task_title"),
                            results.getString("task_description"),
                            results.getString("task_dueDisplay"),
                            results.getString("task_priority"),
                            results.getString("task_status"),
                            Project(results.getString("project_key"), results.getString("project_name")),
                            task_tags = Utils.TagsParser(results.getString("task_tags")),
                            task_tagsDisplay = results.getString("task_tagsDisplay")
                        )
                    )
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return todoInProj
    }

    fun deleteProject(id: String) {
        try {
            if (conn != null) {
                val sql = "DELETE FROM PROJECT WHERE project_key = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, id)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun addTag(tag: Tag) {
        try {
            if (conn != null) {
                val sql = "INSERT INTO TAG (tag_key, tag_name) VALUES (?, ?)"
                val query = conn.prepareStatement(sql)
                query.setString(1, tag.tag_key)
                query.setString(2, tag.tag_name)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun getTag() : MutableList<Tag> {
        val tags: MutableList<Tag> = mutableListOf()
        try {
            if (conn != null) {
                val sql = "SELECT * FROM TAG"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    tags.add(Tag(
                        results.getString("tag_key"),
                        results.getString("tag_name")))
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }

        return tags
    }

    fun deleteTag(id: String) {
        try {
            if (conn != null) {
                val sql = "DELETE FROM TAG WHERE tag_key = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, id)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }
}