package service.file;

import Converter.TaskConverter;
import exception.ManagerSaveException;
import exception.NotFoundException;
import model.*;
import service.HistoryManager;
import service.Managers;
import service.memory.InMemoryTaskManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path file;
    TaskConverter converter = new TaskConverter();

    public FileBackedTaskManager(HistoryManager historyManager) {
        this(historyManager, Paths.get("resources/task.csv"));
    }


    public FileBackedTaskManager(HistoryManager inMemoryHistoryManager, Path file) {
        super(inMemoryHistoryManager);
        this.file = file;
    }

    public FileBackedTaskManager(Path file) {
        this(Managers.getHistoryManager(), file);
    }


    public static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        return manager;
    }

    // TODO....
    private void loadFromFile() {
        int maxId = 0;

        try (final BufferedReader reader = Files.newBufferedReader(file)) {

        } catch (IOException e) {
            throw new NotFoundException("Ошибка в файле" + file.toFile().getAbsolutePath(), );
        }
    }

    public String toString(Task task) {

        return task.getId() + "," + task.getTaskType() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription() + "," + task.getEpicId();

    }

    public Task fromString(String value) {

        String[] valuesFromSting = value.split(",");

        Integer id = Integer.valueOf(valuesFromSting[0]);
        String name = valuesFromSting[2];
        Status status = Status.valueOf(valuesFromSting[3]);
        String description = valuesFromSting[4];
        Integer epicId = Integer.valueOf(valuesFromSting[5]);

        TaskType taskType = TaskType.valueOf(valuesFromSting[1]);
        Task task = null;
        switch (taskType) {
            case TaskType.Task:
                task = new Task(id, name, status, description);
                break;

            case TaskType.Epic:
                task = new Epic(id, name, status, description);
                break;

            case TaskType.SubTask:
                task = new SubTask(id, name, status, description, epicId);
                break;
        }

        return task;
    }


    /*
     * метод сохраняет задачи, эпики, подзадачи в файл
     */
    public void save() {

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                writer.append(converter.toString(entry.getValue()));
                writer.newLine();
            }

            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                writer.append(converter.toString(entry.getValue()));
                writer.newLine();
            }

            for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                writer.append(converter.toString(entry.getValue()));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле" + file.toFile().getAbsolutePath(), e);
        }

    }


}
