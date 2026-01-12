package org.oop.app.web;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.oop.app.domain.Client;
import org.oop.app.service.ClientRepositoryImpl;

@PageTitle("Clienți | MagFilme")
@Route(value = "clienti", layout = MainLayout.class)
public class ClientView extends VerticalLayout {

    private final ClientRepositoryImpl clientRepo = new ClientRepositoryImpl();
    // Definire Grid fără scanare automată pentru a evita erorile de nume (fidel C10)
    private final Grid<Client> grid = new Grid<>();

    public ClientView() {
        add(new H2("👥 Administrare Clienți"));
        setSizeFull();

        // Mapare manuală pe metodele tale din Client.java
        grid.addColumn(Client::getIdClient).setHeader("ID");
        grid.addColumn(Client::getNume).setHeader("Nume Client");


        // Coloană specială pentru Tip (PF/PJ)
        grid.addColumn(c -> c.getClass().getSimpleName()).setHeader("Tip Client");

        grid.setItems(clientRepo.getAll());
        add(grid);
    }
}