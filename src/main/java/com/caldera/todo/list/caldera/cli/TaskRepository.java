/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.caldera.todo.list.caldera.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author calderaly
 */
public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;
    private final Path storageFile;

    public TaskRepository(Path storageFile) {
        this.storageFile = storageFile;
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    public Task addTask(String title, String description) {
        Task task = new Task(nextId++, title, description);
        tasks.add(task);
        return task;
    }

    public boolean removeTaskById(int id) {
        return tasks.removeIf(t -> t.getId() == id);
    }

    public Task findById(int id) {
        return tasks.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public void markCompleted(int id, boolean completed) {
        Task task = findById(id);
        if (task != null) {
            task.setCompleted(completed);
        }
    }

    public void loadFromFile() throws IOException {
        tasks.clear();
        nextId = 1;

        if (!Files.exists(storageFile)) {
            return; // Nothing to load
        }

        try (BufferedReader reader =
                 Files.newBufferedReader(storageFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank())
                    continue;
                Task task = Task.fromStorageString(line);
                tasks.add(task);
                nextId = Math.max(nextId, task.getId() + 1);
            }
        }
    }

    public void saveToFile() throws IOException {
        if (tasks.isEmpty()) {
            // If there are no tasks, you can optionally delete the file.
            if (Files.exists(storageFile)) {
                Files.delete(storageFile);
            }
            return;
        }

        Files.createDirectories(storageFile.getParent());
        try (BufferedWriter writer =
                 Files.newBufferedWriter(storageFile, StandardCharsets.UTF_8)) {
            for (Task task : tasks) {
                writer.write(task.toStorageString());
                writer.newLine();
            }
        }
    }
}
