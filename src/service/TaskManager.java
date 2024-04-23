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

    /**
     * метод для генерации нового id
     */
    int idGenerator();


    // TASK-методы

    /**
     * метод для вывода всех задач Task
     */
    ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * метод для удаления всех Task
     */
    void removeAllTasks() {
        tasks.clear();
    }

    /**
     * метод для поиска Task по её id
     */
    Task getTaskById(int id) {
        return tasks.get(id);
    }

    /**
     * метод для создания нового Task
     */
    Task createTask(Task task) {
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
    void upDateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /**
     * метод для удаление Task по id
     */
    void removeTaskById(int id) {
        tasks.remove(id);
    }


    // EPIC-методы

    /**
     * метод для вывода всех задач Epic
     */
    ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    /**
     * метод для удаления всех Epic, и, соответственно, всех субТасков
     */
    void removeAllEpics() {
        epics.clear();
        removeAllSubTasks();
    }

    /**
     * метод для поиска Epic по её id
     */
    Epic getEpicById(int id) {
        return epics.get(id);
    }

    /**
     * метод для создания нового Epic
     */
    Epic createEpic(Epic epic) {
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
    void updateEpic(Epic epic) {
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
    void removeEpicById(int id) {
        epics.remove(id);
    }

    /**
     * метод для получения списка всех подзадач определённого эпика
     */
    ArrayList<SubTask> getAllSubTaskInEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        return savedEpic.getSubTasks();
    }


    // SUBTASK-методы

    /**
     * метод для вывода всех задач SubTusk
     */
    ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    /**
     * метод для удаления всех SubTask
     * из-за удаления сабТасков обновятся все статусы у Эпиков
     */
    void removeAllSubTasks() {
        subTasks.clear();

        // удаляем все субтаски в каждом Эпике
        for (Integer id : epics.keySet()) {
            epics.get(id).removeAllSubTasks();
        }
    }

    /**
     * метод для поиска SubTask по её id
     */
    SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    /**
     * метод для создания нового SubTask
     * также метод добавляем Субтаск в соответсвующий Эпик
     */
    SubTask createSubTask(SubTask subTask) {
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
    void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpicId());
        epic.updateSubTask(subTask);
    }

    /**
     * метод для удаление SubTask по id
     */
    void removeSubTaskById(int id) {
        SubTask removedSubTask = subTasks.remove(id);

        Epic epic = epics.get(removedSubTask.getEpicId());
        epic.removeSubTask(removedSubTask);
    }


}
