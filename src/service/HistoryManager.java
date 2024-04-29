package service;

import model.Task;

import java.util.Collection;
import java.util.List;

public interface HistoryManager {

    void addViewedT(Task task);

    List<Task> getHist();
}
