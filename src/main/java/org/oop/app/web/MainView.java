package org.oop.app.web;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.oop.app.domain.Film;
import org.oop.app.service.FilmRepositoryImpl;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Filme | MagFilme")
public class MainView extends VerticalLayout {

    private final FilmRepositoryImpl filmRepo = new FilmRepositoryImpl();
    private final Grid<Film> grid = new Grid<>(Film.class);
    private final TextField filterText = new TextField();
    private final FilmForm form = new FilmForm();

    public MainView() {
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("idFilm", "titlu", "regizor", "anLansare", "gen", "pret");
        grid.asSingleSelect().addValueChangeListener(e -> editFilm(e.getValue()));
    }

    private void configureForm() {
        // Logica butoanelor din formular
        form.save.addClickListener(e -> saveFilm());
        form.close.addClickListener(e -> closeEditor());
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filtrează după titlu...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        // Aici poti adauga logica de filtrare daca ai implementat-o in repo

        Button addFilmButton = new Button("Adaugă Film");
        addFilmButton.addClickListener(click -> addFilm());

        return new HorizontalLayout(filterText, addFilmButton);
    }

    // Metodele de control (Controller) conform pag. 5 L8
    private void saveFilm() {
        Film film = form.binder.getBean();
        if (film != null) {
            filmRepo.add(film);
            updateList();
            closeEditor();
        }
    }

    private void addFilm() {
        grid.asSingleSelect().clear();
        editFilm(new Film());
    }

    public void editFilm(Film film) {
        if (film == null) {
            closeEditor();
        } else {
            form.setFilm(film);
            form.setVisible(true);
        }
    }

    private void closeEditor() {
        form.setVisible(false);
    }

    private void updateList() {
        grid.setItems(filmRepo.getAll());
    }
}