package Converter;

import model.Task;


/*
 * класс отвечает за конвертирование задачи в строку
 */
public class TaskConverter {

    public String toString(Task task) {

        return task.getId() + "," + task.getTaskType() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription() + "," + task.getEpicId();

    }

}
