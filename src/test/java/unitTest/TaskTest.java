/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unitTest;

/**
 *
 * @author calderaly
 */
import com.caldera.todo.list.caldera.cli.Task;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    void testTaskConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task(1, "Beli Susu", "Rasa cokelat", true, now);

        assertEquals(1, task.getId());
        assertEquals("Beli Susu", task.getTitle());
        assertEquals("Rasa cokelat", task.getDescription());
        assertTrue(task.isCompleted());
        assertEquals(now, task.getCreatedAt());
    }

    @Test
    void testShortConstructorDefaults() {
        Task task = new Task(2, "Belajar Java", "Unit Testing");

        assertEquals(2, task.getId());
        assertFalse(task.isCompleted());
        assertNotNull(task.getCreatedAt());
    }

    @Test
    void testToStorageStringEscapesPipe() {
        Task task = new Task(1, "Kerja|Kantor", "Detail|Tugas", false, 
                LocalDateTime.of(2026, 6, 7, 12, 0, 0));
        
        String expected = "1|0|2026-06-07 12:00:00|Kerja/Kantor|Detail/Tugas";
        assertEquals(expected, task.toStorageString());
    }

    @Test
    void testFromStorageStringValidLine() {
        String line = "5|1|2026-06-07 15:30:22|Makan Siang|Di Restoran";
        Task task = Task.fromStorageString(line);

        assertEquals(5, task.getId());
        assertTrue(task.isCompleted());
        assertEquals("Makan Siang", task.getTitle());
        assertEquals("Di Restoran", task.getDescription());
        assertEquals(LocalDateTime.of(2026, 6, 7, 15, 30, 22), task.getCreatedAt());
    }

    @Test
    void testFromStorageStringInvalidLineThrowsException() {
        String invalidLine = "1|0|FormatSalah";
        assertThrows(IllegalArgumentException.class, () -> {
            Task.fromStorageString(invalidLine);
        });
    }

    @Test
    void testEqualsAndHashCodeBasedOnId() {
        Task task1 = new Task(1, "Task A", "Desc A");
        Task task2 = new Task(1, "Task B", "Desc B"); // ID sama, konten beda
        Task task3 = new Task(2, "Task A", "Desc A"); // ID beda, konten sama

        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
        assertEquals(task1.hashCode(), task2.hashCode());
    }
}
