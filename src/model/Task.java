package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected String name;
    private Status status;
    private String description;
    private int id;


    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;


    public Task(int id, String name, Status status, String description, LocalDateTime startTime, long duration) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;

        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
        this.endTime = startTime.plusMinutes(duration);

    }

    public Task(String name, Status status, String description) {
        this.name = name;
        this.status = status;
        this.description = description;

        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(15);
        this.endTime = startTime.plus(duration);

    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;

        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(15);
        this.endTime = startTime.plus(duration);
    }

    public Task(Integer id, String name, Status status, String description) {
    }


    /**
     * этот метод мы переопределяем в Epic и SubTask. Подход ООП
     */
    public TaskType getTaskType() {
        return TaskType.Task;
    }

    /**
     * этот метод мы переопределяем в SubTask. Подход ООП
     */
    public Integer getEpicId() {
        return null;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Task task = (Task) object;
        return id == task.id;
    }

    @Override
    public String toString() {
        return "\nname: " + name + "\n"
                + "status: " + status + "\n"
                + "description: " + description + "\n"
                + "id: " + id + "\n"
                + "epicId: " + getEpicId();
    }
}