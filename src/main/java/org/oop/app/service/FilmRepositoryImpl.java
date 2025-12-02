package org.oop.app.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.oop.app.domain.Film;
import org.oop.app.domain.GenFilm;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

// Implementarea Repository Pattern folosind JPA
public class FilmRepositoryImpl implements IFilmRepository { // LINIE CORECTATĂ

    private final EntityManager entityManager;
    private final String JPQL_SELECT_ALL = "SELECT f FROM Film f";

    public FilmRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public FilmRepositoryImpl() {
        // Configurarea Unității de Persistență: ATENȚIE la numele PU
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MagazinFilmeJPA");
        this.entityManager = emf.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Long getNumarFilme() {
        return this.entityManager
                .createQuery("SELECT COUNT(f) FROM Film f", Long.class)
                .getSingleResult();
    }

    @Override
    public Film getFilmById(Integer id) {
        return this.entityManager.find(Film.class, id);
    }

    @Override
    public Film getFilmByTitlu(String titlu) {
        return this.entityManager
                .createQuery(JPQL_SELECT_ALL + " WHERE f.titlu = :titlu", Film.class)
                .setParameter("titlu", titlu)
                .getSingleResult();
    }

    @Override
    public Collection<Film> getAll() {
        List<Film> result = this.entityManager
                .createQuery(JPQL_SELECT_ALL, Film.class)
                .getResultList();

        // Folosim un TreeSet pentru a returna rezultatele ordonate, bazat pe Comparable<Film>
        return new TreeSet<>(result);
    }

    @Override
    public void add(Film film) {
        try {
            entityManager.getTransaction().begin();
            // Dacă entitatea există, o actualizăm (merge), altfel o salvăm (persist)
            if (film.getIdFilm() != null && entityManager.find(Film.class, film.getIdFilm()) != null) {
                this.entityManager.merge(film);
            } else {
                this.entityManager.persist(film);
            }
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException("Eroare la adăugarea/actualizarea filmului: " + ex.getMessage());
        }
    }

    @Override
    public void remove(Film film) {
        try {
            Film filmPersistent = entityManager.find(Film.class, film.getIdFilm());
            if (filmPersistent != null) {
                entityManager.getTransaction().begin();
                this.entityManager.remove(filmPersistent);
                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException("Eroare la ștergerea filmului: " + ex.getMessage());
        }
    }

    @Override
    public void update(Film film) {
        // In JPA, merge acoperă operația de update, așa că refolosim add
        add(film);
    }

    @Override
    public void removeAll() {
        try {
            entityManager.getTransaction().begin();
            // Atenție: JPQL/HQL DELETE nu apelează cascade/orphanRemoval, trebuie făcut manual
            entityManager.createQuery("DELETE FROM ArticolComanda a").executeUpdate();
            entityManager.createQuery("DELETE FROM Factura f").executeUpdate();
            entityManager.createQuery("DELETE FROM Client c").executeUpdate();
            entityManager.createQuery("DELETE FROM Film f").executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException("Eroare la ștergerea tuturor datelor: " + ex.getMessage());
        }
    }

    @Override
    public Map<Integer, List<Film>> interogareFilmeHorrorDupaAnDurata(Integer anMinim) {
        // Pasul 1: Obținem toate filmele din DB
        List<Film> toateFilmele = entityManager.createQuery(JPQL_SELECT_ALL, Film.class).getResultList();

        // Pasul 2-5: Aplicăm logica de business prin Streams
        return toateFilmele.stream()
                // 2. Filtrare (WHERE gen = HORROR)
                .filter(f -> f.getGen().equals(GenFilm.HORROR))
                // 3. Filtrare (WHERE anLansare > anMinim)
                .filter(f -> f.getAnLansare() > anMinim)
                // 4. Sortare (ORDER BY) - după durată descrescătoare
                .sorted(Comparator.comparing(Film::getDurata).reversed())
                // 5. Gruparea (GROUP BY) - după durată
                .collect(Collectors.groupingBy(Film::getDurata, toList()));
    }
}