package service.memory;

import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("InMemoryHistoryManagerTest")
class InMemoryHistoryManagerTest {

    TaskManager taskManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;


    @BeforeEach
    void init() throws ValidationException {
        taskManager = Managers.getDefault();

        task = taskManager.createTask(new Task("task1", Status.NEW, "descriptionTask1"));
        epic = taskManager.createEpic(new Epic("epic1", "descriptionEpic1"));
        subTask = taskManager.createSubTask(new SubTask(epic.getId(), "subTask1Epic1", Status.NEW,
                "descriptionSubTask1Epic1"));
    }

    @Test
    @DisplayName("Метод проверяет корректность добавление задачи в список просмотренных, и если задача добавляется" +
            "повторно, удаляет первое вхождение")
    void shouldAddViewedCorrectly() throws NotFoundException {
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubTaskById(subTask.getId());

        assertEquals(3, taskManager.getHistory().size(), "Неккоректное добавление просмотренных " +
                "задач в InMemoryHistoryManager viewedT");

        taskManager.getTaskById(task.getId());
        assertEquals(3, taskManager.getHistory().size(), "Неккоректное добавление повторно " +
                "просмотренных задач в InMemoryHistoryManager viewedT");
    }

    @Test
    @DisplayName("Метод проверяет корректный вывод всех просмотренных задч, с учётом повторно-просмотренных. " +
            "Также метод проверяет корректность удаления задач из списка просмотренных")
    void shouldReturnCorrectListOfViewedTasks() throws NotFoundException {
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubTaskById(subTask.getId());

        taskManager.getTaskById(task.getId());


        List<Task> listOfViewedTasks = new ArrayList<>();
        listOfViewedTasks.add(epic);
        listOfViewedTasks.add(subTask);
        listOfViewedTasks.add(task);

        assertEquals(listOfViewedTasks, taskManager.getHistory(), "Неккоректное добавление просмотренных задач" +
                "в список");

        taskManager.removeTaskFromViewed(epic.getId());
        listOfViewedTasks.remove(epic);
        assertEquals(listOfViewedTasks, taskManager.getHistory(), "Неккоректное удаление просмотренных задач" +
                "из списка");
    }

}