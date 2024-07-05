package dao.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ValidationException;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public SubtasksHandler(TaskManager taskManager, Gson gson) {
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
                        handleGetSubtaskById(exchange, pathParts[2]);
                    } else if (pathParts.length == 2) {
                        handleGetAllSubtasks(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                case "POST":
                    handlePostSubtask(exchange);
                    break;

                case "DELETE":
                    if (pathParts.length == 3) {
                        handleDeleteSubtaskById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                default:
                    sendText(exchange, "Метод не поддерживается", 400);
                    break;
            }
        } catch (Exception e) {
            sendServerError(exchange, e.getMessage());
        }
    }

    private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(taskManager.getSubTasks());
            sendText(exchange, response, 200);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }

    }

    private void handleGetSubtaskById(HttpExchange exchange, String taskIdStr) throws IOException {
        try {
            int taskId = Integer.parseInt(taskIdStr);
            SubTask task = taskManager.getSubTaskById(taskId);
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

    private void handlePostSubtask(HttpExchange exchange) throws IOException, ValidationException {

        try {
            InputStreamReader inputReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            SubTask task = gson.fromJson(inputReader, SubTask.class);
            taskManager.createSubTask(task);
            sendText(exchange, "Задача добавлена", 201);

        } catch (Exception e) {
            e.printStackTrace();
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange, String taskIdStr) throws IOException {
        try {
            int taskId = Integer.parseInt(taskIdStr);
            taskManager.removeSubTaskById(taskId);
            sendText(exchange, "Задача удалена", 204);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }
    }

}
