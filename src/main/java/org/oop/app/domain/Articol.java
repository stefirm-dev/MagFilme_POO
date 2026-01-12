package org.oop.app.domain;

import jakarta.persistence.*;
import lombok.*;

// Am înlocuit @Data cu @Getter, @Setter, @ToString
@Entity
@Getter @Setter @ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor // Păstrează-l pentru JPA
// Am eliminat @RequiredArgsConstructor pentru a evita conflictul
public class Articol {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idArticol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_film")
    @NonNull private Film film;

    @NonNull private Integer cantitate;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_factura")
    private Factura factura;

    @Setter(AccessLevel.NONE)
    private Double pretArticol; // Pretul total (cantitate * pret_film)




    public Articol(@NonNull Film film, @NonNull Integer cantitate) {
        this.film = film;
        this.cantitate = cantitate;
        this.pretArticol = calculPretTotal();
    }

    private Double calculPretTotal(){
        if (film != null && cantitate != null) {
            return film.getPret() * cantitate;
        }
        return 0.0;
    }


    public void setFilm(@NonNull Film film) {
        this.film = film;
        this.pretArticol = calculPretTotal();
    }
}