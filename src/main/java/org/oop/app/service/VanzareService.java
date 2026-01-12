package org.oop.app.service;

import org.oop.app.domain.Factura;

public class VanzareService {


    public Factura calculeazaPretFinal(Factura factura) {

        Double subTotal = factura.calculSubTotal();

        Double pretFinal = subTotal;

 ;;       factura.setPretTotal(pretFinal);
        return factura;
    }

    public boolean isCnpValid(String cnp) {
        return cnp != null && cnp.matches("\\d{13}");
    }

    private static final VanzareService serviceInstance = new VanzareService();
    private VanzareService(){}
    public static VanzareService getInstance(){
        return serviceInstance;
    }
}