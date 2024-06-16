package br.com.darlan.tasks.service;

import br.com.darlan.tasks.model.Task;
import br.com.darlan.tasks.repositories.TaskCustomRepository;
import br.com.darlan.tasks.repositories.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final TaskCustomRepository taskCustomRepository;

    public TaskService(TaskRepository taskRepository, TaskCustomRepository taskCustomRepository) {
        this.taskRepository = taskRepository;
        this.taskCustomRepository = taskCustomRepository;
    }

    public Mono<Task> insert(Task task) {
        return Mono.just(task)
                .map(Task::insert)
                .flatMap(this::save)
//                .flatMap(it -> doError())
                .doOnError(error -> LOGGER.error("Error during save task. Title: {}", task.getTitle(), error));
    }

    public Page<Task> findPaginated(Task task, Integer pageNumber, Integer pageSize) {
        return taskCustomRepository.findPaginated(task, pageNumber, pageSize);
    }

    private Mono<Task> save(Task task) {
        return Mono.just(task)
                .doOnNext(t -> LOGGER.info("Saving with title {}", t.getTitle()))
                .map(taskRepository::save);
    }

    public Mono<Void> deleteById(String id) {
        //! Quando o retorno da função é void usamos o Mono.fromRunnable
        return Mono.fromRunnable(() -> taskRepository.deleteById(id));

    }
}
