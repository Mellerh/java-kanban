package service;

import java.util.ArrayList;
import java.util.List;

import model.Task;

public class InMemoryHistoryManager implements HistoryManager {

    // список для хранения просмотренных задач
    private final List<Task> viewedT = new ArrayList<>();

    /**
     * метод возвращает историю просмотра задач
     */
    @Override
    public List<Task> getHist() {
        return viewedT;
    }

    /**
     * метод добавляет просмотренную задачу в список viewedTasks
     * также метод проверяет заполненность списка и удаляет первый элемент
     */
    @Override
    public void addViewedT(Task task) {

        if (task == null) {
            return;
        }

        if (viewedT.size() >= 10) {
            viewedT.removeFirst();
        }

        viewedT.add(task);
//        System.out.println("Задача: " + task + " добавлена в список");
    }


}
