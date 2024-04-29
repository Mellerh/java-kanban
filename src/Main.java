import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();


        Task task = taskManager.createTask(new Task("Задача1", Status.NEW, "описание задачи1"));

        Task taskFromManager = taskManager.getTaskById(task.getId());
//        System.out.println("Get task: " + taskFromManager);
//        System.out.println();

        Task taskUpdated = new Task(taskFromManager.getId(), "Задача1", Status.DONE,
                "новое описание задачи1");
        taskManager.upDateTask(taskUpdated);
//        System.out.println("Update task: " + taskUpdated);
//        System.out.println();


        Epic epic = taskManager.createEpic(new Epic("Эпик1", "описание эпика1"));
//        System.out.println("Create epic: " + epic);
//        System.out.println();


        SubTask subTask = taskManager.createSubTask(new SubTask(epic.getId(), "сабтаск1 эпика1",
                Status.DONE, "описание сабтаска1 эпика1"));
        SubTask subTask1 = taskManager.createSubTask(new SubTask(epic.getId(), "сабтаск2 эпика1",
                Status.DONE, "описание сабтаска2 эпика1"));


        printAllTasks(taskManager);
    }


    // метод для вывода все просмотренных задач
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }

        System.out.println();

        System.out.print("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task subTask : manager.getSubTasks()) {
                System.out.println("--> " + subTask);
            }
        }

        System.out.println();

        System.out.print("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println();

        System.out.print("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
