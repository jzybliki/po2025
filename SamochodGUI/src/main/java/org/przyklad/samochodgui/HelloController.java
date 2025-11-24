package org.przyklad.samochodgui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.przyklad.samochod.*;

public class HelloController {

    // ====== GUI ELEMENTY ======
    @FXML private ComboBox<String> carComboBox;

    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button gearUpButton;
    @FXML private Button gearDownButton;
    @FXML private Button clutchPressButton;
    @FXML private Button clutchReleaseButton;
    @FXML private Button driveButton;

    @FXML private TextField modelTextField;
    @FXML private TextField nrRejTextField;
    @FXML private TextField wagaTextField;
    @FXML private TextField predkoscTextField;

    @FXML private TextField skrzyniaNazwaTextField;
    @FXML private TextField skrzyniaCenaTextField;
    @FXML private TextField skrzyniaWagaTextField;
    @FXML private TextField skrzyniaBiegTextField;

    @FXML private TextField silnikNazwaTextField;
    @FXML private TextField silnikCenaTextField;
    @FXML private TextField silnikWagaTextField;
    @FXML private TextField silnikObrotyTextField;

    @FXML private TextField sprzegloNazwaTextField;
    @FXML private TextField sprzegloCenaTextField;
    @FXML private TextField sprzegloWagaTextField;
    @FXML private TextField sprzegloStanTextField;

    @FXML private AnchorPane mapPane;
    @FXML private ImageView carImageView;

    // ====== MODEL ======
    private Samochod aktualnySamochod;

    // Inicjalizacja
    @FXML
    private void initialize() {
        System.out.println("Kontroler zainicjalizowany.");
        carComboBox.getItems().addAll("Opel Astra", "BMW M3", "Fiat 126p");
    }

    // ====== OBSŁUGA KOMBOBOXA ======
    @FXML
    private void onCarComboBox() {
        String wybrany = carComboBox.getValue();
        if (wybrany == null) return;

        // Tworzymy przykładowy samochód
        Silnik silnik = new Silnik("Bosch", wybrany + " Silnik", 8000);
        SkrzyniaBiegow skrzynia = new SkrzyniaBiegow("ZF", wybrany + " Skrzynia", 6);
        Sprzeglo sprzeglo = new Sprzeglo("Valeo", "Standard");
        Pozycja pozycja = new Pozycja();

        aktualnySamochod = new Samochod(silnik, skrzynia, sprzeglo, pozycja);

        odswiezGUI();
    }

    // ====== OBSŁUGA PRZYCISKÓW ======
    @FXML private void onStartButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.wlacz();
        odswiezGUI();
    }

    @FXML private void onStopButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.wylacz();
        odswiezGUI();
    }

    @FXML private void onGearUpButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.zwiekszBieg();
        odswiezGUI();
    }

    @FXML private void onGearDownButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.zmniejszBieg();
        odswiezGUI();
    }

    @FXML private void onClutchPressButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.getSprzeglo().wcisnij();
        odswiezGUI();
    }

    @FXML private void onClutchReleaseButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.getSprzeglo().zwolnij();
        odswiezGUI();
    }

    @FXML private void onDriveButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.jedz();
        odswiezGUI();
    }

    // ====== AKTUALIZACJA GUI ======
    private void odswiezGUI() {
        if (aktualnySamochod == null) return;

        // Silnik
        silnikNazwaTextField.setText(aktualnySamochod.getSilnik().getModel());
        silnikObrotyTextField.setText("" + aktualnySamochod.getSilnik().getObroty());

        // Skrzynia
        skrzyniaBiegTextField.setText("" + aktualnySamochod.getSkrzynia().getAktualnyBieg());

        // Sprzęgło
        sprzegloStanTextField.setText(
                aktualnySamochod.getSprzeglo().getStanSprzegla() ? "Wciśnięte" : "Zwolnione"
        );

        // Pozycja samochodu
        predkoscTextField.setText(
                "(" + aktualnySamochod.getPozycja().getX() + ", " +
                        aktualnySamochod.getPozycja().getY() + ")"
        );
    }
}
