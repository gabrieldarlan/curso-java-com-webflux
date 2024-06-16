package br.com.darlan.tasks.controller.converter;

import br.com.darlan.tasks.controller.dto.TaskDTO;
import br.com.darlan.tasks.model.Task;
import br.com.darlan.tasks.model.TaskState;
import br.com.darlan.tasks.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaskDTOConverterTest {

    @InjectMocks
    private TaskDTOConverter converter;

    @Test
    void converter_mustReturnTaskDto_whenInputTask() {
        Task task = TestUtils.buildValidTask();

        TaskDTO dto = converter.convert(task);

        assertEquals(dto.getId(), task.getId());
        assertEquals(dto.getDescription(), task.getDescription());
        assertEquals(dto.getTitle(), task.getTitle());
        assertEquals(dto.getPriority(), task.getPriority());
        assertEquals(dto.getState(), task.getState());
    }

    @Test
    void converter_mustReturnTask_whenInputTaskDTO() {

        TaskDTO dto = TestUtils.buildValidTaskDTO();

        Task result = converter.convert(dto);

        assertEquals(result.getTitle(), dto.getTitle());
        assertEquals(result.getDescription(), dto.getDescription());
        assertEquals(result.getState(), dto.getState());
        assertEquals(result.getPriority(), dto.getPriority());
    }

    @Test
    void converter_mustReturnTask_whenInputRequestParamsOfPagination() {
        String id = "12345";
        String title = "title";
        String description = "Description";
        int priority = 1;
        TaskState taskState = TaskState.INSERT;

        Task result = converter.convert(id, title, description, priority, taskState);

        assertEquals(result.getId(), id);
        assertEquals(result.getTitle(), title);
        assertEquals(result.getDescription(), description);
        assertEquals(result.getState(), taskState);
        assertEquals(result.getPriority(), priority);


    }


}