package org.oop.app.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor
public class ClientPF extends Client {

    // Java Bean Validation
    @Size(min = 13, max = 13, message = "CNP-ul trebuie sa aiba exact 13 cifre!")
    @Pattern(regexp = "\\d+", message = "CNP-ul trebuie sa contina doar cifre!")
    private String CNP;
    private LocalDate dataNastere;

    public ClientPF(Integer idClient, String nume, String email, String CNP, LocalDate dataNastere) {
        super( nume, email);
        this.CNP = CNP;
        this.dataNastere = dataNastere;
    }

    @Override
    public Double getDiscountProcent() {
        return 0.0; // Fara discount implicit
    }
}