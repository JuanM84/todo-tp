package com.frp.utn.todo_tp.view;

import com.vaadin.flow.router.Route;
import com.frp.utn.todo_tp.model.Task;
import com.frp.utn.todo_tp.repository.TaskRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;


@Route("")
public class MainView extends VerticalLayout{
    
    private final TaskRepository taskRepository;

    private final Grid<Task> grid = new Grid<>(Task.class);
    private final TextField descriptionField = new TextField("Nueva Tarea");
    private final TextField searchField = new TextField("Buscar...");
    private final Button addButton = new Button("Agregar");

    public MainView(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        // TITULO
        H1 titulo = new H1("Lista de Tareas");
        titulo.getStyle()
            .set("margin","0")
            .set("font-size","2rem");
        add(titulo);

        // NAVBAR

        HorizontalLayout navbar = new HorizontalLayout(descriptionField, addButton, searchField);
        navbar.setWidthFull();
        navbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        navbar.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        navbar.getStyle().set("margin-bottom","1rem");
        addButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        add(navbar);


        setPadding(true);
        setSpacing(true);

        configureGrid();
        configureAddButton();
        configureSearchField();

        add(grid);
        refreshGrid();

    }

    private void configureGrid() {
        
        grid.removeAllColumns();
        // Columnas
        grid.addColumn(Task::getId).setHeader("ID").setWidth("5%").setFlexGrow(0);
        
        grid.addComponentColumn(task -> {
            Checkbox checkbox = new Checkbox(task.isDone());
            checkbox.addValueChangeListener(event -> {
                task.setDone(event.getValue());
                taskRepository.save(task);
                refreshGrid();
            });
            return checkbox;
        }).setHeader("Hecho").setWidth("10%").setFlexGrow(0);
        
        grid.addColumn(Task::getDescription).setHeader("Descripción").setWidth("300px").setFlexGrow(0);

        grid.addComponentColumn(task -> {
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(event -> deleteTask(task));
            return deleteButton;
        }).setHeader("Acciones");
        grid.setWidthFull();

    }
    private void deleteTask(Task task){
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new H3("Eliminar tarea"));
        confirmDialog.add(new Paragraph("¿Estas seguro de que quieres borrar esta tarea?"));

        Button confirmButton = new Button("Eliminar", e -> {
            taskRepository.delete(task);
            refreshGrid();
            confirmDialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancelar", e -> confirmDialog.close());

        HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
        confirmDialog.add(buttons);
        confirmDialog.open();

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
    private void configureSearchField(){
        searchField.setPlaceholder("Filtra por descripción");
        searchField.addValueChangeListener(event ->{
            String filter = event.getValue();
            if(filter == null || filter.isEmpty()){
                refreshGrid();
            }else{
                grid.setItems(
                    taskRepository.findAll().stream()
                    .filter(task -> task.getDescription().toLowerCase().contains(filter.toLowerCase())).toList()
                );
            }
        });
    }

    private void refreshGrid(){
        grid.setItems(taskRepository.findAll());
    }
}
