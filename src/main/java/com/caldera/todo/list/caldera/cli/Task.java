/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.caldera.todo.list.caldera.cli;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author calderaly
 */
public class Task {
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Task(int id, String title, String description, boolean completed,
        LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public Task(int id, String title, String description) {
        this(id, title, description, false, LocalDateTime.now());
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Serialize task as a simple pipe-separated line:
     * id|completed|createdAt|title|description
     * @return
     */
    public String toStorageString() {
        String safeTitle = title.replace("|", "/");
        String safeDesc =
            description == null ? "" : description.replace("|", "/");
        return id + "|" + (completed ? "1" : "0") + "|"
            + createdAt.format(FORMATTER) + "|" + safeTitle + "|" + safeDesc;
    }

    /**
     * Parse from storage line.
     * @param line
     * @return
     */
    public static Task fromStorageString(String line) {
        String[] parts = line.split("\\|", 5);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid task line: " + line);
        }
        int id = Integer.parseInt(parts[0]);
        boolean completed = "1".equals(parts[1]);
        LocalDateTime createdAt = LocalDateTime.parse(parts[2], FORMATTER);
        String title = parts[3];
        String description = parts.length >= 5 ? parts[4] : "";
        return new Task(id, title, description, completed, createdAt);
    }

    @Override
    public String toString() {
        return "Task{"
            + "id=" + id + ", title='" + title + '\''
            + ", completed=" + completed + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Task task))
            return false;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
