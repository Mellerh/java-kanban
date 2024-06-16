package model;

import java.time.LocalDateTime;

public class SubTask extends Task {

    // id эпика, в котором хранится этот subTask
    private int epicId;


    public SubTask(String name, Status status, String description) {
        super(name, status, description);
    }

    public SubTask(int epicId, String name, Status status, String description) {
        super(name, status, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, Status status, String description, int epicId, LocalDateTime localDateTime,
                   Long l) {
        super(id, name, status, description, localDateTime, l);
        this.epicId = epicId;
    }

    public SubTask(int epicId, String name, Status status, String description, LocalDateTime localDateTime,
                   Long duration) {
        super(name, status, description, localDateTime, duration);
        this.epicId = epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SubTask;
    }


    /**
     * данный метод - это пример применения ООП-инкапсуляции.
     */
    @Override
    public Integer getEpicId() {
        return this.epicId;
    }

    public void setEpicId(Epic epic) {
        this.epicId = epic.getId();
    }

}
