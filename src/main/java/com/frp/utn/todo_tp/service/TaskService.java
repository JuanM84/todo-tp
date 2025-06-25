package com.frp.utn.todo_tp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.frp.utn.todo_tp.model.Task;
import com.frp.utn.todo_tp.repository.TaskRepository;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public void deleteAll(){
        taskRepository.deleteAll();
    }
}
