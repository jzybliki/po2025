package org.przyklad.samochod;

public class SkrzyniaBiegow extends Komponent {
    private int aktualnyBieg;
    private int iloscBiegow;
    private double aktualnePrzelozenie; // Zgodnie z diagramem

    public SkrzyniaBiegow(String producent, String model, int iloscBiegow, double waga, double cena) {
        super(producent, model, waga, cena);
        this.iloscBiegow = iloscBiegow;
        this.aktualnyBieg = 0;
        this.aktualnePrzelozenie = 0.0;
    }

    // Konstruktor uproszczony
    public SkrzyniaBiegow(String producent, String model, int iloscBiegow) {
        this(producent, model, iloscBiegow, 50, 2000);
    }

    public void zwiekszBieg() {
        if (aktualnyBieg < iloscBiegow) {
            aktualnyBieg++;
            obliczPrzelozenie();
        }
    }

    public void zmniejszBieg() {
        if (aktualnyBieg > 0) {
            aktualnyBieg--;
            obliczPrzelozenie();
        }
    }

    public void zerujBieg() {
        aktualnyBieg = 0;
        obliczPrzelozenie();
    }

    private void obliczPrzelozenie() {
        if (aktualnyBieg == 0) aktualnePrzelozenie = 0.0;
        else aktualnePrzelozenie = 1.0 + (aktualnyBieg * 0.5); // Przykładowa logika
    }

    public int getAktualnyBieg() { return aktualnyBieg; }
    public int getIloscBiegow() { return iloscBiegow; }
    public double getAktPrzelozenie() { return aktualnePrzelozenie; }
}