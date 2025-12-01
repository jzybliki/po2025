package org.przyklad.samochod;

public class Samochod {
    private Silnik silnik;
    private SkrzyniaBiegow skrzynia;
    private Sprzeglo sprzeglo;
    private Pozycja pozycja;

    public Samochod(Silnik silnik, SkrzyniaBiegow skrzynia, Sprzeglo sprzeglo, Pozycja pozycja) {
        this.silnik = silnik;
        this.skrzynia = skrzynia;
        this.sprzeglo = sprzeglo;
        this.pozycja = pozycja;
    }

    public void wlacz() {
        silnik.uruchom();
    }

    public void wylacz() {
        silnik.zatrzymaj();
    }

    // Metoda wywoływana cyklicznie przez timer
    public void jedz() {
        // Jedziemy tylko jak: Silnik działa, Bieg > 0, Sprzęgło puszczone
        if (silnik.getObroty() > 0 && skrzynia.getAktualnyBieg() > 0 && !sprzeglo.getStanSprzegla()) {
            double predkosc = (silnik.getObroty() * skrzynia.getAktualnyBieg()) / 4000.0;
            pozycja.aktualizujPozycje(predkosc, 0);
        }
    }

    public void zwiekszBieg() {
        if(sprzeglo.getStanSprzegla()) skrzynia.zwiekszBieg();
    }

    public void zmniejszBieg() {
        if(sprzeglo.getStanSprzegla()) skrzynia.zmniejszBieg();
    }

    public void zwiekszObroty() { silnik.zwiekszObroty(); }
    public void zmniejszObroty() { silnik.zmniejszObroty(); }

    public Silnik getSilnik() { return silnik; }
    public SkrzyniaBiegow getSkrzynia() { return skrzynia; }
    public Sprzeglo getSprzeglo() { return sprzeglo; }
    public Pozycja getPozycja() { return pozycja; }
}