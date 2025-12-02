package org.oop.app.domain;

import jakarta.persistence.*;
import lombok.*;

// Strategia JOINED: o tabela pentru clasa de baza si tabele separate pentru fiecare subclasa
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public abstract class Client implements Comparable<Client> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idClient;

    @NonNull private String nume;
    @NonNull private String email;

    // Metoda abstractă (polimorfism)
    public abstract Double getDiscountProcent();

    @Override
    public int compareTo(Client other) {
        return this.idClient.compareTo(other.idClient);
    }
}