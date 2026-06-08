/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.caldera.todo.list.caldera.cli;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author calderaly
 */
public class TodoListCalderaCli {
    private final TaskRepository repository;
    private final Scanner scanner;

    public TodoListCalderaCli() {
        // Store tasks under user home: ~/.todo-list-cli/tasks.txt (or Windows
        // equivalent)
        String userHome = System.getProperty("user.home");
        Path storagePath = Path.of(userHome, ".todo-list-cli", "tasks.txt");
        this.repository = new TaskRepository(storagePath);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        TodoListCalderaCli app = new TodoListCalderaCli();
        app.run();
    }

    public void run() {
        loadTasks();

        System.out.println("==== To-Do List CLI ====");
        System.out.println("Tasks are stored in your home directory per user.");
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> listTasks();
                case 2 -> addTask();
                case 3 -> markTask(true);
                case 4 -> markTask(false);
                case 5 -> removeTask();
                case 0 -> {
                    running = false;
                    saveTasks();
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }

            System.out.println();
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println("-------------------------------------");
        System.out.println("1) List tasks");
        System.out.println("2) Add task");
        System.out.println("3) Mark task as completed");
        System.out.println("4) Mark task as not completed");
        System.out.println("5) Remove task");
        System.out.println("0) Exit");
        System.out.println("-------------------------------------");
    }

    private void listTasks() {
        List<Task> tasks = repository.findAll();
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet.");
            return;
        }

        System.out.println("Your tasks:");
        for (Task task : tasks) {
                        String status = task.isCompleted() ? "[x]" : "[ ]";
                        System.out.printf("%d. %s %s%n", task.getId(), status,
                            task.getTitle());
                        if (task.getDescription() != null
                            && !task.getDescription().isBlank()) {
                            System.out.println("    " + task.getDescription());
                        }
                    }
            }

            private void addTask() {
                System.out.println("=== Add Task ===");
                String title = readNonEmptyLine("Title: ");
                System.out.print("Description (optional): ");
                String desc = scanner.nextLine().trim();

                Task task = repository.addTask(title, desc);
                System.out.println("Task added with id " + task.getId());
            }

            private void markTask(boolean completed) {
                String action = completed ? "complete" : "not complete";
                System.out.println("=== Mark Task as " + action + " ===");
                int id = readInt("Enter task id: ");
                Task task = repository.findById(id);
                if (task == null) {
                    System.out.println("Task with id " + id + " not found.");
                    return;
                }
                repository.markCompleted(id, completed);
                System.out.println("Task " + id + " updated.");
            }

            private void removeTask() {
                System.out.println("=== Remove Task ===");
                int id = readInt("Enter task id: ");
                boolean removed = repository.removeTaskById(id);
                if (removed) {
                    System.out.println("Task " + id + " removed.");
                } else {
                    System.out.println("Task with id " + id + " not found.");
                }
            }

            private int readInt(String prompt) {
                while (true) {
                    System.out.print(prompt);
                    String line = scanner.nextLine().trim();
                    try {
                        return Integer.parseInt(line);
                    } catch (NumberFormatException e) {
                        System.out.println(
                            "Invalid integer. Please try again.");
                    }
                }
            }

            private String readNonEmptyLine(String prompt) {
                while (true) {
                    System.out.print(prompt);
                    String line = scanner.nextLine().trim();
                    if (!line.isBlank()) {
                        return line;
                    }
                    System.out.println(
                        "Value cannot be empty. Please try again.");
                }
            }

            private void loadTasks() {
                try {
                    repository.loadFromFile();
                } catch (IOException e) {
                    System.out.println("Warning: Could not load tasks file: "
                        + e.getMessage());
                }
            }

            private void saveTasks() {
                try {
                    repository.saveToFile();
                } catch (IOException e) {
                    System.out.println("Warning: Could not save tasks file: "
                        + e.getMessage());
                }
            }
        }