package org.przyklad.samochod;

public class SkrzyniaBiegow extends Komponent {
    private int aktualnyBieg;
    private int iloscBiegow;
    private double aktualnePrzelozenie;

    public SkrzyniaBiegow(String producent, String model, int iloscBiegow, double waga, double cena) {
        super(producent, model, waga, cena);
        this.iloscBiegow = iloscBiegow;
        this.aktualnyBieg = 0; // 0 to luz
        this.aktualnePrzelozenie = 0.0;
    }

    public SkrzyniaBiegow(String producent, String model, int iloscBiegow) {
        this(producent, model, iloscBiegow, 50, 2000);
    }

    // Sama zmiana numerka biegu - walidacja sprzęgła jest w samochodzie
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

    // Przeliczenie biegu na mnożnik prędkości
    private void obliczPrzelozenie() {
        if (aktualnyBieg == 0) aktualnePrzelozenie = 0.0;
        else aktualnePrzelozenie = 1.0 + (aktualnyBieg * 0.5); // Przykładowa logika
    }

    public int getAktualnyBieg() { return aktualnyBieg; }
    public int getIloscBiegow() { return iloscBiegow; }
    public double getAktPrzelozenie() { return aktualnePrzelozenie; }
}