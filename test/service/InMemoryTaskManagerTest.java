package service;

import java.util.List;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryTaskManagerTest")
class InMemoryTaskManagerTest {

    TaskManager taskManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;


    @BeforeEach
    void init() {
        taskManager = Managers.getDefault();

        task = taskManager.createTask(new Task("task1", Status.NEW, "descriptionTask1"));

        epic = taskManager.createEpic(new Epic("epic1", "descriptionEpic1"));
        subTask = taskManager.createSubTask(new SubTask(epic.getId(), "subTask1Epic1", Status.NEW,
                "descriptionSubTask1Epic1"));
    }



    @Test
    @DisplayName("Задачи должны корректно добавляться в tasks")
    void shouldGetTasks() {
        List<Task> tasks = taskManager.getTasks();

        assertEquals(1, tasks.size(), "В список tasks неккоректно добавляются задачи");

        assertEquals(tasks.getFirst(), task, "В список tasks неккоректно сохраняются задачи");

    }


    private static void assertEqualsTask(Task expectedTask, Task actualTask, String description) {
        assertEquals(expectedTask.getId(), actualTask.getId(), description + " id");
        assertEquals(expectedTask.getName(), actualTask.getName(), description + " name");
    }


}