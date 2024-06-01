package service;

import service.file.FileBackedTaskManager;
import service.memory.InMemoryHistoryManager;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(getHistoryManager());
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }


}
