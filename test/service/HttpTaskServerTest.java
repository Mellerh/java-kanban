package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dao.adapters.DurationAdapter;
import dao.adapters.LocalDateTimeAdapter;
import exception.ValidationException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {

    public final static int PORT = 8080;
    private HttpTaskServer server;
    private TaskManager taskManager;
    private final Gson gson = getGson();

    private Task task;
    private Epic epic;
    private SubTask subTask;

    private List<Task> tasks;
    private List<Epic> epics;
    private List<SubTask> subTasks;


    @BeforeEach
    void init() throws IOException, ValidationException {

        taskManager = Managers.getDefault();
        server = new HttpTaskServer(taskManager);

        task = taskManager.createTask(new Task("task1", Status.NEW, "descriptionTask1",
                LocalDateTime.now(), 15L));
        epic = taskManager.createEpic(new Epic("epic1", "descriptionEpic1"));
        subTask = taskManager.createSubTask(new SubTask(epic.getId(), "subTask1Epic1",
                Status.NEW, "descriptionSubTask1Epic1", LocalDateTime.parse("2026-12-21T21:00:00"),
                15L));
    }

    @DisplayName("Запускаем сервер")
    @BeforeEach
    void start() {
        System.out.println("Starting server on port " + PORT);
        server.start();
    }

    @DisplayName("Останавливаем сервер")
    @AfterEach
    void stop() {
        server.stop();
        System.out.println("Server stopped");
    }


    @DisplayName("Тест проверят корректность работы сервера на запрос GET")
    @Test
    void shouldGetTaskList() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        try (client) {
            try {

                URI uriTasks = URI.create("http://localhost:8080/tasks");
                HttpRequest requestTasks = HttpRequest.newBuilder().uri(uriTasks).GET().build();

                HttpResponse<String> responseTasks = client.send(requestTasks, HttpResponse.BodyHandlers.ofString());


                assertEquals(200, responseTasks.statusCode());

                // сравниваем списки задач
                List<Task> taskList = gson.fromJson(responseTasks.body(), new TypeToken<ArrayList<Task>>() {
                }.getType());
                assertEqualsListOfTasks(taskManager.getTasks(), taskList);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }



    }


    @DisplayName("Тест проверят корректность работы сервера на запрос POST")
    @Test
    void shouldPostTask() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        try (client) {

            try {
                URI uriTasks = URI.create("http://localhost:8080/tasks");

                // ДОБАВЛЯЕМ ЗАДАЧУ ЧЕРЕЗ ПОСТ
                Task taskToPost = new Task(4, "task2", Status.NEW, "descriptionTask2");
                String taskToPostGson = gson.toJson(taskToPost);

                HttpRequest postTask = HttpRequest.newBuilder()
                        .uri(uriTasks)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(taskToPostGson))
                        .build();

                HttpResponse<String> responsePostTask = client.send(postTask, HttpResponse.BodyHandlers.ofString());
                assertEquals(201, responsePostTask.statusCode(), "неккоректный код ответа");


                // ПРОВЕРЯЕМ, ЧТО ЗАДАЧА ДОБАВИЛАСЬ
                HttpRequest getTasks = HttpRequest.newBuilder().uri(uriTasks).GET().build();
                HttpResponse<String> responseTasks = client.send(getTasks, HttpResponse.BodyHandlers.ofString());

                // сравниваем списки задач
                List<Task> taskList = gson.fromJson(responseTasks.body(), new TypeToken<ArrayList<Task>>() {
                }.getType());

                assertEqualsListOfTasks(taskManager.getTasks(), taskList);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @DisplayName("Тест проверят корректность работы сервера на запрос DELETE")
    @Test
    void shouldDeleteTask() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        try (client) {
            try {
                URI uriTasks = URI.create("http://localhost:8080/tasks/1");

                taskManager.createTask(new Task("task3", Status.NEW, "descriptionTask2"));

                // УДАЛЯЕМ ЗАДАЧУ
                HttpRequest deleteTask = HttpRequest.newBuilder()
                        .uri(uriTasks)
                        .DELETE()
                        .build();

                HttpResponse<String> responsePostTask = client.send(deleteTask, HttpResponse.BodyHandlers.ofString());
                assertEquals(204, responsePostTask.statusCode(), "неккоректный код ответа");


                // ПРОВЕРЯЕМ, ЧТО ЗАДАЧА УДАЛИЛАСЬ
                URI uriAllTasks = URI.create("http://localhost:8080/tasks");
                HttpRequest getTasks = HttpRequest.newBuilder().uri(uriAllTasks).GET().build();
                HttpResponse<String> responseTasks = client.send(getTasks, HttpResponse.BodyHandlers.ofString());

                // сравниваем списки задач
                List<Task> taskList = gson.fromJson(responseTasks.body(), new TypeToken<ArrayList<Task>>() {
                }.getType());

                assertEquals(taskManager.getTasks().size(), taskList.size());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * метод создан для создания Gson
     */
    private static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

    /**
     * метод создан для корректного сравнения задач
     */
    private static void assertEqualsTask(Task expectedTask, Task actualTask, String description) {
        assertEquals(expectedTask.getId(), actualTask.getId(), description + " id");
        assertEquals(expectedTask.getName(), actualTask.getName(), description + " name");
    }


    /**
     * переопределяем метод для корректного сравнения полей задач из списокв
     */
    private static <T extends Task> void assertEqualsListOfTasks(List<T> expectedTaskList, List<T> actualTaskList) {
        assertEquals(expectedTaskList.size(), actualTaskList.size(), "Размеры списков не ранвы");

        for (int i = 0; i < expectedTaskList.size(); i++) {
            assertEquals(expectedTaskList.get(i).getId(), actualTaskList.get(i).getId(), "id задач " +
                    "не совпадают");
            assertEquals(expectedTaskList.get(i).getName(), actualTaskList.get(i).getName(), "имена задач " +
                    "не совпадают");
        }

    }
}