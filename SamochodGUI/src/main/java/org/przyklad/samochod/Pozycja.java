package org.przyklad.samochod;

public class Pozycja {
    private double x;
    private double y;

    // Konstruktor bez argumentów
    public Pozycja() {
        this.x = 0;
        this.y = 0;
    }

    // Konstruktor z argumentami
    public Pozycja(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void aktualizujPozycje(double deltaX, double deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public String getPozycja() {
        return "(" + x + ", " + y + ")";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
