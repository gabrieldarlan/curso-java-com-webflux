package br.com.darlan.tasks.controller;

import br.com.darlan.tasks.controller.converter.TaskDTOConverter;
import br.com.darlan.tasks.controller.converter.TaskInsertDTOConverter;
import br.com.darlan.tasks.controller.dto.TaskDTO;
import br.com.darlan.tasks.controller.dto.TaskInsertDTO;
import br.com.darlan.tasks.model.TaskState;
import br.com.darlan.tasks.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/task")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final TaskService service;
    private final TaskDTOConverter converter;
    private final TaskInsertDTOConverter taskInsertDTOConverter;

    public TaskController(TaskService service, TaskDTOConverter converter, TaskInsertDTOConverter taskInsertDTOConverter) {
        this.service = service;
        this.converter = converter;
        this.taskInsertDTOConverter = taskInsertDTOConverter;
    }

    @GetMapping
    public Page<TaskDTO> findPaginated(@RequestParam(required = false) String id,
                                       @RequestParam(required = false) String title,
                                       @RequestParam(required = false) String description,
                                       @RequestParam(required = false, defaultValue = "0") int priority,
                                       @RequestParam(required = false) TaskState taskState,
                                       @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        return service.findPaginated(converter.convert(id, title, description, priority, taskState), pageNumber, pageSize)
                .map(converter::convert);
    }

    @PostMapping
    public Mono<TaskDTO> createTask(@RequestBody TaskInsertDTO taskInsertDTO) {
        return service
                .insert(taskInsertDTOConverter.convert(taskInsertDTO))
                .doOnNext(task -> LOGGER.info("Saved task with id {}", task.getId()))
                .map(converter::convert);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return Mono.just(id)
                .doOnNext(it -> LOGGER.info("Deleting task with id {}", id))
                .flatMap(service::deleteById);
    }

}
