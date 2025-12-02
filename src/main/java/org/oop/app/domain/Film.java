package org.oop.app.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Film implements Comparable<Film> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    // S-A ELIMINAT @NonNull AICI!
    private Integer idFilm;

    @NonNull private String titlu;
    @NonNull private String regizor;
    @NonNull private Integer anLansare;
    @NonNull private Integer durata; // în minute
    @NonNull private Double pret;

    @Enumerated(STRING)
    @NonNull private GenFilm gen;

    @Override
    public int compareTo(Film other) {
        return this.idFilm.compareTo(other.idFilm);
    }
}