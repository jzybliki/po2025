package org.przyklad.samochod;

public class Komponent {
    // Pola wspólne dla wszystkich części
    private String producent;
    private String model;
    private double waga;
    private double cena;

    // Konstruktor ogólny
    public Komponent(String producent, String model, double waga, double cena) {
        this.producent = producent;
        this.model = model;
        this.waga = waga;
        this.cena = cena;
    }

    // Konstruktor uproszczony (dla kompatybilności, bez podawania wagi/ceny)
    public Komponent(String producent, String model) {
        this(producent, model, 0, 0);
    }

    // Gettery potrzebne do wyświetlania w GUI
    public String getProducent() { return producent; }
    public String getModel() { return model; }
    public double getWaga() { return waga; }
    public double getCena() { return cena; }
}