package service;

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

    Task getTaskById(int id);

    Task createTask(Task task);

    void upDateTask(Task task);

    void removeTaskById(int id);

    List<Epic> getEpics();

    void removeAllEpics();

    Epic getEpicById(int id);

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    void removeEpicById(int id);

    List<SubTask> getAllSubTaskInEpic(Epic epic);

    List<SubTask> getSubTasks();

    void removeAllSubTasks();

    SubTask getSubTaskById(int id);

    SubTask createSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    void removeSubTaskById(int id);

}
