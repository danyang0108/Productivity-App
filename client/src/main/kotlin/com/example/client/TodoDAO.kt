package com.example.client

import com.example.client.model.Project
import com.example.client.model.Tag
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.sql.Connection
import java.sql.SQLException

class TodoDAO {
    private val conn: Connection? = Connect.connect()

    fun addTodo(todo: TodoItem, projectID: Int) {
        try {
            if (conn != null) {
                val sql = "INSERT INTO todo_item (task_title, task_description,task_due, priority, status, project_id, task_tags, task_tagsDisplay)" +
                        "VALUES (?, ?, ?, ?, ?, ?,?,?)"
                val query = conn.prepareStatement(sql)
                query.setString(1, todo.task_title)
                query.setString(2, todo.task_description)
                query.setString(3, todo.task_dueDisplay)
                if (todo.task_priority == "") {
                    query.setString(4, "Low")
                } else {
                    query.setString(4, todo.task_priority)
                }
                query.setString(5, "Open")
                query.setInt(6, projectID)
                query.setString(7, Utils.TagsParser(todo.task_tags))
                query.setString(8, todo.task_tagsDisplay)
                query.execute()
//                conn.close()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }

    }

    fun addTodoItem(todo: TodoItem, projectID: Int) {
        try {
            if (conn != null) {
                val sql = "INSERT INTO todo_item (task_title, task_description,task_due, priority, status, project_id)" +
                        "VALUES (?, ?, ?, ?, ?, ?)"
                val query = conn.prepareStatement(sql)
                query.setString(1, todo.task_title)
                query.setString(2, todo.task_description)
                query.setString(3, todo.task_dueDisplay)
                if (todo.task_priority == "") {
                    query.setString(4, "Low")
                } else {
                    query.setString(4, todo.task_priority)
                }
                query.setString(5, "Open")
                query.setInt(6, projectID)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }

    }

    fun deleteTodo(todo: TodoItem) {
        try {
            if (conn != null) {
                val sql = "DELETE FROM todo_item WHERE task_title = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, todo.task_title)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun deleteUndoneTodoList(){
        try {
            if (conn != null) {
                val sql = "DELETE FROM todo_item WHERE status = 'Open'"
                val query = conn.prepareStatement(sql)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun getUndoneTodoProjectID(): ObservableList<Int> {
        var idList = FXCollections.observableArrayList<Int>()
        try {
            if (conn != null) {
                val sql = "SELECT project_id FROM todo_item WHERE status = 'Open'"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    idList.add(results.getInt("project_id"))
                }

            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return idList
    }
    fun getTodoID(title: String): Int {
        var id = 0
        try {
            if (conn != null) {
                val sql = "SELECT task_id FROM todo_item WHERE task_title = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, title)
                val results = query.executeQuery()
                id = results.getInt("task_id")

            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return id
    }

    fun updateTodoTitle(index: Int, title: String) {
        try {
            if (conn != null) {
                val sql = "UPDATE todo_item SET task_title = ? WHERE task_id = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, title)
                query.setInt(2, index)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun updateTodoDescription(title: String, desc: String) {
        try {
            if (conn != null) {
                val sql = "UPDATE todo_item SET task_description = ?" +
                        "WHERE task_title = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, desc)
                query.setString(2, title)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun updateTodoDue(title: String, due: String) {
        try {
            if (conn != null) {
                val sql = "UPDATE todo_item SET task_due = ?" +
                        "WHERE task_title = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, due)
                query.setString(2, title)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun updateTodoPriority(title: String, prio: String) {
        try {
            if (conn != null) {
                val sql = "UPDATE todo_item SET priority = ?" +
                        "WHERE task_title = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, prio)
                query.setString(2, title)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun updateTodoStatus(title: String, status: String) {
        try {
            if (conn != null) {
                val sql = "UPDATE todo_item SET status = '${status}'" +
                        "WHERE task_title = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, title)
              query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun updateTodoProject(title: String, project_key: Int) {
        try {
            if (conn != null) {
                val sql = "UPDATE todo_item SET project_id = ? " +
                        "WHERE task_title = ?"
                val query = conn.prepareStatement(sql)
                query.setInt(1, project_key)
                query.setString(2, title)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun getAllTodoList(projects: ObservableList<Project>): ObservableList<TodoItem> {
        val todos = FXCollections.observableArrayList<TodoItem>()
        try {
            if (conn != null) {
                val sql = "SELECT task_title, task_description, task_due, priority, status, p.project_id, project_name, task_tags, task_tagsDisplay FROM todo_item t, project p WHERE t.project_id = p.project_id"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    todos.add(
                        TodoItem(
                        results.getString("task_title"),
                    results.getString("task_description"),
                    results.getString("task_due"),
                    results.getString("priority"),
                    projects[results.getInt("project_id") - 1],
                    results.getString("project_name"),
                      results.getString("status"),
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
        return todos
    }

    fun getDoneTodoList(projects: ObservableList<Project>): ObservableList<TodoItem> {
        val done = FXCollections.observableArrayList<TodoItem>()
        try {
            if (conn != null) {
                val sql = "SELECT task_title, task_description, task_due, priority, status, p.project_id, project_name, task_tags, task_tagsDisplay FROM todo_item t, project p WHERE t.project_id = p.project_id AND status = 'Closed'"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    done.add(
                        TodoItem(
                        results.getString("task_title"),
                        results.getString("task_description"),
                        results.getString("task_due"),
                        results.getString("priority"),
                        projects[results.getInt("project_id") - 1],
                        results.getString("project_name"),
                      results.getString("status"),
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
        return done
    }

  fun getUnDoneTodoList(projects: ObservableList<Project>): ObservableList<TodoItem> {
    val undone = FXCollections.observableArrayList<TodoItem>()
    try {
      if (conn != null) {
        val sql = "SELECT task_title, task_description, task_due, priority, status, p.project_id, project_name, task_tags, task_tagsDisplay FROM todo_item t, project p WHERE t.project_id = p.project_id AND status = 'Open'"
        val query = conn.createStatement()
        val results = query.executeQuery(sql)
        while (results.next()) {
          val todo: TodoItem = TodoItem(
            results.getString("task_title"),
            results.getString("task_description"),
            results.getString("task_due"),
            results.getString("priority"),
            projects[results.getInt("project_id") - 1],
            results.getString("project_name"),
            results.getString("status"),
            task_tags = Utils.TagsParser(results.getString("task_tags")),
            task_tagsDisplay = results.getString("task_tagsDisplay")
          )
          undone.add(todo)
        }
      }
    }
    catch (ex: SQLException) {
      println(ex.message)
    }
    return undone
  }
    fun addProject(project: Project) {
        try {
            if (conn != null) {
                val sql = "INSERT INTO project (project_name)" +
                        "VALUES (?, ?)"
                val query = conn.prepareStatement(sql)
                query.setString(1, project.project_name)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun getProjectID(project_name: String): Int {
        var id = 0
        try {
            if (conn != null) {
                val sql = "SELECT project_id FROM project WHERE project_name = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, project_name)
                val results = query.executeQuery()
                id = results.getInt("project_id")

            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return id
    }

    fun getProjectID(index: Int): Int {
        var count = 0
        try {
            if (conn != null) {
                val sql = "SELECT * FROM todo_item WHERE status = 'Open'"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    if (count == index) {
                        return results.getInt("project_id")
                    }
                    count++
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return 1
    }

    fun deleteProject(project_name: String) {
        try {
            if (conn != null) {
                val sql = "DELETE FROM project WHERE project_name = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, project_name)
                query.execute()
                val reset = "UPDATE SQLITE_SEQUENCE SET SEQ=1 WHERE NAME = 'project'"
                val resetQuery = conn.prepareStatement(reset)
                resetQuery.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun getALlProject(): ObservableList<Project> {
        val projects = FXCollections.observableArrayList<Project>()
        try {
            if (conn != null) {
                val sql = "SELECT * FROM project"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    projects.add(
                        Project(results.getString("project_name"))
                    )
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return projects
    }

    fun getTodoProject(project_name: String, projects: ObservableList<Project>) : ObservableList<TodoItem> {
        val todoProject = FXCollections.observableArrayList<TodoItem>()
        try {
            if (conn != null) {
                val sql = "SELECT * FROM todo_item t, project p WHERE t.project_id = p.project_id AND project_name = ?"
                val query = conn.prepareStatement(sql)
                query.setString(1, project_name)
                val results = query.executeQuery()
                while (results.next()) {
                    todoProject.add(
                        TodoItem(
                        results.getString("task_title"),
                        results.getString("task_description"),
                        results.getString("task_due"),
                        results.getString("priority"),
                        projects[results.getInt("project_id") - 1],
                        results.getString("project_name"),
                        results.getString("status"),
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
        return todoProject
    }

  fun addTag(tag: Tag) {
    try {
      if (conn != null) {
        val sql = "INSERT INTO tag (tag_name)" +
          "VALUES (?)"
        val query = conn.prepareStatement(sql)
        query.setString(1, tag.tag_name)
        query.execute()
        conn.close()
      }
    }
    catch (ex: SQLException) {
      println(ex.message)
    }

  }

  fun deleteTag(tag_name: String) {
    try {
      if (conn != null) {
        val sql = "DELETE FROM tag WHERE tag_name = ?"
        val query = conn.prepareStatement(sql)
        query.setString(1, tag_name)
        query.execute()
      }
    }
    catch (ex: SQLException) {
      println(ex.message)
    }
  }

    fun getALlTags(): ObservableList<Tag> {
        val tags = FXCollections.observableArrayList<Tag>()
        try {
            if (conn != null) {
                val sql = "SELECT * FROM tag"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    tags.add(Tag(results.getString("tag_name"), results.getInt("tag_id")))
            }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return tags
    }

    fun getNumOfTodoItems(): Int {
        var todoItemCount = 0
        try {
            if (conn != null) {
                val sql = "SELECT * FROM todo_item"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    todoItemCount += 1
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return todoItemCount
    }

    fun getNumOfProjects(): Int {
        var projectCount = 0
        try {
            if (conn != null) {
                val sql = "SELECT * FROM project"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    projectCount += 1
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return projectCount
    }

    fun getNumOfTags(): Int {
        var tagCount = 0
        try {
            if (conn != null) {
                val sql = "SELECT * FROM tag"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    tagCount += 1
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return tagCount
    }
    fun getLastTodoItem(projects: ObservableList<Project>): TodoItem {
        val item = FXCollections.observableArrayList<TodoItem>()
        try {
            if (conn != null) {
                val sql = "SELECT task_title, task_description, task_due, priority, status, p.project_id, project_name,task_tags, task_tagsDisplay FROM todo_item t, project p WHERE t.project_id = p.project_id"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    item.add(
                        TodoItem(
                            results.getString("task_title"),
                            results.getString("task_description"),
                            results.getString("task_due"),
                            results.getString("priority"),
                            projects[results.getInt("project_id") - 1],
                            results.getString("project_name"),
                            results.getString("status"),
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
        return item[item.size -1]
    }

    fun getLastSessionWindowSize(): ObservableList<Int> {
        val windowSize = FXCollections.observableArrayList<Int>()
        try {
            if (conn != null) {
                val sql = "SELECT * FROM window"
                val query = conn.createStatement()
                val results = query.executeQuery(sql)
                while (results.next()) {
                    windowSize.add(results.getInt("width"))
                    windowSize.add(results.getInt("height"))
                    windowSize.add(results.getInt("x"))
                    windowSize.add(results.getInt("y"))
                }
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
        return windowSize
    }

    fun saveLastSessionWindowSize(x: Int, y: Int, width: Int, height: Int) {
        try {
            if (conn != null) {
                val sql = "UPDATE window SET x = ?, y = ?, width = ?, height = ?"
                val query = conn.prepareStatement(sql)
                query.setInt(1, x)
                query.setInt(2, y)
                query.setInt(3, width)
                query.setInt(4, height)
                query.execute()
            }
        }
        catch (ex: SQLException) {
            println(ex.message)
        }
    }
}
