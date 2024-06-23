package br.com.darlan.tasks.controller.converter;

import br.com.darlan.tasks.controller.dto.TaskDTO;
import br.com.darlan.tasks.model.Task;
import br.com.darlan.tasks.model.TaskState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TaskDTOConverter {

    public TaskDTO convert(Task task) {
        return Optional.ofNullable(task)
                .map(source -> {
                    TaskDTO dto = new TaskDTO();
                    dto.setId(source.getId());
                    dto.setTitle(source.getTitle());
                    dto.setDescription(source.getDescription());
                    dto.setState(source.getState());
                    dto.setPriority(source.getPriority());
                    dto.setAddress(source.getAddress());
                    dto.setCreated(source.getCreated());
                    return dto;
                })
                .orElse(null);
    }

    public Task convert(TaskDTO taskDTO) {
        return Optional.ofNullable(taskDTO)
                .map(source -> Task.builder()
                        .withTitle(source.getTitle())
                        .withDescription(source.getDescription())
                        .withPriority(source.getPriority())
                        .withState(source.getState())
                        .build()
                ).orElse(null);
    }

    public Task convert(String id, String title, String description, int priority, TaskState taskState) {
        return Task.builder()
                .withId(id)
                .withTitle(title)
                .withDescription(description)
                .withPriority(priority)
                .withState(taskState)
                .build();
    }

    public List<TaskDTO> convertList(List<Task> list) {
        return Optional.ofNullable(list)
                .map(array -> array.stream().map(this::convert).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }


}


