package org.oop.app.web;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField; //
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.oop.app.domain.Film;
import org.oop.app.domain.GenFilm;

public class FilmForm extends FormLayout {
    TextField titlu = new TextField("Titlu Film");
    TextField regizor = new TextField("Regizor");

    IntegerField anLansare = new IntegerField("An Lansare");
    IntegerField durata = new IntegerField("Durată (minute)");

    NumberField pret = new NumberField("Preț");
    ComboBox<GenFilm> gen = new ComboBox<>("Gen");

    Button save = new Button("Salvează");
    Button delete = new Button("Șterge");
    Button close = new Button("Anulează");

    Binder<Film> binder = new BeanValidationBinder<>(Film.class);

    public FilmForm() {
        gen.setItems(GenFilm.values());

        binder.bindInstanceFields(this);

        add(titlu, regizor, anLansare, durata, pret, gen, createButtonsLayout());
    }

    public void setFilm(Film film) {
        binder.setBean(film);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return new HorizontalLayout(save, delete, close);
    }
}