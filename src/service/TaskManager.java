package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс для управления всеми задачами
 */
public class TaskManager {

    // хеш-мап для хранения задач. Integer - для хранения id задач
    public HashMap<Integer, Task> tasks;

    // хеш-мапа для хранения эпиков
    private final HashMap<Integer, Epic> epics;

    // хеш-мапа для хранения сабТасков эпика
    private final HashMap<Integer, SubTask> subTasks;

    // глобальный id-генератор для задач
    private int id = 0;


    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    /**
     * метод для генерации нового id
     */
    private int idGenerator() {
        return ++id;
    }


    // TASK-методы

    /**
     * метод для вывода всех задач Task
     */
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * метод для удаления всех Task
     */
    public void removeAllTasks() {
        tasks.clear();
    }

    /**
     * метод для поиска Task по её id
     */
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    /**
     * метод для создания нового Task
     */
    public Task createTask(Task task) {
        // создаём id для задачи
        task.setId(idGenerator());
        // сохраняем новый task в хеш-таблицу
        tasks.put(this.id, task);

        // возврат объекта нужен для frontEnd
        return task;
    }

    /**
     * метод для обновления Task
     */
    public void upDateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /**
     * метод для удаление Task по id
     */
    public void removeTaskById(int id) {
        tasks.remove(id);
    }


    // EPIC-методы

    /**
     * метод для вывода всех задач Epic
     */
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    /**
     * метод для удаления всех Epic, и, соответственно, всех субТасков
     */
    public void removeAllEpics() {
        epics.clear();
        removeAllSubTasks();
    }

    /**
     * метод для поиска Epic по её id
     */
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    /**
     * метод для создания нового Epic
     */
    public Epic createEpic(Epic epic) {
        // создаём id для задачи
        epic.setId(idGenerator());
        // сохраняем новый task в хеш-таблицу
        epics.put(this.id, epic);

        epic.updateStatus();

        // возврат объекта нужен для frontEnd
        return epic;
    }

    /**
     * метод для обновления Epic
     */
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());

        // проверяем, что epic нашёлся
        if (savedEpic == null) {
            return;
        }

        // в Epic реализована выборочная перезапись полей, так как полный апдейт эпика сотрёт все субтаски
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    /**
     * метод для удаление Epic по id
     */
    public void removeEpicById(int id) {
        epics.remove(id);
    }

    /**
     * метод для получения списка всех подзадач определённого эпика
     */
    public ArrayList<SubTask> getAllSubTaskInEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        return savedEpic.getSubTasks();
    }


    // SUBTASK-методы

    /**
     * метод для вывода всех задач SubTusk
     */
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    /**
     * метод для удаления всех SubTask
     * из-за удаления сабТасков обновятся все статусы у Эпиков
     */
    public void removeAllSubTasks() {
        subTasks.clear();

        // удаляем все субтаски в каждом Эпике
        for (Integer id : epics.keySet()) {
            epics.get(id).removeAllSubTasks();
        }
    }

    /**
     * метод для поиска SubTask по её id
     */
    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    /**
     * метод для создания нового SubTask
     * также метод добавляем Субтаск в соответсвующий Эпик
     */
    public SubTask createSubTask(SubTask subTask) {
        // создаём id для задачи
        subTask.setId(idGenerator());
        // сохраняем новый task в хеш-таблицу
        subTasks.put(this.id, subTask);

        // сохраняем таск в нужный Epic и обновим его статус
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTask(subTask);
        // возврат объекта нужен для frontEnd
        return subTask;
    }

    /**
     * метод для обновления SubTask
     */
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpicId());
        epic.updateSubTask(subTask);
    }

    /**
     * метод для удаление SubTask по id
     */
    public void removeSubTaskById(int id) {
        SubTask removedSubTask = subTasks.remove(id);

        Epic epic = epics.get(removedSubTask.getEpicId());
        epic.removeSubTask(removedSubTask);
    }


}
