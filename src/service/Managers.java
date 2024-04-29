package service;

public class Managers {

    public static TaskManager getDefault(int maxViewedTasks) {
        return new InMemoryTaskManager(new InMemoryHistoryManager(maxViewedTasks));
    }

}
