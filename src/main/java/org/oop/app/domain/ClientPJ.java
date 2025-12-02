package org.oop.app.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class ClientPJ extends Client {

    @Size(min = 2, max = 20, message = "CUI-ul trebuie sa fie intre 2 si 20 de caractere!")
    private String CUI;
    private String denumireCompanie;

    public ClientPJ(Integer idClient, String nume, String email, String CUI, String denumireCompanie) {
        super(nume, email);
        this.CUI = CUI;
        this.denumireCompanie = denumireCompanie;
    }

    // Implementarea metodei abstracte (Polimorfism)
    @Override
    public Double getDiscountProcent() {
        // Reducere de 5% pentru Clientii PJ (cerinta stabilita)
        return 0.05;
    }
}