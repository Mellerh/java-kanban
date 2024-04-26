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

        Task task1 = taskManager.createTask(new Task("Машина1", Status.NEW, "нужно купить машину1"));

        Task taskFromManager = taskManager.getTaskById(task1.getId());

        taskFromManager.setDescription("HAVE TO BUY A NEW CAR");
        taskManager.upDateTask(taskFromManager);

        taskManager.removeTaskById(taskFromManager.getId());

        Epic epic = taskManager.createEpic(new Epic("Эпик1", "описание эпика1"));
        SubTask subTask = taskManager.createSubTask(new SubTask(epic.getId(), "сабтаск1 эпика1",
                Status.DONE, "описание сабтаска1 эпика1"));

        Epic epic1 = taskManager.getEpicById(epic.getId());

        SubTask subTask1 = taskManager.createSubTask(new SubTask(epic.getId(), "сабтаск2 эпика1",
                Status.DONE, "описание сабтаска2 эпика1"));

        System.out.println(epic1.getSubTasks());


        printAllTasks(taskManager);
    }


    // метод для вывода все просмотренных задач
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task subTask : manager.getSubTasks()) {
                System.out.println("--> " + subTask);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
