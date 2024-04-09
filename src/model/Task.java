package model;

import java.util.Objects;

public class Task {

    protected String name;
    private Status status;
    private String description;
    private int id;


    public Task(int id, String name, Status status, String description) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(String name, Status status, String description) {
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
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
                + "id: " + id;
    }
}