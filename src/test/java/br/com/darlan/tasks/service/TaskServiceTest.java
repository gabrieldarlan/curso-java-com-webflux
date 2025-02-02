package br.com.darlan.tasks.service;

import br.com.darlan.tasks.model.Task;
import br.com.darlan.tasks.repositories.TaskCustomRepository;
import br.com.darlan.tasks.repositories.TaskRepository;
import br.com.darlan.tasks.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    @InjectMocks
    private TaskService service;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskCustomRepository taskCustomRepository;


    @Test
    void service_mustReturnTask_whenSuccessfully() {
        Task task = TestUtils.buildValidTask();

        when(taskRepository.save(any())).thenReturn(task);

        StepVerifier.create(service.insert(task))
                .then(() -> verify(taskRepository, times(1)).save(any()))
                .expectNext(task)
                .expectComplete();
    }

    @Test
    void service_mustReturnMonoEmpty_whenSuccessfully() {
        StepVerifier.create(service.deleteById(any()))
                .then(() -> verify(taskRepository, times(1)).deleteById(any()))
                .verifyComplete();
    }

    @Test
    void service_mustReturnTaskPage_whenFindPaginated() {
        Task task = TestUtils.buildValidTask();
        when(taskCustomRepository.findPaginated(any(), anyInt(), anyInt())).thenReturn(Page.empty());
        Page<Task> result = service.findPaginated(task, 0, 10);
        assertNotNull(result);
        verify(taskCustomRepository, times(1)).findPaginated(any(), anyInt(), anyInt());
    }

}