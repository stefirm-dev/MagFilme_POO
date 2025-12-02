package org.oop.app.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.ConstraintViolationException;
import org.oop.app.domain.Client;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class ClientRepositoryImpl implements IClientRepository {
    private final EntityManager entityManager;
    private final String JPQL_SELECT_ALL = "SELECT c FROM Client c";

    public ClientRepositoryImpl() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MagazinFilmeJPA");
        this.entityManager = emf.createEntityManager();
    }

    @Override
    public Client getClientById(Integer id) {
        return this.entityManager.find(Client.class, id);
    }

    @Override
    public Collection<Client> getAll() {
        List<Client> result = this.entityManager
                .createQuery(JPQL_SELECT_ALL, Client.class)
                .getResultList();
        // Returneaza rezultatele ordonate (folosind Comparable<Client>)
        return new TreeSet<>(result);
    }

    @Override
    public void add(Client client) {
        try {
            entityManager.getTransaction().begin();
            // Merge va persista (INSERT) o entitate noua sau o va actualiza (UPDATE) pe cea existenta (cu ID)
            this.entityManager.merge(client);
            entityManager.getTransaction().commit();
        } catch(ConstraintViolationException e) {
            // Prinde eroarea de validare Java Bean Validation
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            System.err.println("EROARE VALIDARE CLIENT: Datele clientului nu sunt valide!");
            e.getConstraintViolations().forEach(v -> System.err.println(" - " + v.getMessage()));
            throw new RuntimeException("Eroare de validare a datelor clientului.", e);
        }
        catch (Exception ex) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException("Eroare la adăugarea/actualizarea clientului: " + ex.getMessage());
        }
    }
}