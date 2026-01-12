package org.oop.app.web;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.oop.app.domain.Client;
import org.oop.app.domain.Film;
import org.oop.app.domain.Factura;
import org.oop.app.domain.Articol;
import org.oop.app.service.ClientRepositoryImpl;
import org.oop.app.service.FilmRepositoryImpl;
import org.oop.app.service.FacturaBuilder;

import java.util.Set;

@PageTitle("Comenzi | Magazin Filme")
@Route(value = "comenzi", layout = MainLayout.class)
public class ComenziView extends VerticalLayout {

    private final ClientRepositoryImpl clientRepo = new ClientRepositoryImpl();
    private final FilmRepositoryImpl filmRepo = new FilmRepositoryImpl();

    private ComboBox<Client> comboClienti = new ComboBox<>("Selectează Clientul");
    private Grid<Film> gridFilme = new Grid<>(Film.class);
    private Button btnGenerare = new Button("Generează Factură");
    private Pre facturaDisplay = new Pre();

    public ComenziView() {
        setSizeFull();
        setSpacing(true);

        add(new H2("Creare Comandă Nouă"));

        comboClienti.setItems(clientRepo.getAll());
        comboClienti.setItemLabelGenerator(Client::getNume);
        comboClienti.setWidth("350px");

        gridFilme.setColumns("titlu", "pret");
        gridFilme.setSelectionMode(Grid.SelectionMode.MULTI);
        gridFilme.setItems(filmRepo.getAll());
        gridFilme.setHeight("300px");

        btnGenerare.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnGenerare.addClickListener(e -> genereazaFacturaDirect());

        facturaDisplay.getStyle().set("background", "#eeeeee");
        facturaDisplay.getStyle().set("padding", "15px");
        facturaDisplay.getStyle().set("border", "1px solid #ccc");

        add(comboClienti, gridFilme, btnGenerare, facturaDisplay);
    }

    private void genereazaFacturaDirect() {
        Client clientSelectat = comboClienti.getValue();
        Set<Film> filmeSelectate = gridFilme.getSelectedItems();

        if (clientSelectat == null || filmeSelectate.isEmpty()) {
            Notification.show("Alegeți un client și măcar un film!");
            return;
        }

        try {
            FacturaBuilder builder = new FacturaBuilder();

            builder.forClient(clientSelectat);

            for (Film f : filmeSelectate) {
                builder.withArticol(new Articol(f, 1));
            }

            Factura facturaFinala = builder.build();

            facturaDisplay.setText(facturaFinala.toString());
            Notification.show("Factura a fost generată cu succes!");

        } catch (Exception ex) {
            Notification.show("Eroare la asamblare: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}