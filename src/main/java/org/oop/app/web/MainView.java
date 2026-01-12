package org.oop.app.web;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.oop.app.domain.Film;
import org.oop.app.service.FilmRepositoryImpl;

@PageTitle("Filme | Magazin Filme")
@Route(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    private final FilmRepositoryImpl filmRepo = new FilmRepositoryImpl();
    private final Grid<Film> grid = new Grid<>();
    private final FilmForm form = new FilmForm(); // Formularul tău pentru CRUD

    public MainView() {
        setSizeFull();
        configureGrid();
        configureForm();

        Button addFilmButton = new Button("Adaugă Film Nou");
        addFilmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addFilmButton.addClickListener(click -> addFilm());

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setFlexGrow(2, grid);
        mainContent.setFlexGrow(1, form);
        mainContent.setSizeFull();

        add(new H2("Gestiune Inventar Filme"), addFilmButton, mainContent);

        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(Film::getIdFilm).setHeader("ID");
        grid.addColumn(Film::getTitlu).setHeader("Titlu");
        grid.addColumn(Film::getRegizor).setHeader("Regizor");
        grid.addColumn(Film::getAnLansare).setHeader("An");
        grid.addColumn(Film::getGen).setHeader("Gen");
        grid.addColumn(Film::getPret).setHeader("Preț");

        grid.asSingleSelect().addValueChangeListener(event -> editFilm(event.getValue()));
    }

    private void configureForm() {
        form.save.addClickListener(event -> saveFilm());

        form.delete.addClickListener(event -> {
            Film film = form.binder.getBean();
            if (film != null) {
                filmRepo.remove(film); // Asigură-te că metoda remove e în repo
                updateList();
                closeEditor();
            }
        });

        form.close.addClickListener(event -> closeEditor());
    }

    private void saveFilm() {
        Film film = form.binder.getBean();
        if (film != null) {
            filmRepo.add(film); // Folosește em.merge() sau em.persist()
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