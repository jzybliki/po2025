package symulator;

public class Sprzeglo extends Komponent {
    private boolean stanSprzegla;

    public Sprzeglo(String producent, String model) {
        super(producent, model);
        this.stanSprzegla = false;
    }

    public void wcisnij() {
        stanSprzegla = true;
    }

    public void zwolnij() {
        stanSprzegla = false;
    }

    public boolean getStanSprzegla() {
        return stanSprzegla;
    }
}
