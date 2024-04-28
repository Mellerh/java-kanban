package service;

import java.util.List;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    void shouldGetTasks() {
        TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task = taskManager.createTask(new Task("Машина", Status.NEW, "нужно купить машину"));

        List<Task> tasks = taskManager.getTasks();

        assertEquals(1, tasks.size(), "В список tasks неккоректно добавляются задачи");

        assertEquals(tasks.getFirst(), task, "В список tasks неккоректно сохраняются задачи");

    }


}