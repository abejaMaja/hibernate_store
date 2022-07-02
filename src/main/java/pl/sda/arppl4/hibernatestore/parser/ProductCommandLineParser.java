package pl.sda.arppl4.hibernatestore.parser;

import pl.sda.arppl4.hibernatestore.dao.ProductDao;
import pl.sda.arppl4.hibernatestore.model.Product;
import pl.sda.arppl4.hibernatestore.model.ProductUnit;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProductCommandLineParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Scanner scanner;
    private final ProductDao dao;

    public ProductCommandLineParser(Scanner scanner, ProductDao dao) {
        this.scanner = scanner;
        this.dao = dao;
    }

    public void parse() {
        String command;
        do {
            System.out.println("Command: [dodaj, lista, update, usun, readById]");
            command = scanner.next();
            switch (command) {
                case "dodaj":
                    obslugaDodaj();
                    break;
                case "lista":
                    obslugaZwrocListe();
                    break;
                case "readById":
                    obslugaZwrocProduct();
                    break;
                case "usun":
                    obslugaUsun();
                    break;
                case "update":
                    obslugaAktualizuj();
                    break;
            }
        } while (!command.equalsIgnoreCase("koniec"));
    }

    private void obslugaAktualizuj() {
        System.out.println("Podaj id produktu, który chcesz aktualizować");
        Long id = scanner.nextLong();
        Optional<Product> optionalProduct = dao.zwrocProduct(id);
        if (optionalProduct.isPresent()) {
            System.out.println("Co zmieniamy, nazwę czy cenę");
            String output = scanner.next();
            Product productAktualizowany = optionalProduct.get();
            switch (output) {
                case "imie":
                    System.out.println("Podaj nazwę " + optionalProduct);
                    String name = scanner.next();
                    productAktualizowany.setName(name);
                    break;
                case "nazwisko":
                    System.out.println("Podaj cenę " + optionalProduct);
                    Double price = scanner.nextDouble();
                    productAktualizowany.setPrice(price);
                    break;
            }

            dao.updateProduct(productAktualizowany);
            System.out.println("Dokonano aktualizacji danych dla produktu " + productAktualizowany);
        }
    }

    private void obslugaUsun() {
        System.out.println("Podaj id produktu");
        Long id = scanner.nextLong();
        Optional<Product> optionalProduct = dao.zwrocProduct(id);
        Product product = null;
        if (optionalProduct.isPresent()) {
            product = optionalProduct.get();
            dao.usunProduct(product);
        }
        System.out.println("Usunięto product  " + product);
    }

    private void obslugaZwrocProduct() {
        System.out.println("Podaj id produktu");
        Long id = scanner.nextLong();
        Optional<Product> optionalProduct = dao.zwrocProduct(id);
        System.out.println("Szukany product to " + optionalProduct);
    }

    private void obslugaZwrocListe() {
        List<Product> productList = dao.zwrocListeProducts();
        for (Product product : productList) {
            System.out.println(product);
        }

        System.out.println();
    }

    private void obslugaDodaj() {
        System.out.println("Podaj nazwę:");
        String name = scanner.next();

        System.out.println("Podaj cenę:");
        Double price = scanner.nextDouble();

        System.out.println("Podaj producenta:");
        String producent = scanner.next();

        LocalDate expiryDate = loadExpiryDateFromUser();

        System.out.println("Podaj ilość:");
        Double quantity = scanner.nextDouble();

        ProductUnit productUnit = loadProductUnitFromUser();

        Product product = new Product(null, name, price, producent, expiryDate, quantity, productUnit);
        dao.dodajProduct(product);

    }

    private ProductUnit loadProductUnitFromUser() {
        ProductUnit productUnit = null;
        do {
            try {
                System.out.println("Podaj jednostkę:");
                String unitString = scanner.next();

                productUnit = ProductUnit.valueOf(unitString.toUpperCase());
            } catch (IllegalArgumentException iae) {
                System.err.println("Przyjęto jednostkę spoza dostępnej puli: ...");
            }
        } while (productUnit == null);
        return productUnit;
    }

    private LocalDate loadExpiryDateFromUser() {
        LocalDate expiryDate = null;
        do {
            try {
                System.out.println("Podaj datę ostatecznej przydatności:");
                String expiryDateString = scanner.next();

                expiryDate = LocalDate.parse(expiryDateString, FORMATTER);

                LocalDate today = LocalDate.now();
                if (expiryDate.isBefore(today)) {
                    // błąd, expiry date jest przed dzisiejszym dniem
                    throw new IllegalArgumentException("Podana data już mineła");
                }

            } catch (IllegalArgumentException | DateTimeException iae) {
                expiryDate = null;
                System.err.println("Niewłaściwy format daty, podaj datę w formacie: yyyy-MM-dd");
            }
        } while (expiryDate == null);
        return expiryDate;
    }
}
    
    
    
    
            