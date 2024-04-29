import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();


        Task task = taskManager.createTask(new Task("Машина", Status.NEW, "нужно купить машину"));

        Task taskFromManager = taskManager.getTaskById(task.getId());
//        System.out.println("Get task: " + taskFromManager);
//        System.out.println();

        Task taskUpdated = new Task(taskFromManager.getId(), "Машина", Status.DONE, "машина куплена");
        taskManager.upDateTask(taskUpdated);
//        System.out.println("Update task: " + taskUpdated);
//        System.out.println();

        taskManager.removeTaskById(taskUpdated.getId());
//        System.out.println("Delete: " + task);
//        System.out.println();

        Epic epic = taskManager.createEpic(new Epic("Эпик1", "описание эпика1"));
//        System.out.println("Create epic: " + epic);
//        System.out.println();


        SubTask subTask = taskManager.createSubTask(new SubTask(epic.getId(), "сабтаск1 эпика1",
                Status.DONE, "описание сабтаска1 эпика1"));
        SubTask subTask1 = taskManager.createSubTask(new SubTask(epic.getId(), "сабтаск2 эпика1",
                Status.DONE, "описание сабтаска2 эпика1"));


        /*taskManager.getEpicById(epic.getId());
        taskManager.getSubTaskById(subTask.getId());
        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getEpicById(epic.getId());
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(taskManager.getHistory());*/


        printAllTasks(taskManager);
    }


    // метод для вывода все просмотренных задач
    private static void printAllTasks(TaskManager manager) {
        System.out.print("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.print("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task subTask : manager.getSubTasks()) {
                System.out.println("--> " + subTask);
            }
        }
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
