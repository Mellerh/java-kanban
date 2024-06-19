package model;

import exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("EpicTest")
class EpicTest {

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
    @DisplayName("Проверяем корректность добавление сабтасков в эпик и их удаление")
    void shouldAddSubTaskInToSubTasksArrayListAndRemoveThem() {

        assertEquals(1, epic.getSubTasks().size(), "неккоретное добавление сабТасков");

        epic.removeAllSubTasks();
        assertEquals(0, epic.getSubTasks().size(), "неккоретное удаление сабТасков");

        epic.addSubTask(subTask);
        assertEquals(1, epic.getSubTasks().size(), "неккоретное добавление сабТасков");

        epic.removeSubTask(subTask);
        assertEquals(0, epic.getSubTasks().size(), "неккоретное удаление сабТасков");
    }


    @Test
    @DisplayName("Проверям корректное обновление статуса эпика")
    void shouldUpdateStatusOfEpicCorrectly() throws ValidationException {
        assertEquals(Status.NEW, epic.getStatus(), "Неккоректный статус эпика. Проблема с методом обновления" +
                " updateSubTask");

        SubTask subTask2 = taskManager.createSubTask(new SubTask(epic.getId(), "subTask1Epic2", Status.DONE,
                "descriptionSubTask1Epic2"));

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Неккоректный статус эпика. Проблема с методом " +
                "обновления updateSubTask");
    }


    @Test
    @DisplayName("Проверяем корректное обновление subTask в листе subTasks")
    void shouldUpdateSubTaskInSubTasksListCorrectly() throws ValidationException {
        SubTask subTaskUpdated = taskManager.createSubTask(new SubTask(epic.getId(), "subTask1Epic1",
                Status.IN_PROGRESS, "descriptionSubTask1Epic1Updated"));

        epic.updateSubTask(subTaskUpdated);

        assertEqualsSubTasks(subTask, subTaskUpdated, "СабТаски с не равны");
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Неккоректное обновление статуса");
    }


    /**
     * метод assertEquals переопределён для корректного сравнения объектов
     */
    private static void assertEqualsSubTasks(SubTask expectedTask, SubTask actualTask, String description) {
        assertEquals(expectedTask.getEpicId(), actualTask.getEpicId(), description + " id");
        assertEquals(expectedTask.getName(), actualTask.getName(), description + " name");
    }


}