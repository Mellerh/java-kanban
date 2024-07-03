package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected String name;
    private Status status;
    private String description;
    private Integer id;


    private LocalDateTime startTime;
    private Duration duration;
    protected LocalDateTime endTime;


    public Task(int id, String name, Status status, String description, LocalDateTime startTime, Long duration) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;

        this.startTime = startTime;
        this.duration = duration == null ? null : Duration.ofMinutes(duration);
        this.endTime = duration == null ? null : startTime.plusMinutes(duration);

    }

    public Task(String name, Status status, String description) {
        this.name = name;
        this.status = status;
        this.description = description;

        this.startTime = null;
        this.duration = null;
        this.endTime = null;

    }

    // для SubTask с startTime и duration
    public Task(String name, Status status, String description, LocalDateTime startTime, Long duration) {
        this.name = name;
        this.status = status;
        this.description = description;

        this.startTime = startTime;
        this.duration = duration == null ? null : Duration.ofMinutes(duration);
        this.endTime = duration == null ? null : startTime.plusMinutes(duration);

    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;

        this.startTime = null;
        this.duration = null;
        this.endTime = null;
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
        if (this.startTime != null && this.duration != null) {
            this.endTime = this.startTime.plus(this.duration);
        } else {
            this.endTime = null;
        }

    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        if (duration != null) {
            this.duration = Duration.ofMinutes(duration);
            if (this.startTime != null) {
                this.endTime = this.startTime.plus(this.duration);
            }
        } else {
            this.duration = null;
            this.endTime = null;
        }
    }

    public LocalDateTime getEndTime() {
        return endTime;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

        return "\nname: " + name + "\n"
                + "status: " + status + "\n"
                + "description: " + description + "\n"
                + "id: " + id + "\n"
                + "epicId: " + getEpicId() + "\n"
                + "startTime: " + (getStartTime() == null ? "null" : getStartTime().format(formatter)) + "\n"
                + "duration: " + (getDuration() == null ? "null" : getDuration().toMinutes()) + "\n"
                + "endTime: " + (getEndTime() == null ? "null" : getEndTime().format(formatter));
    }
}
