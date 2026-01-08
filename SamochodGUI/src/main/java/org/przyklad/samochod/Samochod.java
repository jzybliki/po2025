package org.przyklad.samochod;

public class Samochod {
    // Pola komponentów
    private Silnik silnik;
    private SkrzyniaBiegow skrzynia;
    private Sprzeglo sprzeglo;
    private Pozycja pozycja;

    // Pola własne samochodu (zgodnie z Lab 5 i 6)
    private String nrRejest;
    private String model;
    private int predkoscMax;
    private double wagaCalkowita;

    public Samochod(String nrRejest, String model, int predkoscMax,
                    Silnik silnik, SkrzyniaBiegow skrzynia, Sprzeglo sprzeglo, Pozycja pozycja) {
        this.nrRejest = nrRejest;
        this.model = model;
        this.predkoscMax = predkoscMax;

        this.silnik = silnik;
        this.skrzynia = skrzynia;
        this.sprzeglo = sprzeglo;
        this.pozycja = pozycja;

        // Obliczenie wagi całkowitej (suma komponentów + przykładowa karoseria)
        this.wagaCalkowita = silnik.getWaga() + skrzynia.getWaga() + sprzeglo.getWaga() + 800;
    }

    // Konstruktor uproszczony (zgodny ze starym kodem w kontrolerze, ale dodający domyślne dane)
    public Samochod(Silnik silnik, SkrzyniaBiegow skrzynia, Sprzeglo sprzeglo, Pozycja pozycja) {
        this("KR 12345", "Domyślny Model", 200, silnik, skrzynia, sprzeglo, pozycja);
    }

    public void wlacz() {
        silnik.uruchom();
    }

    public void wylacz() {
        silnik.zatrzymaj();
        skrzynia.zerujBieg(); // POPRAWKA: Lab 5 wymaga zerowania biegu przy wyłączaniu
    }

    public void jedz() {
        if (silnik.getObroty() > 0 && skrzynia.getAktualnyBieg() > 0 && !sprzeglo.getStanSprzegla()) {
            // Prędkość zależna od obrotów i biegu (uproszczona fizyka)
            double predkosc = (silnik.getObroty() * skrzynia.getAktualnyBieg()) / 4000.0;
            // Ograniczenie do prędkości max
            if (predkosc * 10 > predkoscMax) predkosc = predkoscMax / 10.0;

            pozycja.aktualizujPozycje(predkosc, 0);
        }
    }

    // Delegaty
    public void zwiekszBieg() { if(sprzeglo.getStanSprzegla()) skrzynia.zwiekszBieg(); }
    public void zmniejszBieg() { if(sprzeglo.getStanSprzegla()) skrzynia.zmniejszBieg(); }
    public void zwiekszObroty() { silnik.zwiekszObroty(); }
    public void zmniejszObroty() { silnik.zmniejszObroty(); }

    // Gettery
    public Silnik getSilnik() { return silnik; }
    public SkrzyniaBiegow getSkrzynia() { return skrzynia; }
    public Sprzeglo getSprzeglo() { return sprzeglo; }
    public Pozycja getPozycja() { return pozycja; }

    public String getNrRejest() { return nrRejest; }
    public String getModel() { return model; }
    public double getWagaCalkowita() { return wagaCalkowita; }
    public int getPredkoscMax() { return predkoscMax; }
}