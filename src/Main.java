import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
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
        System.out.println(epic1);
        System.out.println(epic1.getSubTasks());
    }
}
