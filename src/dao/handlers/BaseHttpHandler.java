package dao.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler {

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {

        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.getResponseBody().close();


    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendText(exchange, "Не найдено", 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        sendText(exchange, "Нельяза добавить или обновить", 406);
    }

    protected void sendServerError(HttpExchange exchange, String errorMessage) throws IOException {
        sendText(exchange, errorMessage, 500);
    }
}
