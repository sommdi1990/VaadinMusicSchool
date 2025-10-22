package com.musicschool.ui;

import com.musicschool.entity.Student;
import com.musicschool.service.StudentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * Students management view for the music school system.
 */
@Route(value = "students", layout = MainLayout.class)
@PageTitle("Students | Music School Management")
public class StudentsView extends VerticalLayout {

    private final StudentService studentService;
    private Grid<Student> grid;
    private TextField searchField;
    private ConfigurableFilterDataProvider<Student, Void, String> dataProvider;

    public StudentsView(StudentService studentService) {
        this.studentService = studentService;

        setSizeFull();
        addClassName("students-view");

        createHeader();
        createToolbar();
        createGrid();
        loadData();
    }

    private void createHeader() {
        H2 header = new H2("Students Management");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE, LumoUtility.Margin.Top.MEDIUM);
        add(header);
    }

    private void createToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        searchField = new TextField();
        searchField.setPlaceholder("Search students...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("300px");

        Button addButton = new Button("Add Student", VaadinIcon.PLUS.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openStudentDialog(new Student()));

        Button refreshButton = new Button("Refresh", VaadinIcon.REFRESH.create());
        refreshButton.addClickListener(e -> refreshGrid());

        toolbar.add(searchField, addButton, refreshButton);
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        add(toolbar);
    }

    private void createGrid() {
        grid = new Grid<>(Student.class, false);
        grid.setSizeFull();

        grid.addColumn(Student::getFirstName).setHeader("First Name").setSortable(true);
        grid.addColumn(Student::getLastName).setHeader("Last Name").setSortable(true);
        grid.addColumn(Student::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(Student::getPhone).setHeader("Phone");
        grid.addColumn(student -> student.getDateOfBirth().toString()).setHeader("Date of Birth").setSortable(true);
        grid.addColumn(student -> student.getLevel().toString()).setHeader("Level").setSortable(true);
        grid.addColumn(Student::getAge).setHeader("Age");

        grid.addComponentColumn(student -> {
            Button editButton = new Button("Edit", VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.addClickListener(e -> openStudentDialog(student));
            return editButton;
        }).setHeader("Actions");

        grid.addComponentColumn(student -> {
            Button deleteButton = new Button("Delete", VaadinIcon.TRASH.create());
            deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteStudent(student));
            return deleteButton;
        }).setHeader("");

        add(grid);
    }

    private void loadData() {
        dataProvider = DataProvider.fromFilteringCallbacks(
            query -> {
                Stream<Student> stream = studentService.findStudentsWithSearch(
                    searchField.getValue(),
                    query.getPageRequest()
                ).getContent().stream();
                return stream;
            },
            query -> (int) studentService.findStudentsWithSearch(
                searchField.getValue(),
                query.getPageRequest()
            ).getTotalElements()
        );

        grid.setDataProvider(dataProvider);

        searchField.addValueChangeListener(e -> {
            dataProvider.setFilter(e.getValue());
        });
    }

    private void refreshGrid() {
        dataProvider.refreshAll();
    }

    private void openStudentDialog(Student student) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );

        TextField firstNameField = new TextField("First Name");
        firstNameField.setValue(student.getFirstName() != null ? student.getFirstName() : "");
        firstNameField.setRequired(true);

        TextField lastNameField = new TextField("Last Name");
        lastNameField.setValue(student.getLastName() != null ? student.getLastName() : "");
        lastNameField.setRequired(true);

        EmailField emailField = new EmailField("Email");
        emailField.setValue(student.getEmail() != null ? student.getEmail() : "");

        TextField phoneField = new TextField("Phone");
        phoneField.setValue(student.getPhone() != null ? student.getPhone() : "");

        TextField dateOfBirthField = new TextField("Date of Birth");
        dateOfBirthField.setValue(student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "");
        dateOfBirthField.setPlaceholder("YYYY-MM-DD");

        TextField addressField = new TextField("Address");
        addressField.setValue(student.getAddress() != null ? student.getAddress() : "");

        formLayout.add(firstNameField, lastNameField, emailField, phoneField, dateOfBirthField, addressField);

        Button saveButton = new Button("Save", VaadinIcon.CHECK.create());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            student.setFirstName(firstNameField.getValue());
            student.setLastName(lastNameField.getValue());
            student.setEmail(emailField.getValue());
            student.setPhone(phoneField.getValue());
            if (!dateOfBirthField.getValue().isEmpty()) {
                student.setDateOfBirth(LocalDate.parse(dateOfBirthField.getValue()));
            }
            student.setAddress(addressField.getValue());

            studentService.save(student);
            dialog.close();
            refreshGrid();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonLayout);
        dialog.add(dialogLayout);

        dialog.open();
    }

    private void deleteStudent(Student student) {
        studentService.deleteById(student.getId());
        refreshGrid();
    }
}
