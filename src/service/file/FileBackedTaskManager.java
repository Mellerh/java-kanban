package service.file;

import exception.ManagerLoadException;
import exception.ManagerSaveException;
import exception.NotFoundException;
import exception.ValidationException;
import model.*;
import service.HistoryManager;
import service.memory.InMemoryHistoryManager;
import service.memory.InMemoryTaskManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path file;

    public FileBackedTaskManager(HistoryManager historyManager) {
        this(historyManager, Paths.get("task.csv"));
    }

    private FileBackedTaskManager(HistoryManager inMemoryHistoryManager, Path file) {
        super(inMemoryHistoryManager);
        this.file = file;
    }

    private FileBackedTaskManager(Path file) {
        this(new InMemoryHistoryManager(), file);
    }



    // Task
    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public void removeAllTasks() {
        super.tasks.clear();
        save();
    }

    @Override
    public Task getTaskById(int id) throws NotFoundException {
        return super.getTaskById(id);
    }

    @Override
    public Task createTask(Task task) throws ValidationException {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public void upDateTask(Task task) throws ValidationException, NotFoundException {
        super.upDateTask(task);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }


    // Epic
    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public void updateEpic(Epic epic) throws NotFoundException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public List<SubTask> getAllSubTaskInEpic(Epic epic) {
        return super.getAllSubTaskInEpic(epic);
    }


    // SUBTASK-методы
    @Override
    public List<SubTask> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public SubTask getSubTaskById(int id) {
        return super.getSubTaskById(id);
    }

    @Override
    public SubTask createSubTask(SubTask subTask) throws ValidationException {
        SubTask newSubTask = super.createSubTask(subTask);
        save();
        return newSubTask;
    }

    @Override
    public void updateSubTask(SubTask subTask) throws ValidationException, NotFoundException {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }


    /**
     * метод восстанавливает данные менеджера из файла при запуске программы
     */
    public static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.loadAndReadFromFile();
        return manager;
    }

    /**
     * метод загружает данные из файла. используется в loadFromFile
     */
    private void loadAndReadFromFile() {
        int maxId = 0;
        String line;

        try (final BufferedReader reader = Files.newBufferedReader(file)) {

            reader.readLine();

            while (reader.ready()) {
                line = reader.readLine();
                Task task = fromString(line);

                int id = task.getId();

                // Добавляем Task
                if (task.getTaskType() == TaskType.Task) {
                    tasks.put(task.getId(), task);
                    prioritizedTasks.add(task);
                }

                // Добавляем Epic
                if (task.getTaskType() == TaskType.Epic) {
                    epics.put(task.getId(), (Epic) task);
                }

                // Добавляем SubTask
                if (task.getTaskType() == TaskType.SubTask) {
                    subTasks.put(task.getId(), (SubTask) task);
                    prioritizedTasks.add(task);
                }

                if (maxId < id) {
                    maxId = id;
                }


            }

            // Сохраняем максимальный ID задач
            super.setId(maxId); // Вызов метода суперкласса
            setId(maxId); // Вызов метода текущего класса

        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка в файле" + file.toFile().getAbsolutePath(), e);
        }
    }

    /**
     * метод преобразует задачу в строку
     */
    public String toString(Task task) {

        Long taskGetDuration = null;
        if (task.getDuration() != null) {
            taskGetDuration = task.getDuration().toMinutes();
        }

        return task.getId() + "," + task.getTaskType() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription() + "," + task.getEpicId() + ","
                + taskGetDuration + "," + task.getStartTime();

    }

    /**
     * метод преобразует строку в задачу
     */
    public Task fromString(String value) {

        Task task = null;

        String[] valuesFromSting = value.split(",");

        Integer id = Integer.valueOf(valuesFromSting[0]);
        String name = valuesFromSting[2];
        Status status = Status.valueOf(valuesFromSting[3]);
        String description = valuesFromSting[4];


        // парсим строку-продолжительность в число
        Long duration = null;
        if (!valuesFromSting[6].equals("null")) {
            duration = Long.parseLong(valuesFromSting[6]);
        }

        // парсим строку-startTime в LocalDate
        LocalDateTime startTime = null;
        if (!valuesFromSting[7].equals("null")) {
            startTime = LocalDateTime.parse(valuesFromSting[7]);
        }


        Integer epicId = null;
        if (!valuesFromSting[5].equals("null")) {
            epicId = Integer.valueOf(valuesFromSting[5]);
        }

        TaskType taskType = TaskType.valueOf(valuesFromSting[1]);

        task = switch (taskType) {
            case TaskType.Task -> new Task(id, name, status, description, startTime, duration);
            case TaskType.Epic -> new Epic(id, name, status, description, startTime, duration);
            case TaskType.SubTask -> new SubTask(id, name, status, description, epicId, startTime, duration);
        };

        return task;
    }


    /**
     * метод сохраняет задачи, эпики, подзадачи в файл
     */
    public void save() {

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {

            writer.append("id,type,name,status,description,epic,duration,startTime");
            writer.newLine();

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }

            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }

            for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле" + file.toFile().getAbsolutePath(), e);
        }

    }


}
