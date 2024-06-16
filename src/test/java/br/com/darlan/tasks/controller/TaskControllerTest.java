package br.com.darlan.tasks.controller;

import br.com.darlan.tasks.controller.converter.TaskDTOConverter;
import br.com.darlan.tasks.controller.converter.TaskInsertDTOConverter;
import br.com.darlan.tasks.controller.dto.TaskDTO;
import br.com.darlan.tasks.controller.dto.TaskInsertDTO;
import br.com.darlan.tasks.model.Task;
import br.com.darlan.tasks.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskControllerTest {

    @InjectMocks
    private TaskController controller;

    @Mock
    private TaskService service;

    @Mock
    private TaskDTOConverter taskDTOConverter;

    @Mock
    private TaskInsertDTOConverter taskInsertDTOConverter;

    @Test
    public void controller_must_returnOk_whenSaveSuccess() {

        when(service.insert(any())).thenReturn(Mono.just(new Task()));
        when(taskDTOConverter.convert(any(Task.class))).thenReturn(new TaskDTO());

        WebTestClient client = WebTestClient.bindToController(controller).build();
        client.post()
                .uri("/task")
                .bodyValue(new TaskInsertDTO())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDTO.class);

    }

    @Test
    public void controller_must_returnNoContent_whenDeleteWithSuccess() {
        String taskId = "any-id";

        when(service.deleteById(any())).thenReturn(Mono.empty());
        WebTestClient client = WebTestClient.bindToController(controller).build();

        client.delete()
                .uri("/task/" + taskId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

//    @Test
//    public void contoller_must_returnOk_whenGetPaginationSuccessfully() {
//        when(service.findPaginated(any(), anyInt(), anyInt())).thenReturn(Page.empty());
//
//        WebTestClient client = WebTestClient.bindToController(controller).build();
//
//        client.get()
//                .uri("/task")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(TaskDTO.class);
////                .expectBody().jsonPath("$.content").isNotEmpty();
//    }


}