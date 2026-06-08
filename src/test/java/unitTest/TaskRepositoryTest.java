/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unitTest;

import com.caldera.todo.list.caldera.cli.Task;
import com.caldera.todo.list.caldera.cli.TaskRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 *
 * @author calderaly
 */
public class TaskRepositoryTest {

    // JUnit 5 menyediakan folder temp otomatis yang akan dihapus setelah test selesai
    @TempDir
    Path tempDir;

    @Test
    void testAddTaskIncrementsId() {
        Path tempFile = tempDir.resolve("tasks.txt");
        TaskRepository repo = new TaskRepository(tempFile);

        Task t1 = repo.addTask("Task 1", "Desc 1");
        Task t2 = repo.addTask("Task 2", "Desc 2");

        assertEquals(1, t1.getId());
        assertEquals(2, t2.getId());
        assertEquals(2, repo.findAll().size());
    }

    @Test
    void testFindByIdAndRemoveTask() {
        Path tempFile = tempDir.resolve("tasks.txt");
        TaskRepository repo = new TaskRepository(tempFile);

        Task task = repo.addTask("Hapus Aku", "Segera");
        
        assertNotNull(repo.findById(task.getId()));
        
        boolean removed = repo.removeTaskById(task.getId());
        assertTrue(removed);
        assertNull(repo.findById(task.getId()));
    }

    @Test
    void testMarkCompleted() {
        Path tempFile = tempDir.resolve("tasks.txt");
        TaskRepository repo = new TaskRepository(tempFile);

        Task task = repo.addTask("Belum Selesai", "");
        assertFalse(task.isCompleted());

        repo.markCompleted(task.getId(), true);
        assertTrue(repo.findById(task.getId()).isCompleted());
    }

    @Test
    void testSaveAndLoadFromFile() throws IOException {
        Path tempFile = tempDir.resolve("tasks.txt");
        TaskRepository repoSource = new TaskRepository(tempFile);

        // 1. Tambah data di repo pertama dan simpan ke file
        repoSource.addTask("Task File 1", "Deskripsi File 1");
        repoSource.addTask("Task File 2", "Deskripsi File 2");
        repoSource.saveToFile();

        // Pastikan file fisik benar-benar terbuat
        assertTrue(Files.exists(tempFile));

        // 2. Buat repo baru dengan file yang sama, lalu load data
        TaskRepository repoDest = new TaskRepository(tempFile);
        repoDest.loadFromFile();

        List<Task> loadedTasks = repoDest.findAll();
        assertEquals(2, loadedTasks.size());
        assertEquals("Task File 1", loadedTasks.get(0).getTitle());
        assertEquals("Task File 2", loadedTasks.get(1).getTitle());
        
        // Memastikan nextId ter-update dengan benar setelah di-load (ID terakhir + 1 = 3)
        Task t3 = repoDest.addTask("Task Baru", "");
        assertEquals(3, t3.getId());
    }

    @Test
    void testSaveEmptyRepositoryDeletesFile() throws IOException {
        Path tempFile = tempDir.resolve("tasks.txt");
        Files.createFile(tempFile); // Buat file kosong dulu
        
        TaskRepository repo = new TaskRepository(tempFile);
        // Kondisi repo kosong, panggil save
        repo.saveToFile();

        // Sesuai logika di TaskRepository, file harusnya dihapus jika repo kosong
        assertFalse(Files.exists(tempFile));
    }
}