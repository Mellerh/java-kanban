package service;

import service.file.FileBackedTaskManager;
import service.memory.InMemoryHistoryManager;

import java.nio.file.Path;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(getHistoryManager());
    }

    private static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

    private static TaskManager getDefaultLoader(Path file) {
        return FileBackedTaskManager.loadFromFile(file);
    }


}
