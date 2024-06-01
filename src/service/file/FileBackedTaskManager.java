package service.file;

import model.Task;
import service.HistoryManager;
import service.memory.InMemoryTaskManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path file;


    public FileBackedTaskManager(HistoryManager historyManager) {
        this(historyManager, Paths.get("resources/task.csv"));
    }


    public FileBackedTaskManager(HistoryManager inMemoryHistoryManager, Path file) {
        super(inMemoryHistoryManager);
        this.file = file;
    }


    public Task fromString(String value) {


    }


    public void save() {
    }


}
