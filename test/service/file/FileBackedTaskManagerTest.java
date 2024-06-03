package service.file;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    TaskManager taskManager;
    TaskManager taskManagerLoader;
    FileBackedTaskManager fileBackedTaskManager;
    Path tempFile;

    private Task task;
    private Epic epic;
    private SubTask subTask;

    private List<Task> tasks;
    private List<Epic> epics;
    private List<SubTask> subTasks;


    @BeforeEach
    void init() {

        try {
            taskManager = Managers.getDefault();
            FileBackedTaskManager fileBackedTaskManager = (FileBackedTaskManager) taskManager;

            task = taskManager.createTask(new Task("task1", Status.NEW, "descriptionTask1"));
            epic = taskManager.createEpic(new Epic("epic1", "descriptionEpic1"));
            subTask = taskManager.createSubTask(new SubTask(epic.getId(), "subTask1Epic1", Status.NEW,
                    "descriptionSubTask1Epic1"));


            // создаём файл и добавляем в него данные
            tempFile = Files.createTempFile("task", ".csv");

            try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
                writer.append("id,type,name,status,description,epic");
                writer.newLine();

                writer.append(fileBackedTaskManager.toString(task));
                writer.newLine();
                writer.append(fileBackedTaskManager.toString(epic));
                writer.newLine();
                writer.append(fileBackedTaskManager.toString(subTask));
                writer.newLine();

            } catch (IOException e) {
                throw new RuntimeException("Ошибка в файле: " + tempFile.getFileName(), e);
            }

            // создаём новый менеджер, передав в него задачи из временного файла
            taskManagerLoader = FileBackedTaskManager.loadFromFile(tempFile);

            List<String> fileContent = Files.readAllLines(tempFile);
            System.out.println("Файл создан с содержимым:");
            fileContent.forEach(System.out::println);

            System.out.println(taskManagerLoader.getSubTasks());
            System.out.println(taskManager.getSubTasks());


        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании временного файла", e);
        }


    }


    @Test
    @DisplayName("Тест проверяет корректную загрузку данных из файла и установку корректного id " +
            "для генерации следующих задач")
    void shouldLoadFromFileAllTasks() {

        assertEqualsListOfTasks(taskManagerLoader.getTasks(), taskManager.getTasks());
        assertEqualsListOfTasks(taskManagerLoader.getEpics(), taskManager.getEpics());
//        assertEqualsListOfTasks(taskManagerLoader.getSubTasks(), taskManager.getSubTasks());

        /*System.out.println(taskManagerLoader.getSubTasks());
        System.out.println(taskManager.getSubTasks());*/
    }

    @DisplayName("Тест проверяет корректную загрузку данных из файла")
    @Test
    void shouldConvertTasksToString() {
    }

    @DisplayName("Тест проверяет корректную загрузку данных из файла")
    @Test
    void shouldConvertTasksFromString() {
    }

    @DisplayName("Тест проверяет корректное сохранение данных в файл")
    @Test
    void shouldSaveTasksCorrectlyToFile() {
    }


    /**
     * переопределяем метод для корректного сравнения полей задач из списокв
     */
    private static <T extends Task> void assertEqualsListOfTasks(List<T> expectedTaskList, List<T> actualTaskList) {
        assertEquals(expectedTaskList.size(), actualTaskList.size(), "Размеры списков не ранвы");

        for (int i = 0; i < expectedTaskList.size(); i++) {
            assertEquals(expectedTaskList.get(i).getId(), actualTaskList.get(i).getId(), "id задач " +
                    "не совпадают");
            assertEquals(expectedTaskList.get(i).getName(), actualTaskList.get(i).getName(), "имена задач " +
                    "не совпадают");
        }

    }
}