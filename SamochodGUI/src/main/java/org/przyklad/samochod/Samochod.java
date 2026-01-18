package org.przyklad.samochod;

import java.util.ArrayList;
import java.util.List;

// Dziedziczy po Thread -> to jest ten osobny wątek do jazdy
public class Samochod extends Thread {
    // Pola komponentów
    private Silnik silnik;
    private SkrzyniaBiegow skrzynia;
    private Sprzeglo sprzeglo;
    private Pozycja pozycja;

    // Cel podróży i logika wątku
    private Pozycja cel;
    private boolean aktywny = true; // Flaga do zatrzymania wątku przy zamykaniu aplikacji

    // Pola własne samochodu
    private String nrRejest;
    private String model;
    private int predkoscMax;
    private double wagaCalkowita;

    // Lista obserwatorów (GUI)
    private List<Listener> listeners = new ArrayList<>();

    public Samochod(String nrRejest, String model, int predkoscMax, double wagaPodstawowa,
                    Silnik silnik, SkrzyniaBiegow skrzynia, Sprzeglo sprzeglo, Pozycja pozycja) {
        this.nrRejest = nrRejest;
        this.model = model;
        this.predkoscMax = predkoscMax;

        this.silnik = silnik;
        this.skrzynia = skrzynia;
        this.sprzeglo = sprzeglo;
        this.pozycja = pozycja;

        this.wagaCalkowita = silnik.getWaga() + skrzynia.getWaga() + sprzeglo.getWaga() + wagaPodstawowa;

        // Uruchomienie wątku od razu po stworzeniu auta
        start();
    }

    // --- Wzorzec Obserwator ---
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    // Daj znać GUI, że coś się zmieniło
    private void notifyListeners() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

    // --- Logika Wątku (Jazda do celu) ---
    public void jedzDo(Pozycja cel) {
        this.cel = cel;
        System.out.println("Jadę do: " + cel.getX() + ", " + cel.getY());
    }

    @Override
    public void run() {
        double deltat = 0.0166; // Krok czasowy (s), 60 FPS

        while (aktywny) {
            try {
                Thread.sleep(16); // Symulacja czasu rzeczywistego

                // Warunki jazdy: jest cel, silnik chodzi, sprzęgło puszczone
                if (cel != null && silnik.getObroty() > 0 && !sprzeglo.getStanSprzegla()) {
                    double dx = cel.getX() - pozycja.getX();
                    double dy = cel.getY() - pozycja.getY();
                    double odleglosc = Math.sqrt(dx*dx + dy*dy);

                    if (odleglosc > 1.0) { // Jeśli jesteśmy dalej niż 1 jednostka od celu
                        // Aktualna prędkość (zależna od obrotów i biegu)
                        double predkoscAktualna = (silnik.getObroty() * skrzynia.getAktualnyBieg()) / 4000.0 * 50.0; // *50 dla widoczności ruchu
                        if (predkoscAktualna > predkoscMax) predkoscAktualna = predkoscMax;

                        // Przesunięcie
                        double moveX = predkoscAktualna * deltat * (dx / odleglosc);
                        double moveY = predkoscAktualna * deltat * (dy / odleglosc);

                        pozycja.aktualizujPozycje(moveX, moveY);

                        // Powiadom GUI o zmianie, odświeżenie mapy
                        notifyListeners();
                    } else {
                        // Dojechaliśmy
                        cel = null;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void zakonczWatek() {
        aktywny = false;
    }

    // --- Metody sterowania ---
    public void wlacz() { silnik.uruchom(); notifyListeners(); }
    public void wylacz() { silnik.zatrzymaj(); skrzynia.zerujBieg(); notifyListeners(); }

    // Zmiana biegów z WALIDACJĄ (rzuca wyjątek jak brak sprzęgła)
    public void zwiekszBieg() throws SamochodException {
        if (!sprzeglo.getStanSprzegla()) {
            throw new SamochodException("Nie można zmienić biegu bez wciśniętego sprzęgła!");
        }
        skrzynia.zwiekszBieg();
        notifyListeners();
    }
    public void zmniejszBieg() throws SamochodException {
        if (!sprzeglo.getStanSprzegla()) {
            throw new SamochodException("Nie można zmienić biegu bez wciśniętego sprzęgła!");
        }
        skrzynia.zmniejszBieg();
        notifyListeners();
    }
    // Gazowanie
    public void zwiekszObroty() { silnik.zwiekszObroty(); notifyListeners(); }
    public void zmniejszObroty() { silnik.zmniejszObroty(); notifyListeners(); }

    // Gettery
    public Silnik getSilnik() { return silnik; }
    public SkrzyniaBiegow getSkrzynia() { return skrzynia; }
    public Sprzeglo getSprzeglo() { return sprzeglo; }
    public Pozycja getPozycja() { return pozycja; }
    public String getNrRejest() { return nrRejest; }
    public String getModel() { return model; }
    public double getWagaCalkowita() { return wagaCalkowita; }
    public int getPredkoscMax() { return predkoscMax; }

    @Override
    public String toString() {
        return model + " (" + nrRejest + ")"; // Dla ładnego wyświetlania w ComboBox
    }
}