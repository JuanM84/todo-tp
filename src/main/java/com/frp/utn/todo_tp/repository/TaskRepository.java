package com.frp.utn.todo_tp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.frp.utn.todo_tp.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{    
}
