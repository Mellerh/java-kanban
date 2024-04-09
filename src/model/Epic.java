package model;

import java.util.ArrayList;

public class Epic extends Task {

    // список для хранения сабТасков эпика
    private final ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, Status status, String description) {
        super(name, status, description);
    }

    public ArrayList<SubTask> getSubTasks() {
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
        /*for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).equals(subTask)) {
                subTasks.set(i, subTask);
                return;
            }
        }*/
        if (subTasks.contains(subTask)) {
            subTasks.set(subTasks.indexOf(subTask), subTask);
        }
        updateStatus();
    }

    /**
     * метод высчитывает статус Эпика на основании статусов всех его субтасков
     */
    public void updateStatus() {
        boolean AllNew = true;
        boolean AllDone = true;

        if (subTasks.isEmpty()) {
            this.setStatus(Status.NEW);
            return;
        }

        for (SubTask subTask : subTasks) {
            // проверка, что все субтакси Status.NEW
            if (subTask.getStatus() != Status.NEW) {
                AllNew = false;
            }

            // проверка, что все субтакси Status.DONE
            if (subTask.getStatus() != Status.DONE) {
                AllDone = false;
            }
        }

        if (AllNew) {
            this.setStatus(Status.NEW);
        } else if (AllDone) {
            this.setStatus(Status.DONE);
        } else {
            this.setStatus(Status.IN_PROGRESS);
        }


    }
}

