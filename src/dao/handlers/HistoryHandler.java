package dao.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        try {
            if (method.equals("GET")) {
                if (pathParts.length == 2) {
                    handleGetHistory(exchange);
                } else {
                    sendNotFound(exchange);
                }

            } else {
                sendText(exchange, "Метод не поддерживается", 404);
            }

        } catch (Exception e) {
            sendServerError(exchange, e.getMessage());
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(taskManager.getHistory());
            sendText(exchange, response, 200);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }

    }

}
