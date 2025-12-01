package org.przyklad.samochod;

public class Silnik extends Komponent {
    private int maxObroty;
    private int obroty;
    private boolean wlaczony;

    public Silnik(String producent, String model, int maxObroty) {
        super(producent, model);
        this.maxObroty = maxObroty;
        this.obroty = 0;
        this.wlaczony = false;
    }

    public void uruchom() {
        wlaczony = true;
        obroty = 800; // Wolne obroty
    }

    public void zatrzymaj() {
        wlaczony = false;
        obroty = 0;
    }

    public void zwiekszObroty() {
        if (wlaczony && obroty < maxObroty) obroty += 500;
    }

    public void zmniejszObroty() {
        if (wlaczony && obroty > 800) obroty -= 500;
    }

    public int getObroty() { return obroty; }
    public int getMaxObroty() { return maxObroty; }
}