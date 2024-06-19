package model;

import java.time.LocalDateTime;
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

    // конструктор сработает при создании эпика из Файла
    public Epic(Integer id, String name, Status status, String description, LocalDateTime startTime, Long duration) {
        super(id, name, status, description, startTime, duration);
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

        // поля для расчёта времени начала, продолжительности и окончания эпика
        LocalDateTime startTimeOfSubTasks = subTasks.getFirst().getStartTime();
        long durationOfSubTasks = 0;

        LocalDateTime endTimeOfSubTasks = subTasks.getFirst().getEndTime();

        for (SubTask subTask : subTasks) {

            // устанавливаем Status для Epic
            if (subTask.getStatus() != statusOfSubTasks) {
                isDifferentStatus = true;
            }

            // Рассчитываем время начала эпика, если startTime у subTask не null и меньше текущего времени начала эпика
            if (subTask.getStartTime() != null &&
                    (startTimeOfSubTasks == null || subTask.getStartTime().isBefore(startTimeOfSubTasks))) {
                startTimeOfSubTasks = subTask.getStartTime();
            }

            // Рассчитываем время окончания эпика, если endTime у subTask не null и больше текущего времени окончания эпика
            if (subTask.getEndTime() != null &&
                    (endTimeOfSubTasks == null || subTask.getEndTime().isAfter(endTimeOfSubTasks))) {
                endTimeOfSubTasks = subTask.getEndTime();
            }

            // Увеличиваем продолжительность эпика на продолжительность текущей задачи
            if (subTask.getDuration() != null) {
                durationOfSubTasks += subTask.getDuration().toMinutes();
            }

        }

        setStartTime(startTimeOfSubTasks);
        endTime = endTimeOfSubTasks;
        setDuration(durationOfSubTasks);

        if (isDifferentStatus) {
            this.setStatus(Status.IN_PROGRESS);
        } else {
            this.setStatus(statusOfSubTasks);
        }

    }
}

