package com.github.demo.taskmanager.service;

import com.github.demo.taskmanager.model.Task;
import com.github.demo.taskmanager.model.TaskDto;
import com.github.demo.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repository;

    @Autowired
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskDto createTask(TaskDto taskDto) {
        Task task = toEntity(taskDto);
        task.setCreatedAt(LocalDateTime.now());
        return toDto(repository.save(task));
    }

    public List<TaskDto> getAllTasks() {
        return toDtoList(repository.findAll());
    }

    public Optional<TaskDto> getTaskById(String id) {
        return toDto(repository.findById(id));
    }

    public TaskDto updateTask(String id, TaskDto updatedTask) {
        return repository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());
            task.setDueDate(updatedTask.getDueDate());
            return toDto(repository.save(task));
        }).orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public void deleteTask(String id) {
        repository.deleteById(id);
    }

    public TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .dueDate(task.getDueDate())
                .build();
    }

    public List<TaskDto> toDtoList(List<Task> tasks) {
        return tasks.stream().map(this::toDto).toList();
    }

    public Optional<TaskDto> toDto(Optional<Task> taskOptional) {
        TaskDto taskDto = null;
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            taskDto = TaskDto.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(task.getStatus())
                    .createdAt(task.getCreatedAt())
                    .dueDate(task.getDueDate())
                    .build();
        }
        return Optional.ofNullable(taskDto);
    }

    public Task toEntity(TaskDto taskDto) {
        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .createdAt(taskDto.getCreatedAt())
                .dueDate(taskDto.getDueDate())
                .build();
    }
}
