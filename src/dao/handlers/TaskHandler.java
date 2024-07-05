package dao.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ValidationException;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        try {
            switch (method) {
                case "GET":
                    if (pathParts.length == 3) {
                        handleGetTaskById(exchange, pathParts[2]);
                    } else if (pathParts.length == 2) {
                        handleGetAllTasks(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                case "POST":
                    handlePostTask(exchange);
                    break;

                case "DELETE":
                    if (pathParts.length == 3) {
                        handleDeleteTaskById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                default:
                    sendText(exchange, "Метод не поддерживается", 404);
                    break;
            }
        } catch (Exception e) {
            sendServerError(exchange, e.getMessage());
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(taskManager.getTasks());
            sendText(exchange, response, 200);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }

    }

    private void handleGetTaskById(HttpExchange exchange, String taskIdStr) throws IOException {
        try {
            int taskId = Integer.parseInt(taskIdStr);
            Task task = taskManager.getTaskById(taskId);
            if (task == null) {
                sendNotFound(exchange);

            } else {
                String response = gson.toJson(task);
                sendText(exchange, response, 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException, ValidationException {

        try {
            InputStreamReader inputReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(inputReader, Task.class);
            taskManager.createTask(task);
            sendText(exchange, "Задача добавлена", 201);

        } catch (Exception e) {
            e.printStackTrace();
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteTaskById(HttpExchange exchange, String taskIdStr) throws IOException {
        try {
            int taskId = Integer.parseInt(taskIdStr);
            taskManager.removeTaskById(taskId);
            sendText(exchange, "Задача удалена", 204);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }
    }
}
