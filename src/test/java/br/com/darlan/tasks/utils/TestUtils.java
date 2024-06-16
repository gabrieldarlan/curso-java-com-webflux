package br.com.darlan.tasks.utils;

import br.com.darlan.tasks.controller.dto.TaskDTO;
import br.com.darlan.tasks.model.Task;
import br.com.darlan.tasks.model.TaskState;

public class TestUtils {
    public static Task buildValidTask() {
        return Task.builder()
                .withId("12345")
                .withTitle("title")
                .withDescription("Description")
                .withPriority(1)
                .withState(TaskState.INSERT)
                .build();
    }

    public static TaskDTO buildValidTaskDTO() {
        TaskDTO dto = new TaskDTO();
        dto.setId("123");
        dto.setTitle("Title");
        dto.setPriority(1);
        dto.setState(TaskState.INSERT);
        dto.setDescription("Description");
        return dto;
    }
}
