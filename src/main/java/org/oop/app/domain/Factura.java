package org.oop.app.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Factura {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idFactura;

    @NonNull private LocalDate dataVanzare;

    // Relatia ManyToOne cu Client (poate fi PF sau PJ)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client")
    @NonNull private Client client;
    @OneToMany(mappedBy = "factura", cascade = ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ArticolComanda> articole = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    private Double pretTotal; // Pretul dupa aplicarea reducerii/taxelor

    // Logica de calcul a pretului total
    public Double calculSubTotal() {
        return this.articole.stream()
                .mapToDouble(item -> {
                    // Dacă prețul este null, tratăm ca 0.0 pentru a preveni NPE
                    if (item.getPretArticol() == null) {
                        return 0.0;
                    }
                    return item.getPretArticol();
                })
                .sum();
    }

    // Adaugarea logica a unui articol
    public void adaugaArticol(ArticolComanda articol) {
        if (!this.articole.contains(articol)) {
            articol.setFactura(this); // Seteaza referinta inversa Factura pe Articol
            this.articole.add(articol);
        }
    }

    // Seteaza pretul final (cu tot cu discount - va fi folosit de VanzareService)
    public void setPretTotal(Double pretTotal) {
        this.pretTotal = pretTotal;
    }
}