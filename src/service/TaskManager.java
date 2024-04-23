package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс для управления всеми задачами
 */
interface TaskManager {

    ArrayList<Task> getTasks();

    void removeAllTasks();

    Task getTaskById(int id);

    Task createTask(Task task);

    void upDateTask(Task task);

    void removeTaskById(int id);

    ArrayList<Epic> getEpics();

    void removeAllEpics();

    Epic getEpicById(int id);

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    void removeEpicById(int id);

    ArrayList<SubTask> getAllSubTaskInEpic(Epic epic);

    ArrayList<SubTask> getSubTasks();

    void removeAllSubTasks();

    SubTask getSubTaskById(int id);

    SubTask createSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    void removeSubTaskById(int id);

}
