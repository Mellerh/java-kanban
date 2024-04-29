package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryHistoryManagerTest")
class InMemoryHistoryManagerTest {

    TaskManager taskManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;


    @BeforeEach
    void init() {
        taskManager = Managers.getDefault(10);

        task = taskManager.createTask(new Task("task1", Status.NEW, "descriptionTask1"));
        epic = taskManager.createEpic(new Epic("epic1", "descriptionEpic1"));
        subTask = taskManager.createSubTask(new SubTask(epic.getId(), "subTask1Epic1", Status.NEW,
                "descriptionSubTask1Epic1"));
    }

    @Test
    @DisplayName("Метод проверяет корректность добавление задачи в список просмотренных")
    void shouldAddViewedCorrectly() {
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubTaskById(subTask.getId());

        assertEquals(3, taskManager.getHistory().size(), "Неккоректное добавление просмотренных " +
                "задач в InMemoryHistoryManager viewedT");
    }


}