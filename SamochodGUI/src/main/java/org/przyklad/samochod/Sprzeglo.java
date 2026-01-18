package org.przyklad.samochod;

public class Sprzeglo extends Komponent {
    // true = wciśnięte, false = puszczone
    private boolean stanSprzegla;

    public Sprzeglo(String producent, String model, double waga, double cena) {
        super(producent, model, waga, cena);
        this.stanSprzegla = false;
    }

    // Konstruktor uproszczony
    public Sprzeglo(String producent, String model) {
        this(producent, model, 20, 1000);
    }

    public void wcisnij() { stanSprzegla = true; }
    public void zwolnij() { stanSprzegla = false; }
    public boolean getStanSprzegla() { return stanSprzegla; } // żeby sprawdzić przy zmianie biegu
}