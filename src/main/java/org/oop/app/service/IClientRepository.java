package org.oop.app.service;

import org.oop.app.domain.Client;
import java.util.Collection;

public interface IClientRepository {
    Client getClientById(Integer id);
    Collection<Client> getAll();
    void add(Client client);
    void remove(Client client); // Adăugați această linie
}