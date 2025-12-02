package org.oop.app.service;

import org.oop.app.domain.ClientPJ;
import org.oop.app.domain.Factura;

// Domain Service pentru logica de afaceri (Business Logic)
public class VanzareService {

    /**
     * Calculează prețul final al facturii aplicând discountul corespunzător.
     * Folosește Polimorfismul prin metoda getDiscountProcent() din clasa Client.
     */
    public Factura calculeazaPretFinal(Factura factura) {

        Double subTotal = factura.calculSubTotal();
        Double discountProcent = factura.getClient().getDiscountProcent(); // Polimorfism

        Double pretFinal = subTotal * (1 - discountProcent);

        factura.setPretTotal(pretFinal);
        return factura;
    }

    // Metoda utilitara pentru a demonstra Singleton (similar ComputeCerintaService)
    public boolean isCnpValid(String cnp) {
        return cnp != null && cnp.matches("\\d{13}");
    }

    // Singleton Implementation
    private static final VanzareService serviceInstance = new VanzareService();
    private VanzareService(){}
    public static VanzareService getInstance(){
        return serviceInstance;
    }
}