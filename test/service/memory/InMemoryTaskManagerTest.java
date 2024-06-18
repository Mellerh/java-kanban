package service.memory;

import org.junit.jupiter.api.DisplayName;
import service.TaskManagerTest;

@DisplayName("InMemoryTaskManagerTest")
class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createManager() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }

}