package service;

import model.Epic;
import model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void shouldEqualsWithCope() {
        Epic epic = new Epic("name", Status.NEW, "desc");

    }

}