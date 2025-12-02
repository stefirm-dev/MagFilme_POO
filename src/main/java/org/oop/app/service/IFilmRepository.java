package org.oop.app.service;

import org.oop.app.domain.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;

// Interfata Repository pentru Film (Contractul DDD)
public interface IFilmRepository {
    Long getNumarFilme();
    Film getFilmById(Integer id);
    Film getFilmByTitlu(String titlu);
    Collection<Film> getAll();
    void add(Film film);
    void remove(Film film);
    void update(Film film);
    void removeAll();

    // Metoda specifica pentru cerinta de business: Interogare Complexă
    Map<Integer, List<Film>> interogareFilmeHorrorDupaAnDurata(Integer anMinim);
}