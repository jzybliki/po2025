package org.przyklad.samochod;

public class Komponent {
    private String producent;
    private String model;
    private double waga;
    private double cena;

    public Komponent(String producent, String model, double waga, double cena) {
        this.producent = producent;
        this.model = model;
        this.waga = waga;
        this.cena = cena;
    }

    // Konstruktor uproszczony (dla kompatybilności, jeśli nie chcesz podawać wagi/ceny)
    public Komponent(String producent, String model) {
        this(producent, model, 0, 0);
    }

    public String getProducent() { return producent; }
    public String getModel() { return model; }
    public double getWaga() { return waga; }
    public double getCena() { return cena; }
}