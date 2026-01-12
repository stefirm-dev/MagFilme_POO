package org.oop.app.web;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        H1 logo = new H1("Magazin Filme PRO");
        logo.getStyle().set("font-size", "1.2em");
        logo.getStyle().set("margin", "0 1em");


        RouterLink filmeLink = new RouterLink("Filme", MainView.class);
        RouterLink clientiLink = new RouterLink("Clienți", ClientView.class);
        RouterLink comenziLink = new RouterLink("Comenzi", ComenziView.class);
        RouterLink dashLink = new RouterLink("Portofoliu", DashboardView.class);

        HorizontalLayout menu = new HorizontalLayout(logo, filmeLink, clientiLink, comenziLink, dashLink);
        menu.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        menu.setPadding(true);
        menu.setSpacing(true);

        addToNavbar(menu);
    }
}