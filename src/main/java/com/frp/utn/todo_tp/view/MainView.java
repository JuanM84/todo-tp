package com.frp.utn.todo_tp.view;

import com.vaadin.flow.router.Route;
import com.frp.utn.todo_tp.model.Task;
import com.frp.utn.todo_tp.repository.TaskRepository;
import com.frp.utn.todo_tp.service.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
    
    private final TaskService taskService;

    private final Grid<Task> grid = new Grid<>(Task.class);
    private final TextField descriptionField = new TextField("Nueva Tarea");
    private final TextField searchField = new TextField("Buscar...");
    private final Button addButton = new Button("Agregar");

    public MainView(TaskService taskService) {
        this.taskService = taskService;

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
                taskService.save(task);
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
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader(new H3(task.getDescription()));
        confirmDialog.setText(new Paragraph("¿Estas seguro de que quieres borrar esta tarea?"));
        confirmDialog.setCancelable(true);
        confirmDialog.setConfirmText("Eliminar");
        confirmDialog.setCancelText("Cancelar");
        confirmDialog.addConfirmListener(event -> {
            taskService.delete(task.getId());
            refreshGrid();
        });
        confirmDialog.open();

    }

    private void configureAddButton(){
        addButton.addClickListener(event -> {
            String description = descriptionField.getValue();
            if (description != null && !description.isEmpty()) {
                Task newTask = new Task(description, false);
                taskService.save(newTask);
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
                    taskService.findAll().stream()
                    .filter(task -> task.getDescription().toLowerCase().contains(filter.toLowerCase())).toList()
                );
            }
        });
    }

    private void refreshGrid(){
        grid.setItems(taskService.findAll());
    }
}
