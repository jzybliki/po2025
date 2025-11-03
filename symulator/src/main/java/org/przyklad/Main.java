package org.przyklad;

import symulator.*;

public class Main {
    public static void main(String[] args) {
        // Tworzymy komponenty samochodu
        Sprzeglo sprzeglo = new Sprzeglo("BMW", "Clutch");
        Silnik silnik = new Silnik("BMW", "V8", 7000);
        SkrzyniaBiegow skrzynia = new SkrzyniaBiegow("BMW", "Skrzynia Manualna", 6);
        Pozycja pozycja = new Pozycja(0, 0);

        // Tworzymy samochod
        Samochod bmw = new Samochod(silnik, skrzynia, pozycja);

        // Testujemy wlaczenie silnika
        bmw.wlacz();
        System.out.println("Silnik wlaczony. Obroty: " + silnik.getObroty());

        // Testujemy zmiane biegow
        skrzynia.zwiekszBieg();
        System.out.println("Aktualny bieg: " + skrzynia.getAktualnyBieg());

        skrzynia.zmniejszBieg();
        System.out.println("Aktualny bieg po zmniejszeniu: " + skrzynia.getAktualnyBieg());

        // Testujemy aktualizacje pozycji
        pozycja.aktualizujPozycje(10, 5);
        System.out.println("Aktualna pozycja samochodu: " + pozycja.getPozycja());

        // Testujemy wylaczenie silnika
        bmw.wylacz();
        System.out.println("Silnik wylaczony. Obroty: " + silnik.getObroty());
        System.out.println("Bieg po wylaczeniu: " + skrzynia.getAktualnyBieg());
    }
}

