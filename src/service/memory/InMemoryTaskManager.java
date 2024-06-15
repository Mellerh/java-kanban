package service.memory;

import exception.NotFoundException;
import model.Epic;
import model.SubTask;
import model.Task;
import service.HistoryManager;
import service.TaskManager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    // хеш-мап для хранения задач. Integer - для хранения id задач
    protected Map<Integer, Task> tasks;

    // хеш-мапа для хранения эпиков
    protected final Map<Integer, Epic> epics;

    // хеш-мапа для хранения сабТасков эпика
    protected final Map<Integer, SubTask> subTasks;

    // класс для работы с просмотренными задачами
    private final HistoryManager inMemoryHistoryManager;

    // глобальный id-генератор для задач
    private int id = 0;

    // поле для хранение отсортированного списка задач и подЗадач по их приоритету (времени начала startTime)
    Set<Task> prioritizedTasks;


    public InMemoryTaskManager(HistoryManager inMemoryHistoryManager) {
        this.inMemoryHistoryManager = inMemoryHistoryManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));
    }

    /**
     * метод для генерации нового id
     */
    private void idGenerator() {
        ++id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }


    /**
     * метод возвращает отсортированный списк задач и подЗадач по их приоритету
     */
    public Set<Task> getPrioritizedTasks() {
        return this.prioritizedTasks;
    }

    /**
     * компоратор для сортироваки задач и подЗадач по их приоритету
     * компортатор передаётся в prioritizedTasks при создании
     */
    Comparator<Task> comparatorForPrioritizedTasks = new Comparator<>() {
        @Override
        public int compare(Task t1, Task t2) {

        }
    }

    /**
     * метод возвращает список просмотренных задач из кла из списка viewedTasks
     */
    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    /**
     * метод удаляет задачу из просмотренных
     */
    @Override
    public void removeTaskFromViewed(int id) {
        inMemoryHistoryManager.removeTaskFromViewed(id);
    }

    // TASK-методы

    /**
     * метод для вывода всех задач Task
     */
    @Override
    public List<Task> getTasks() {

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
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача с id " + id + " не найдена");
        }

        inMemoryHistoryManager.addViewedT(tasks.get(id));

        return task;
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

        Task originalTask = tasks.get(task.getId());
        if (originalTask == null) {
            throw new NotFoundException("Task с id-" + task.getId() + "не найдена");
        }








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
            throw new NotFoundException("Не найден эпик: " + epic);
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

        Epic deletedEpic = epics.get(id);
        List<SubTask> subTaskListInDeletedEpic = deletedEpic.getSubTasks();

        // удаляем все вхождения сабТасокв в удаляемом эпике из subTasks
        for (Integer integer : subTasks.keySet()) {
            if (subTaskListInDeletedEpic.contains(subTasks.get(integer))) {
                subTasks.remove(integer);
            }
        }

        epics.remove(id);
    }

    /**
     * метод для получения списка всех подзадач определённого эпика
     */
    @Override
    public List<SubTask> getAllSubTaskInEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());

        return savedEpic.getSubTasks();
    }



    // SUBTASK-методы

    /**
     * метод для вывода всех задач SubTusk
     */
    @Override
    public List<SubTask> getSubTasks() {

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
        if (epic == null) {
            throw new NotFoundException("Не найден эпик с id " + subTask.getEpicId());
        }

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
