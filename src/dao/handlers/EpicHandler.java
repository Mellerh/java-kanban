package dao.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ValidationException;
import model.Epic;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
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

                    if (pathParts.length == 4) {
                        handleGetAllSubTasksInEpic(exchange, pathParts[2]);
                    } else if (pathParts.length == 3) {
                        handleGetEpicById(exchange, pathParts[2]);
                    } else if (pathParts.length == 2) {
                        handleGetAllEpic(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                case "POST":
                    handlePostEpic(exchange);
                    break;

                case "DELETE":
                    if (pathParts.length == 3) {
                        handleDeleteEpicById(exchange, pathParts[2]);
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

    /**
     * возвращаем все сабТаски Эпика
     */
    private void handleGetAllSubTasksInEpic(HttpExchange exchange, String taskIdStr) throws IOException {
        try {
            int taskId = Integer.parseInt(taskIdStr);
            Epic task = taskManager.getEpicById(taskId);
            List<SubTask> allSubTasksInEpic = taskManager.getAllSubTaskInEpic(task);

            if (task == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(allSubTasksInEpic);
                sendText(exchange, response, 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }
    }


    private void handleGetAllEpic(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(taskManager.getEpics());
            sendText(exchange, response, 200);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }

    }

    private void handleGetEpicById(HttpExchange exchange, String taskIdStr) throws IOException {
        try {
            int taskId = Integer.parseInt(taskIdStr);
            Epic task = taskManager.getEpicById(taskId);
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

    private void handlePostEpic(HttpExchange exchange) throws IOException, ValidationException {

        try {
            InputStreamReader inputReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            Epic task = gson.fromJson(inputReader, Epic.class);
            taskManager.createEpic(task);
            sendText(exchange, "Задача добавлена", 201);

        } catch (Exception e) {
            e.printStackTrace();
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteEpicById(HttpExchange exchange, String taskIdStr) throws IOException {
        try {
            int taskId = Integer.parseInt(taskIdStr);
            taskManager.removeEpicById(taskId);
            sendText(exchange, "Задача удалена", 204);

        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }
    }

}
