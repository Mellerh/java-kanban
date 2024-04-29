package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ManagersTest")
class ManagersTest {

    @Test
    @DisplayName("Managers.getDefault() не должен возвращать null")
    void managerGetDefaultShouldNotReturnNull() {

        TaskManager taskManagerResult = Managers.getDefault(10);

        assertNotNull(taskManagerResult, "Managers.getDefault() возвращает null");
    }

}