package service.memory;

import model.Task;
import service.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {


    Map<Integer, Node> history = new HashMap<>();
    Node first;
    Node last;

    private static class Node {
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Node next, Task item) {
            this.prev = prev;
            this.next = next;
            this.item = item;
        }
    }

    /**
     * метод добавляет просмотренную задачу в список viewedTasks
     */
    @Override
    public void addViewedT(Task task) {

        if (task == null) {
            return;
        }

        Node node = history.get(task.getId());

        if (node != null) {
            removeNode(node);
        }

        linkLast(task);
    }


    /**
     * метод возвращает историю просмотра задач
     */
    @Override
    public List<Task> getHistory() {
        List<Task> list = new ArrayList<>();
        Node current = first;

        while (current != null) {
            list.add(current.item);
            current = current.next;
        }

        return list;
    }

    /**
     * метод удаляет задачу из списка просмотренных
     */
    public void removeTaskFromViewed(int id) {
        Node node = history.get(id);

        if (node != null) {
            removeNode(node);
        }
    }


    private void linkLast(Task task) {
        final Node lastNode = this.last;
        final Node newNode = new Node(lastNode, null, task);
        this.last = newNode;
        if (lastNode == null) {
            this.first = newNode;
        } else {
            lastNode.next = newNode;
        }

        history.put(task.getId(), newNode);
    }

    private void removeNode(Node node) {

        final Node prevNode = node.prev;
        final Node nextNode = node.next;

        if (prevNode == null) {
            this.first = nextNode;
        } else {
            prevNode.next = nextNode;
            node.prev = null;
        }

        if (nextNode == null) {
            this.last = prevNode;
        } else {
            nextNode.prev = prevNode;
            node.next = null;
        }

        history.remove(node.item.getId());
    }


}
