package com.github.demo.taskmanager.service;

import com.github.demo.taskmanager.model.Task;
import com.github.demo.taskmanager.model.TaskDto;
import com.github.demo.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = List.of(Task.builder().id("1").title("Read Book").createdAt(LocalDateTime.now()).build(),
                Task.builder().id("2").title("Write Code").createdAt(LocalDateTime.now()).build());

        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskDto> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    void testCreateTask() {
        Task input =
                Task.builder().id("3").title("New Task").description("For Test Purpose").status("NEW").dueDate(LocalDateTime.now()).createdAt(LocalDateTime.now()).build();
        Task saved = Task.builder().id("4").title("New Task").createdAt(LocalDateTime.now()).build();

        when(taskRepository.save(input)).thenReturn(saved);

        TaskDto inputDto =
                TaskDto.builder().id("3").title("New Task").description("For Test Purpose").status("NEW").dueDate(LocalDateTime.now()).createdAt(LocalDateTime.now()).build();
        TaskDto result = taskService.createTask(inputDto);

        assertNotNull(result.getId());
        assertEquals("New Task", result.getTitle());
        verify(taskRepository).save(input);
    }

    @Test
    void testUpdateTask_Success() {
        Task existing = Task.builder().id("1").title("Old Task").createdAt(LocalDateTime.now()).build();
        TaskDto update = TaskDto.builder().title("Updated Task").createdAt(LocalDateTime.now()).build();

        when(taskRepository.findById("1")).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskDto result = taskService.updateTask("1", update);

        assertEquals("Updated Task", result.getTitle());

        verify(taskRepository).findById("1");
        verify(taskRepository).save(existing);
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById("1");

        taskService.deleteTask("1");

        verify(taskRepository).deleteById("1");
    }
}
