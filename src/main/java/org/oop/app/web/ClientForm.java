package org.oop.app.web;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.oop.app.domain.Client;
import org.oop.app.domain.ClientPF;
import org.oop.app.domain.ClientPJ;

public class ClientForm extends FormLayout {
    TextField nume = new TextField("Nume");
    TextField email = new TextField("Email");
    TextField cnp = new TextField("CNP (pentru PF)");
    TextField cui = new TextField("CUI (pentru PJ)");
    TextField denumireCompanie = new TextField("Companie (pentru PJ)");
    DatePicker dataNastere = new DatePicker("Data Naștere (pentru PF)");

    Button save = new Button("Salvează");
    Button delete = new Button("Șterge");
    Button close = new Button("Anulează");

    Binder<Client> binder = new BeanValidationBinder<>(Client.class);

    public ClientForm() {
        addClassName("client-form");
        setWidth("25em"); // Setează o lățime fixă pentru a fi vizibil în layout-ul orizontal

        // Legăm câmpurile de bază
        binder.forField(nume).bind(Client::getNume, Client::setNume);
        binder.forField(email).bind(Client::getEmail, Client::setEmail);

        add(nume, email, cnp, dataNastere, cui, denumireCompanie, createButtonsLayout());
    }

    public void setClient(Client client) {
        binder.setBean(client);

        // Resetăm vizibilitatea câmpurilor în funcție de tipul clientului
        if (client instanceof ClientPF) {
            cnp.setVisible(true);
            dataNastere.setVisible(true);
            cui.setVisible(false);
            denumireCompanie.setVisible(false);

            // Binding manual pentru câmpuri specifice deoarece Binder<Client> nu vede proprietățile subclaselor direct
            ClientPF pf = (ClientPF) client;
            cnp.setValue(pf.getCNP() != null ? pf.getCNP() : "");
            dataNastere.setValue(pf.getDataNastere());
        } else if (client instanceof ClientPJ) {
            cnp.setVisible(false);
            dataNastere.setVisible(false);
            cui.setVisible(true);
            denumireCompanie.setVisible(true);

            ClientPJ pj = (ClientPJ) client;
            cui.setValue(pj.getCUI() != null ? pj.getCUI() : "");
            denumireCompanie.setValue(pj.getDenumireCompanie() != null ? pj.getDenumireCompanie() : "");
        }
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        return new HorizontalLayout(save, delete, close);
    }
}