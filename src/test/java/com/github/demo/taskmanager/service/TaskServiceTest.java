package com.github.demo.taskmanager.service;

import com.github.demo.taskmanager.model.Task;
import com.github.demo.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        Task task = new Task(null, "Title", "Desc", "pending", null, LocalDateTime.now().plusDays(1));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);
        assertNotNull(result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTaskNotFound() {
        when(taskRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask("1", new Task()));
    }
}
