package org.przyklad.samochod;

public class Silnik extends Komponent {
    private int maxObroty;
    private int obroty;

    public Silnik(String producent, String model, int maxObroty) {
        super(producent, model);
        this.maxObroty = maxObroty;
        this.obroty = 0;
    }

    public void uruchom() {
        obroty = 1000; // wartosc poczatkowa, np. obroty biegu jalowego
    }

    public void zatrzymaj() {
        obroty = 0;
    }

    public int getObroty() {
        return obroty;
    }

    public int getMaxObroty() {
        return maxObroty;
    }
}
