package com.frp.utn.todo_tp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String description;
    private boolean done;

    //Constructor por defecto
    public Task(){
    }

    public Task(String description, boolean done){
        this.description = description;
        this.done = done;
    }

    // Getters y Setters
    public Long getId(){
        return id;
    }
    
    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public boolean isDone(){
        return done;
    }

    public void setDone(boolean done){
        this.done = done;
    }
}
