package service;

import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

/**
 * Класс для управления всеми задачами
 */
public interface TaskManager {

    int getId();

    List<Task> getPrioritizedTasks();

    List<Task> getHistory();

    void removeTaskFromViewed(int id);

    List<Task> getTasks();

    void removeAllTasks();

    Task getTaskById(int id) throws NotFoundException;

    Task createTask(Task task) throws ValidationException;

    void upDateTask(Task task) throws NotFoundException, ValidationException;

    void removeTaskById(int id);

    List<Epic> getEpics();

    void removeAllEpics();

    Epic getEpicById(int id);

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic) throws NotFoundException;

    void removeEpicById(int id);

    List<SubTask> getAllSubTaskInEpic(Epic epic);

    List<SubTask> getSubTasks();

    void removeAllSubTasks();

    SubTask getSubTaskById(int id);

    SubTask createSubTask(SubTask subTask) throws ValidationException;

    void updateSubTask(SubTask subTask) throws NotFoundException, ValidationException;

    void removeSubTaskById(int id);

}
