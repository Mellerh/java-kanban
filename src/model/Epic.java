package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    // список для хранения сабТасков эпика
    private final List<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, Status status, String description) {
        super(name, status, description);
    }

    public Epic(Integer id, String name, Status status, String description) {
        super(id, name, status, description);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.Epic;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void removeAllSubTasks() {
        subTasks.clear();
        updateStatus();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        updateStatus();
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
        updateStatus();
    }

    /**
     * метод обновления subTask в листе subTasks
     */
    public void updateSubTask(SubTask subTask) {

        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).equals(subTask)) {
                subTasks.set(i, subTask);
                return;
            }
        }

        updateStatus();
    }

    /**
     * метод высчитывает статус Эпика на основании статусов всех его субтасков
     */
    public void updateStatus() {

        boolean isDifferentStatus = false;

        if (subTasks.isEmpty()) {
            this.setStatus(Status.NEW);
            return;
        }

        Status statusOfSubTasks = subTasks.getFirst().getStatus();

        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() != statusOfSubTasks) {
                isDifferentStatus = true;
                break;
            }
        }

        if (isDifferentStatus) {
            this.setStatus(Status.IN_PROGRESS);
        } else {
            this.setStatus(statusOfSubTasks);
        }

    }
}

