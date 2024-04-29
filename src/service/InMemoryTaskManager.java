package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    // хеш-мап для хранения задач. Integer - для хранения id задач
    public Map<Integer, Task> tasks;

    // хеш-мапа для хранения эпиков
    private final Map<Integer, Epic> epics;

    // хеш-мапа для хранения сабТасков эпика
    private final Map<Integer, SubTask> subTasks;

    // класс для работы с просмотренными задачами
    private final InMemoryHistoryManager inMemoryHistoryManager;

    // глобальный id-генератор для задач
    private int id = 0;


    public InMemoryTaskManager(InMemoryHistoryManager inMemoryHistoryManager) {
        this.inMemoryHistoryManager = inMemoryHistoryManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    /**
     * метод для генерации нового id
     */
    private void idGenerator() {
        ++id;
    }

    /**
     * метод возвращает список просмотренных задач из кла из списка viewedTasks
     */
    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHist();
    }



    // TASK-методы

    /**
     * метод для вывода всех задач Task
     */
    @Override
    public List<Task> getTasks() {
        // этот код написан на всякий случай, если getAll подразумевает добавление всех задач в просмотренные
        /*for (Task task : tasks.values()) {
            inMemoryHistoryManager.addViewedT(task);
        }*/

        return new ArrayList<>(tasks.values());
    }

    /**
     * метод для удаления всех Task
     */
    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    /**
     * метод для поиска Task по её id
     */
    @Override
    public Task getTaskById(int id) {
        inMemoryHistoryManager.addViewedT(tasks.get(id));

        return tasks.get(id);
    }

    /**
     * метод для создания нового Task
     */
    @Override
    public Task createTask(Task task) {
        // создаём id для задачи
        idGenerator();
        task.setId(this.id);
        // сохраняем новый task в хеш-таблицу
        tasks.put(this.id, task);

        // возврат объекта нужен для frontEnd
        return task;
    }

    /**
     * метод для обновления Task
     */
    @Override
    public void upDateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /**
     * метод для удаление Task по id
     */
    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }



    // EPIC-методы

    /**
     * метод для вывода всех задач Epic
     */
    @Override
    public List<Epic> getEpics() {
        // этот код написан на всякий случай, если getAll подразумевает добавление всех задач в просмотренные
        /*for (Epic epic : epics.values()) {
            inMemoryHistoryManager.addViewedT(epic);
        }*/

        return new ArrayList<>(epics.values());
    }

    /**
     * метод для удаления всех Epic, и, соответственно, всех субТасков
     */
    @Override
    public void removeAllEpics() {
        epics.clear();
        removeAllSubTasks();
    }

    /**
     * метод для поиска Epic по её id
     */
    @Override
    public Epic getEpicById(int id) {
        inMemoryHistoryManager.addViewedT(epics.get(id));

        return epics.get(id);
    }

    /**
     * метод для создания нового Epic
     */
    @Override
    public Epic createEpic(Epic epic) {
        // создаём id для задачи
        idGenerator();
        epic.setId(this.id);

        // сохраняем новый task в хеш-таблицу
        epics.put(this.id, epic);

        epic.updateStatus();

        // возврат объекта нужен для frontEnd
        return epic;
    }

    /**
     * метод для обновления Epic
     */
    @Override
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
    @Override
    public void removeEpicById(int id) {
        epics.remove(id);
    }

    /**
     * метод для получения списка всех подзадач определённого эпика
     */
    @Override
    public List<SubTask> getAllSubTaskInEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());

        // этот код написан на всякий случай, если getAll подразумевает добавление всех задач в просмотренные
        /*for (SubTask subTask : savedEpic.getSubTasks()) {
            inMemoryHistoryManager.addViewedT(subTask);
        }*/

        return savedEpic.getSubTasks();
    }



    // SUBTASK-методы

    /**
     * метод для вывода всех задач SubTusk
     */
    @Override
    public List<SubTask> getSubTasks() {
        // этот код написан на всякий случай, если getAll подразумевает добавление всех задач в просмотренные
        /*for (SubTask subTask : subTasks.values()) {
            inMemoryHistoryManager.addViewedT(subTask);
        }*/

        return new ArrayList<>(subTasks.values());
    }

    /**
     * метод для удаления всех SubTask
     * из-за удаления сабТасков обновятся все статусы у Эпиков
     */
    @Override
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
    @Override
    public SubTask getSubTaskById(int id) {
        inMemoryHistoryManager.addViewedT(subTasks.get(id));

        return subTasks.get(id);
    }

    /**
     * метод для создания нового SubTask
     * также метод добавляем Субтаск в соответсвующий Эпик
     */
    @Override
    public SubTask createSubTask(SubTask subTask) {
        // создаём id для задачи
        idGenerator();
        subTask.setId(this.id);

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
    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpicId());
        epic.updateSubTask(subTask);
    }

    /**
     * метод для удаление SubTask по id
     */
    @Override
    public void removeSubTaskById(int id) {
        SubTask removedSubTask = subTasks.remove(id);

        Epic epic = epics.get(removedSubTask.getEpicId());
        epic.removeSubTask(removedSubTask);
    }


}
