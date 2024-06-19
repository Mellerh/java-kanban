package service.file;

import exception.ValidationException;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import service.TaskManagerTest;
import service.memory.InMemoryHistoryManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    protected FileBackedTaskManager createManager() {
        return new FileBackedTaskManager(new InMemoryHistoryManager());
    }


    TaskManager taskManagerLoader;
    FileBackedTaskManager fileBackedTaskManager;
    Path tempFile;


    @BeforeEach
    protected void init() throws ValidationException {
        super.init();

        fileBackedTaskManager = createManager();

        // создаём файл и добавляем в него данные
        try {
            tempFile = Files.createTempFile("task", ".csv");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании временного файла", e);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            writer.append("id,type,name,status,description,epic,duration,startTime");
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


    }


    @Test
    @DisplayName("Тест проверяет корректную загрузку данных из файла и установку корректного id " +
            "для генерации следующих задач")
    void shouldLoadFromFileAllTasks() {

        assertEqualsListOfTasks(taskManagerLoader.getTasks(), taskManager.getTasks());
        assertEqualsListOfTasks(taskManagerLoader.getEpics(), taskManager.getEpics());
        assertEqualsListOfTasks(taskManagerLoader.getSubTasks(), taskManager.getSubTasks());

    }

    @DisplayName("Тест проверяет корректное преобразование задачи в строку")
    @Test
    void shouldConvertTasksToString() {

        String convertedTaskToLine = "1,Task,task1,NEW,descriptionTask1,null,15," + task.getStartTime();
        String convertedSubTaskToLine = "3,SubTask,subTask1Epic1,NEW,descriptionSubTask1Epic1,2,15,"
                + subTask.getStartTime();

        assertEquals(convertedTaskToLine, fileBackedTaskManager.toString(task), "toString неккоректно " +
                "конвертирует Task в строку");
        assertEquals(convertedSubTaskToLine, fileBackedTaskManager.toString(subTask), "toString неккоректно " +
                "конвертирует subTask в строку");

    }

    @DisplayName("Тест проверяет корректное преорбразование строки в файл")
    @Test
    void shouldConvertTasksFromString() {

        String convertedTaskToLine = "1,Task,task1,NEW,descriptionTask1,null,15," + task.getStartTime();
        Task convertedTaskFromLine = fileBackedTaskManager.fromString(convertedTaskToLine);

        assertEqualsTask(task, convertedTaskFromLine, "fromString неккоректно преобразовывает " +
                "строку в задачу");

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

    /**
     * метод создан для корректного сравнения задач
     */
    private static void assertEqualsTask(Task expectedTask, Task actualTask, String description) {
        assertEquals(expectedTask.getId(), actualTask.getId(), description + " id");
        assertEquals(expectedTask.getName(), actualTask.getName(), description + " name");
    }
}