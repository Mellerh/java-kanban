package service;

import model.Task;

import java.util.List;

public interface HistoryManager {

    void addViewedT(Task task);

    List<Task> getHistory();

    void removeTaskFromViewed(int id);
}
