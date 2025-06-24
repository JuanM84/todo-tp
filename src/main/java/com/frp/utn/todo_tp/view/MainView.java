package com.frp.utn.todo_tp.view;

import com.vaadin.flow.router.Route;
import com.frp.utn.todo_tp.model.Task;
import com.frp.utn.todo_tp.repository.TaskRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;


@Route("")
public class MainView extends VerticalLayout{
    
    private final TaskRepository taskRepository;

    private final Grid<Task> grid = new Grid<>(Task.class);
    private final TextField descriptionField = new TextField("Descripción");
    private final Button addButton = new Button("Agregar");

    public MainView(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        configureGrid();
        configureAddButton();

        add(new HorizontalLayout(descriptionField, addButton),grid);
        refreshGrid();

    }

    private void configureGrid() {
        
        grid.removeAllColumns();
        grid.addColumn(Task::getId).setHeader("ID");
        grid.addColumn(Task::getDescription).setHeader("Descripción");

        grid.addComponentColumn(task -> {
            Checkbox checkbox = new Checkbox(task.isDone());
            checkbox.addValueChangeListener(event -> {
                task.setDone(event.getValue());
                taskRepository.save(task);
                refreshGrid();
            });
            return checkbox;
        }).setHeader("Hecho");

        grid.addComponentColumn(task -> {
            Button deleteButton = new Button("Borrar");
            deleteButton.addClickListener(event -> {
                taskRepository.delete(task);
                refreshGrid();
            });
            return deleteButton;
        }).setHeader("Acciones");
    }
    private void configureAddButton(){
        addButton.addClickListener(event -> {
            String description = descriptionField.getValue();
            if (description != null && !description.isEmpty()) {
                Task newTask = new Task(description, false);
                taskRepository.save(newTask);
                descriptionField.clear();
                refreshGrid();
            }
        });
    }
    private void refreshGrid(){
        grid.setItems(taskRepository.findAll());
    }
}
