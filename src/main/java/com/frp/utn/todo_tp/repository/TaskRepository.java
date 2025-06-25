package com.frp.utn.todo_tp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.frp.utn.todo_tp.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{    
}
