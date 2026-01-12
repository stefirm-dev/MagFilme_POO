package org.oop.app.web;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.oop.app.domain.Client;
import org.oop.app.domain.ClientPF;
import org.oop.app.domain.ClientPJ;
import org.oop.app.service.ClientRepositoryImpl;

@Route(value = "clienti", layout = MainLayout.class)
public class ClientView extends VerticalLayout {

    private final ClientRepositoryImpl clientRepo = new ClientRepositoryImpl();
    private final Grid<Client> grid = new Grid<>();
    private final ClientForm form = new ClientForm();

    public ClientView() {
        addClassName("client-view");
        setSizeFull();
        configureGrid();
        configureForm();

        // Butoane pentru adăugare tipuri diferite
        Button addPfBtn = new Button("Adaugă PF");
        addPfBtn.addClickListener(e -> editClient(new ClientPF()));

        Button addPjBtn = new Button("Adaugă PJ");
        addPjBtn.addClickListener(e -> editClient(new ClientPJ()));

        HorizontalLayout toolbar = new HorizontalLayout(addPfBtn, addPjBtn);

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setFlexGrow(2, grid);
        mainContent.setFlexGrow(1, form);
        mainContent.setSizeFull();

        add(new H2("Gestiune Clienți"), toolbar, mainContent);

        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(Client::getNume).setHeader("Nume");
        grid.addColumn(Client::getEmail).setHeader("Email");
        grid.addColumn(c -> c instanceof ClientPF ? "Pers. Fizică" : "Pers. Juridică").setHeader("Tip");

        grid.asSingleSelect().addValueChangeListener(event -> editClient(event.getValue()));
    }

    private void configureForm() {
        form.save.addClickListener(event -> {
            Client client = form.binder.getBean();
            // Actualizăm manual valorile din câmpurile specifice înainte de salvare
            if (client instanceof ClientPF pf) {
                pf.setCNP(form.cnp.getValue());
                pf.setDataNastere(form.dataNastere.getValue());
            } else if (client instanceof ClientPJ pj) {
                pj.setCUI(form.cui.getValue());
                pj.setDenumireCompanie(form.denumireCompanie.getValue());
            }

            clientRepo.add(client);
            updateList();
            closeEditor();
        });

        form.delete.addClickListener(event -> {
            clientRepo.remove(form.binder.getBean());
            updateList();
            closeEditor();
        });

        form.close.addClickListener(event -> closeEditor());
    }

    public void editClient(Client client) {
        if (client == null) {
            closeEditor();
        } else {
            form.setClient(client);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(clientRepo.getAll());
    }
}