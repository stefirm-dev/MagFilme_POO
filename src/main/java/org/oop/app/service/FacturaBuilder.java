package org.oop.app.service;

import org.oop.app.domain.Articol; // NOU
import org.oop.app.domain.Client;
import org.oop.app.domain.Factura;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Builder Pattern
public class FacturaBuilder {
    private Client client;
    private List<Articol> articole = new ArrayList<>(); // NOU
    private LocalDate dataVanzare = LocalDate.now();

    public FacturaBuilder forClient(Client client) {
        this.client = client;
        return this;
    }

    public FacturaBuilder withArticol(Articol articol) { // NOU
        this.articole.add(articol);
        return this;
    }

    public FacturaBuilder onDate(LocalDate dataVanzare) {
        this.dataVanzare = dataVanzare;
        return this;
    }

    public Factura build() {
        if (client == null) {
            throw new IllegalStateException("Factura trebuie sa aiba un client!");
        }
        Factura factura = new Factura(this.dataVanzare, this.client);

        this.articole.forEach(factura::adaugaArticol);

        this.articole = new ArrayList<>();
        this.client = null;
        this.dataVanzare = LocalDate.now();

        return factura;
    }
}