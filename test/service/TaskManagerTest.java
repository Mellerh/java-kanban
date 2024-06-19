package service;

import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@DisplayName("service.TaskManagerTest")
public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createManager();


    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    protected List<Task> tasks;
    protected List<Epic> epics;
    protected List<SubTask> subTasks;


    @BeforeEach
    protected void init() throws ValidationException {
        taskManager = createManager();

        task = taskManager.createTask(new Task("task1", Status.NEW, "descriptionTask1",
                LocalDateTime.now(), 15L));

        epic = taskManager.createEpic(new Epic("epic1", "descriptionEpic1"));

        subTask = taskManager.createSubTask(new SubTask(epic.getId(), "subTask1Epic1",
                Status.NEW, "descriptionSubTask1Epic1", LocalDateTime.parse("2026-12-21T21:00:00"),
                15L));
    }

    @Test
    @DisplayName("Проверяем корректность работы выброса исключения при работе с задачами")
    public void shouldCheckTaskTimeOfTasks() {

        Assertions.assertThrows(Exception.class, () -> {
            taskManager.createTask(new Task("task1", Status.NEW, "descriptionTask1",
                    LocalDateTime.now(), 15L));
        }, "должно быть выброшено исключение из-за пересенчения задач");


        Assertions.assertThrows(Exception.class, () -> {
            taskManager.createTask(new SubTask(epic.getId(), "subTask1Epic1",
                    Status.NEW, "descriptionSubTask1Epic1", LocalDateTime.parse("2026-12-21T21:14:00"),
                    15L));
        }, "должно быть выброшено исключение из-за пересенчения задач");


        Assertions.assertDoesNotThrow(() -> {
            taskManager.createTask(new SubTask(epic.getId(), "subTask1Epic1",
                    Status.NEW, "descriptionSubTask1Epic1", LocalDateTime.parse("2026-12-21T21:15:00"),
                    15L));
        }, "throws не должно произайти, потому что задачи не пересекаются");

    }


    // ТЕСТЫ TASK
    @Test
    @DisplayName("Task должен корректно добавляться в tasks")
    void shouldGetTasks() {
        tasks = taskManager.getTasks();

        assertEquals(1, tasks.size(), "В список tasks неккоректно добавляются задачи");

        assertEquals(tasks.getFirst(), task, "В список tasks неккоректно сохраняются задачи");

    }


    @Test
    @DisplayName("Tasks должны корректно удалятсья")
    void shouldRemoveAllTasks() {
        tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "В список tasks неккоректно добавляются задачи");

        taskManager.removeAllTasks();
        tasks = taskManager.getTasks();
        assertEquals(0, tasks.size(), "Из списка tasks неккоректно удаляются задачи");
    }


    @Test
    @DisplayName("Метод должен возвращать нужный task по его id")
    void shouldGetTaskById() {
        tasks = taskManager.getTasks();

        assertEqualsTask(task, tasks.get(0), "Неккоретный поиск task в списке tasks");

    }


    @Test
    @DisplayName("Метод должен корректно создавать task")
    void shouldCreateTask() {
        // task уже создан через метод createTask в методе @Before init
        assertEquals(1, task.getId(), "неверный id у созданного первого task");
        assertEquals("task1", task.getName(), "неверный name у созданного первого task");
        assertEquals(Status.NEW, task.getStatus(), "неверный Status у созданного первого task");
        assertEquals("descriptionTask1", task.getDescription(), "неверный description" +
                " у созданного первого task");
    }


    @Test
    @DisplayName("Метод должен корректно обновлять task")
    void shouldUpdateTask() throws NotFoundException, ValidationException {
        task.setDescription("descriptionTask1Updated");
        task.setStatus(Status.IN_PROGRESS);

        tasks = taskManager.getTasks();

        taskManager.upDateTask(task);

        Task actualTask = taskManager.getTaskById(task.getId());

        assertEquals("descriptionTask1Updated", actualTask.getDescription(),
                "Неккоретное обновление description task");

        assertEquals(Status.IN_PROGRESS, actualTask.getStatus(),
                "Неккоретное обновления status task");
    }


    @Test
    @DisplayName("Метод должен корректно удалять task по id")
    void shouldRemoveTask() {
        tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "неккоректная работа taskManager.getTasks");

        taskManager.removeTaskById(task.getId());
        tasks = taskManager.getTasks();
        assertEquals(0, tasks.size(), "неккоректное удаление task по id");
    }


    // ТЕСТЫ EPIC
    @Test
    @DisplayName("Метод должен удалять все эпики")
    void shouldRemoveAllEpics() {
        epics = taskManager.getEpics();

        assertEquals(1, epics.size(), "неккоректное получение epics");

        taskManager.removeAllEpics();
        epics = taskManager.getEpics();
        assertEquals(0, epics.size(), "неккоректное удаление epics");
    }


    @Test
    @DisplayName("Метод должен получать epic по его id")
    void shouldGetEpicById() {
        Epic actualEpic = taskManager.getEpicById(epic.getId());

        assertEqualsTask(epic, actualEpic, "Неккоретный поиск epic в списке epics");
    }


    @Test
    @DisplayName("Метод должен корректно создавать epic")
    void shouldCreateEpic() {
        // epic уже создан через метод createTask в методе @Before init
        assertEquals(2, epic.getId(), "неверный id у созданного первого epic");
        assertEquals("epic1", epic.getName(), "неверный name у созданного первого epic");
        assertEquals("descriptionEpic1", epic.getDescription(), "неверный description" +
                " у созданного первого epic");
    }


    @Test
    @DisplayName("Метод должен корректно обновлять epic")
    void shouldUpdateEpic() throws NotFoundException {
        epic.setDescription("descriptionEpic1Updated");
        epic.setStatus(Status.IN_PROGRESS);

        epics = taskManager.getEpics();

        taskManager.updateEpic(epic);

        Epic actualEpic = taskManager.getEpicById(epic.getId());

        assertEquals("descriptionEpic1Updated", actualEpic.getDescription(),
                "Неккоретное обновление description epic");

        assertEquals(Status.IN_PROGRESS, actualEpic.getStatus(),
                "Неккоретное обновления status epic");
    }


    @Test
    @DisplayName("Метод должен корректно удалять Epic по id")
    void shouldRemoveEpicById() {
        epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "неккоректное получение epics");

        taskManager.removeEpicById(epic.getId());
        epics = taskManager.getEpics();
        assertEquals(0, epics.size(), "неккоректное удаление epics");
    }


    @Test
    @DisplayName("Метод должен возвращать все subTask в epic")
    void shouldReturnAllSubTasksInEpic() {
        subTasks = taskManager.getAllSubTaskInEpic(epic);

        assertEqualsTask(subTask, subTasks.getFirst(), "неккоректное получение subTasks");
    }


    // subTask методы
    @Test
    @DisplayName("Метод должен удалять все subTasks")
    void shouldRemoveAllSubTasks() {
        subTasks = taskManager.getSubTasks();
        assertEquals(1, subTasks.size(), "неккоректное получение epics");

        taskManager.removeAllSubTasks();
        subTasks = taskManager.getSubTasks();
        assertEquals(0, subTasks.size(), "неккоректное удаление epics");
    }


    @Test
    @DisplayName("Метод должен возвращать subTask по его id")
    void shouldGetSubTaskById() {
        SubTask actualSubTask = taskManager.getSubTaskById(subTask.getId());

        assertEqualsTask(subTask, actualSubTask, "Неккоретный поиск epic в списке epics");
    }


    @Test
    @DisplayName("Метод должен корректно создавать subTask")
    void shouldCreateSubTask() {
        // subTask уже создан через метод createTask в методе @Before init
        assertEquals(3, subTask.getId(), "неверный id у созданного первого subTask");
        assertEquals("subTask1Epic1", subTask.getName(), "неверный name у созданного первого subTask");
        assertEquals("descriptionSubTask1Epic1", subTask.getDescription(), "неверный description" +
                " у созданного первого subTask");
    }


    @Test
    @DisplayName("Метод должен корректно обновлять subTask")
    void shouldUpdateSubTask() throws ValidationException, NotFoundException {
        subTask.setDescription("descriptionSubTask1Epic1Updated");
        subTask.setStatus(Status.IN_PROGRESS);

        subTasks = taskManager.getSubTasks();

        taskManager.updateSubTask(subTask);

        SubTask actualSubTask = taskManager.getSubTaskById(subTask.getId());

        assertEquals("descriptionSubTask1Epic1Updated", actualSubTask.getDescription(),
                "Неккоретное обновление description subTask");

        assertEquals(Status.IN_PROGRESS, actualSubTask.getStatus(),
                "Неккоретное обновления status subTask");
    }


    @Test
    @DisplayName("Метод должен удалять subTask по его id")
    void shouldRemoveSubTaskById() {
        subTasks = taskManager.getSubTasks();
        assertEquals(1, subTasks.size(), "неккоректное получение subTasks");

        taskManager.removeSubTaskById(subTask.getId());
        subTasks = taskManager.getSubTasks();
        assertEquals(0, subTasks.size(), "неккоректное удаление subTasks");
    }

    @Test
    @DisplayName("Метод проверяет, что после удаления эпика удалились все его подзадачи")
    void shouldRemovedAllSubTaskInDeletedEpic() {

        assertEquals(subTask, taskManager.getSubTaskById(subTask.getId()), "неккоректное получение сабТаска");

        taskManager.removeEpicById(epic.getId());

        assertNull(taskManager.getSubTaskById(subTask.getId()), "неккоректное удаление " +
                "сабтасков после удаление эпика");
    }

    @Test
    @DisplayName("Метод проверяет целостность данных при изменении полей задачи")
    void shouldUpdateListOfViewedTasksAfterSetDateIntoThem() throws NotFoundException {
        task.setName("new name");
        task.setDescription("new description");
        taskManager.getTaskById(task.getId());

        Task updatedTask = taskManager.getHistory().get(0);
        assertEquals("new name", updatedTask.getName(), "Имя задачи не обновлено в истории.");
        assertEquals("new description", updatedTask.getDescription(), "Описание задачи не обновлено в истории.");
    }


    /**
     * метод создан для корректного сравнения задач
     */
    private static void assertEqualsTask(Task expectedTask, Task actualTask, String description) {
        assertEquals(expectedTask.getId(), actualTask.getId(), description + " id");
        assertEquals(expectedTask.getName(), actualTask.getName(), description + " name");
    }


}
