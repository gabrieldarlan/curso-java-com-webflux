package br.com.darlan.tasks.service;

import br.com.darlan.tasks.exception.TaskNotFoundException;
import br.com.darlan.tasks.message.TaskNotificationProducer;
import br.com.darlan.tasks.model.Address;
import br.com.darlan.tasks.model.Task;
import br.com.darlan.tasks.repositories.TaskCustomRepository;
import br.com.darlan.tasks.repositories.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository repository;
    private final TaskCustomRepository taskCustomRepository;
    private final AdressService adressService;
    private final TaskNotificationProducer producer;


    public TaskService(TaskRepository repository, TaskCustomRepository taskCustomRepository, AdressService adressService, TaskNotificationProducer producer) {
        this.repository = repository;
        this.taskCustomRepository = taskCustomRepository;
        this.adressService = adressService;
        this.producer = producer;
    }

    public Mono<Task> insert(Task task) {
        return Mono.just(task)
                .map(Task::insert)
                .flatMap(this::save)
                .doOnError(error -> LOGGER.error("Error during save task. Title: {}", task.getTitle(), error));
    }

    public Mono<Page<Task>> findPaginated(Task task, Integer pageNumber, Integer pageSize) {
        return taskCustomRepository.findPaginated(task, pageNumber, pageSize);
    }

    private Mono<Task> updateAddress(Task task, Address address) {
        return Mono.just(task)
                .map(it -> task.updateAddress(address));
    }

    public Mono<Task> start(String id, String zipcode) {
        return repository.findById(id)
                .zipWhen(it -> adressService.getAddress(zipcode))
                .flatMap(it -> updateAddress(it.getT1(), it.getT2()))
                .map(Task::start)
                .flatMap(repository::save)
                .flatMap(producer::sendNotification)
                .switchIfEmpty(Mono.error(TaskNotFoundException::new))
                .doOnError(error -> LOGGER.error("Error on start task. ID: {}", id, error));
    }

    private Mono<Task> save(Task task) {
        return Mono.just(task)
                .doOnNext(t -> LOGGER.info("Saving with title {}", t.getTitle()))
                .flatMap(repository::save);
    }

    public Mono<Void> deleteById(String id) {
        //! Quando o retorno da função é void usamos o Mono.fromRunnable
//        return Mono.fromRunnable(() -> taskRepository.deleteById(id));
        return repository.deleteById(id);

    }

    public Mono<Task> update(Task task) {
        return repository.findById(task.getId())
                .map(task::update)
                .flatMap(repository::save)
                .switchIfEmpty(Mono.error(TaskNotFoundException::new))
                .doOnError(error -> LOGGER.error("Error during update task with id: {}. Message: {}", task.getId(), error.getMessage()));
    }

    public Mono<Task> done(Task task) {
        return Mono.just(task)
                .doOnNext(it -> LOGGER.info("Finishing task. ID: {}", task.getId()))
                .map(Task::done)
                .flatMap(repository::save);

    }

    public Flux<Task> refreshCreated() {
        return repository.findAll()
                .filter(Task::createdIsEmpty)
                .map(Task::createdNow)
                .flatMap(repository::save);
    }

    public Mono<List<Task>> doneMany(List<String> ids) {
        return Flux.fromIterable(ids)
                .flatMap(repository::findById)
                .map(Task::done)
                .flatMap(repository::save)
                .doOnNext(it -> LOGGER.info("Done task. ID: {}", it.getId()))
                .collectList();
    }
}
