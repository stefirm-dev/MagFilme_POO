package org.oop.app.web;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.oop.app.service.FilmRepositoryImpl;

@PageTitle("Dashboard | MagFilme")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        setAlignItems(Alignment.CENTER);
        add(new H2("📊 Statistici Magazin"));

        FilmRepositoryImpl filmRepo = new FilmRepositoryImpl();
        int total = filmRepo.getAll().size();

        Span s = new Span("Total filme în baza de date: " + total);
        s.getStyle().set("font-size", "var(--lumo-font-size-xl)");
        add(s);
    }
}