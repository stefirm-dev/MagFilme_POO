package org.oop.app.app;

import org.oop.app.domain.*;
import org.oop.app.service.ClientRepositoryImpl;
import org.oop.app.service.FacturaBuilder;
import org.oop.app.service.FilmRepositoryImpl;
import org.oop.app.service.VanzareService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class MagazinApp {

    // Dependentele sunt inițializate la pornire
    private static final FilmRepositoryImpl filmRepo = new FilmRepositoryImpl();
    private static final ClientRepositoryImpl clientRepo = new ClientRepositoryImpl();
    private static final VanzareService vanzareService = VanzareService.getInstance(); // Singleton

    public static void main(String[] args) {

        // 1. Initializare BD
        System.out.println("--- 1. INIȚIALIZARE DATE ---");
        filmRepo.removeAll(); // Curata tabelele Film, Factura, ArticolComanda

        // 2. Populare initiala
        populeazaFilme();

        // 3. Demo POO
        demoClientiValidation();

        // 4. Demo Streams: Interogare Complexă (Cerinta ta)
        demoStreamsInterogare();

        // 5. Demo Design Patterns: Builder, Polimorfism & Business Logic
        demoVanzareLogic();
    }

    private static void populeazaFilme() {
        System.out.println("\n--- 2. POPULARE FILME (JPA ADD) ---");

        // APELURI CORECTATE: ID-ul (null) NU se mai transmite constructorului
        Film f1 = new Film("The Shining", "S. Kubrick", 1980, 146, 25.0, GenFilm.HORROR);
        Film f2 = new Film("Hereditary", "A. Aster", 2018, 127, 35.0, GenFilm.HORROR);
        Film f3 = new Film("Midsommar", "A. Aster", 2019, 148, 35.0, GenFilm.HORROR);
        Film f4 = new Film("The Grand Budapest Hotel", "W. Anderson", 2014, 99, 30.0, GenFilm.COMEDIE);
        Film f5 = new Film("Planet Earth II", "D. Attenborough", 2016, 300, 40.0, GenFilm.DOCUMENTAR);

        // Salvează-le (JPA persist)
        filmRepo.add(f1);
        filmRepo.add(f2);
        filmRepo.add(f3);
        filmRepo.add(f4);
        filmRepo.add(f5);

        System.out.println("Total filme în BD: " + filmRepo.getNumarFilme());
    }

    private static void demoClientiValidation() {
        System.out.println("\n--- 3. DEMO POO & VALIDATION ---");

        // 3.1 Client PF (Corect - fara exceptii de validare la salvare)
        ClientPF clientPF = new ClientPF(null, "Popescu Ion", "ion@pf.ro", "1900101123456", LocalDate.of(1990, 1, 1));
        clientRepo.add(clientPF);
        System.out.println("Client salvat (PF): " + clientPF.getNume() + " (Discount: " + clientPF.getDiscountProcent() * 100 + "%)");

        // 3.2 Client PJ (Corect)
        ClientPJ clientPJ = new ClientPJ(null, "SRL Tech", "tech@pj.ro", "RO123456", "Tech Solutions S.R.L.");
        clientRepo.add(clientPJ);
        System.out.println("Client salvat (PJ): " + clientPJ.getNume() + " (Discount: " + clientPJ.getDiscountProcent() * 100 + "%)");

        // 3.3 Demo Singleton (Validare)
        System.out.println("Validare CNP ('123') prin Singleton Service: " + vanzareService.isCnpValid("123"));
    }

    private static void demoStreamsInterogare() {
        System.out.println("\n--- 4. DEMO STREAMS (Interogare Complexă) ---");

        // Toate filmele Horror lansate după 2009, grupate după Durată (Desc.)
        Map<Integer, List<Film>> rezultate = filmRepo.interogareFilmeHorrorDupaAnDurata(2009);

        System.out.println("Interogare: Filme HORROR > 2009, grupate după DURATĂ:");

        // Afișarea rezultatelor grupate (Streams + Lambdas)
        rezultate.forEach((durata, lista) -> {
            System.out.println("  -> Filme cu durata " + durata + " min:");
            lista.forEach(f -> System.out.println("     - [" + f.getAnLansare() + "] " + f.getTitlu()));
        });
    }

    private static void demoVanzareLogic() {
        System.out.println("\n--- 5. DEMO DESIGN PATTERNS & BUSINESS LOGIC ---");

        // Preluare clienti si filme
        Client clientPF = clientRepo.getClientById(1); // Popescu Ion (0% discount)
        Client clientPJ = clientRepo.getClientById(2); // SRL Tech (5% discount)
        Film filmHorror = filmRepo.getFilmById(2);
        Film filmComedie = filmRepo.getFilmById(4);

        // 5.1 CREARE FACTURA PENTRU PF (FARA DISCOUNT) - BUILDER PATTERN
        Factura facturaPF = new FacturaBuilder()
                .forClient(clientPF)
                .withArticol(new ArticolComanda(filmHorror, 2))
                .withArticol(new ArticolComanda(filmComedie, 1))
                .build();

        // BUSINESS LOGIC & POLIMORFISM
        vanzareService.calculeazaPretFinal(facturaPF);

        System.out.println("Factura PF (SubTotal: " + facturaPF.calculSubTotal() + ") -> Pret Final: " + facturaPF.getPretTotal());

        // 5.2 CREARE FACTURA PENTRU PJ (CU DISCOUNT) - JPA PERSIST
        Factura facturaPJ = new FacturaBuilder()
                .forClient(clientPJ)
                .withArticol(new ArticolComanda(filmHorror, 5))
                .build();

        // BUSINESS LOGIC & POLIMORFISM
        vanzareService.calculeazaPretFinal(facturaPJ);

        System.out.println("Factura PJ (SubTotal: " + facturaPJ.calculSubTotal() + ") -> Pret Final: " + facturaPJ.getPretTotal());

        // 5.3 Salvarea facturilor (JPA Transaction)
        // Persistenta se face intr-o tranzactie separata pentru Facturi
        FilmRepositoryImpl repoWithEM = new FilmRepositoryImpl();
        try {
            repoWithEM.getEntityManager().getTransaction().begin();
            repoWithEM.getEntityManager().persist(facturaPF);
            repoWithEM.getEntityManager().persist(facturaPJ);
            repoWithEM.getEntityManager().getTransaction().commit();
            System.out.println("Facturi salvate cu succes in BD (ID PF: " + facturaPF.getIdFactura() + ", ID PJ: " + facturaPJ.getIdFactura() + ")");
        } catch (Exception e) {
            System.err.println("Eroare la salvarea Facturilor: " + e.getMessage());
        }
    }
}