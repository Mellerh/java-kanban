package service.memory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("ManagersTest")
class ManagersTest {

    @Test
    @DisplayName("Managers.getDefault() не должен возвращать null")
    void managerGetDefaultShouldNotReturnNull() {

        TaskManager taskManagerResult = Managers.getDefault();

        assertNotNull(taskManagerResult, "Managers.getDefault() возвращает null");
    }

}