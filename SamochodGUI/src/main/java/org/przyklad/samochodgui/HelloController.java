package org.przyklad.samochodgui;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.przyklad.samochod.*;

import java.io.IOException;

public class HelloController {

    // Lista aut dostępna dla obu okien
    public static ObservableList<Samochod> listaSamochodow = FXCollections.observableArrayList();
    private static HelloController instancja;

    // GUI Elementy
    @FXML private ComboBox<String> carComboBox;
    @FXML private Button addNewButton;

    // Pola tekstowe (Info)
    @FXML private TextField modelTextField;
    @FXML private TextField registrationTextField;
    @FXML private TextField weightTextField;
    @FXML private TextField speedTextField; // Tutaj wyświetlamy pozycję (dystans)

    @FXML private TextField gearboxNameTextField;
    @FXML private TextField gearTextField;

    @FXML private TextField engineNameTextField;
    @FXML private TextField rpmTextField;

    @FXML private TextField clutchNameTextField;
    @FXML private TextField clutchStateTextField;

    // Przyciski sterowania
    @FXML private Button startButton;
    @FXML private Button stopButton;      // HAMULEC
    @FXML private Button genericCarButton; // JEDŹ
    @FXML private Button gearUpButton;
    @FXML private Button gearDownButton;
    @FXML private Button clutchPressButton;
    @FXML private Button clutchReleaseButton;
    @FXML private Button throttleUpButton;
    @FXML private Button throttleDownButton;

    @FXML private ImageView carImageView;

    private Samochod aktualnySamochod;
    private AnimationTimer timerJazdy;
    private boolean czyJedzie = false;

    public HelloController() {
        instancja = this;
    }

    @FXML
    public void initialize() {
        System.out.println("Inicjalizacja kontrolera...");

        // 1. Ładowanie ikony (Lab 9)
        try {
            if (getClass().getResource("car-icon.png") != null) {
                Image carImage = new Image(getClass().getResource("car-icon.png").toExternalForm());
                if (carImageView != null) carImageView.setImage(carImage);
            }
        } catch (Exception e) {
            System.out.println("Brak ikony: car-icon.png");
        }

        // 2. Dodanie przykładowych aut na start
        if(listaSamochodow.isEmpty()) {
            listaSamochodow.add(new Samochod(
                    new Silnik("Fiat", "126p Engine", 5000),
                    new SkrzyniaBiegow("Fiat", "Manual 4", 4),
                    new Sprzeglo("Valeo", "Std"),
                    new Pozycja(0, 0)
            ));
            listaSamochodow.add(new Samochod(
                    new Silnik("BMW", "M3 V8", 8000),
                    new SkrzyniaBiegow("ZF", "Sport 6", 6),
                    new Sprzeglo("Sachs", "Sport"),
                    new Pozycja(0, 0)
            ));
        }

        // 3. Konfiguracja ComboBox
        if (carComboBox != null) {
            updateCarListCombo();
            carComboBox.setOnAction(e -> wybierzSamochod());
        }

        // 4. Podpięcie akcji przycisków
        if (addNewButton != null) addNewButton.setOnAction(e -> openAddCarWindow());

        if (startButton != null) startButton.setOnAction(e -> actionStartEngine());
        if (stopButton != null) stopButton.setOnAction(e -> actionBrake());
        if (genericCarButton != null) genericCarButton.setOnAction(e -> actionDriveLoop());

        if (gearUpButton != null) gearUpButton.setOnAction(e -> {
            if(aktualnySamochod != null) { aktualnySamochod.zwiekszBieg(); odswiezGUI(); }
        });
        if (gearDownButton != null) gearDownButton.setOnAction(e -> {
            if(aktualnySamochod != null) { aktualnySamochod.zmniejszBieg(); odswiezGUI(); }
        });

        if (clutchPressButton != null) clutchPressButton.setOnAction(e -> {
            if(aktualnySamochod != null) { aktualnySamochod.getSprzeglo().wcisnij(); odswiezGUI(); }
        });
        if (clutchReleaseButton != null) clutchReleaseButton.setOnAction(e -> {
            if(aktualnySamochod != null) { aktualnySamochod.getSprzeglo().zwolnij(); odswiezGUI(); }
        });

        if (throttleUpButton != null) throttleUpButton.setOnAction(e -> {
            if(aktualnySamochod != null) { aktualnySamochod.zwiekszObroty(); odswiezGUI(); }
        });
        if (throttleDownButton != null) throttleDownButton.setOnAction(e -> {
            if(aktualnySamochod != null) { aktualnySamochod.zmniejszObroty(); odswiezGUI(); }
        });

        stworzTimer();
        zablokujWszystkiePrzyciski();
    }

    // --- LOGIKA JAZDY CIĄGŁEJ ---
    private void stworzTimer() {
        timerJazdy = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (aktualnySamochod != null) {
                    aktualnySamochod.jedz(); // Fizyka jazdy
                    odswiezGUI();            // Aktualizacja ekranu
                }
            }
        };
    }

    private void actionDriveLoop() {
        if (aktualnySamochod != null && !czyJedzie) {
            timerJazdy.start();
            czyJedzie = true;
        }
    }

    private void actionBrake() {
        if (czyJedzie) {
            timerJazdy.stop(); // Zatrzymuje symulację
            czyJedzie = false;
        } else {
            if (aktualnySamochod != null) aktualnySamochod.wylacz(); // Gasi silnik
        }
        odswiezGUI();
    }

    private void actionStartEngine() {
        if (aktualnySamochod != null) {
            aktualnySamochod.wlacz();
            odswiezGUI();
        }
    }

    // --- OBSŁUGA OKNA DODAWANIA ---
    public void openAddCarWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DodajSamochod.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dodaj nowy samochód");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addCarToList(Samochod s) {
        listaSamochodow.add(s);
        if (instancja != null) instancja.updateCarListCombo();
    }

    private void updateCarListCombo() {
        if (carComboBox == null) return;
        carComboBox.getItems().clear();
        for (Samochod s : listaSamochodow) {
            carComboBox.getItems().add(s.getSilnik().getModel());
        }
    }

    private void wybierzSamochod() {
        int index = carComboBox.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < listaSamochodow.size()) {
            aktualnySamochod = listaSamochodow.get(index);
            // Reset pozycji wizualnej
            if(carImageView != null) carImageView.setLayoutX(20);
            odswiezGUI();
        }
    }

    // --- REFRESH ---
    private void odswiezGUI() {
        if (aktualnySamochod == null) return;

        // Wypisywanie danych
        if(modelTextField != null) modelTextField.setText(aktualnySamochod.getSilnik().getModel()); // Używam modelu silnika jako modelu auta dla uproszczenia
        if(engineNameTextField != null) engineNameTextField.setText(aktualnySamochod.getSilnik().getModel());
        if(rpmTextField != null) rpmTextField.setText(String.valueOf(aktualnySamochod.getSilnik().getObroty()));

        if(gearboxNameTextField != null) gearboxNameTextField.setText(aktualnySamochod.getSkrzynia().getModel());
        if(gearTextField != null) gearTextField.setText(String.valueOf(aktualnySamochod.getSkrzynia().getAktualnyBieg()));

        if(clutchNameTextField != null) clutchNameTextField.setText(aktualnySamochod.getSprzeglo().getModel());
        if(clutchStateTextField != null) {
            boolean wcisniete = aktualnySamochod.getSprzeglo().getStanSprzegla();
            clutchStateTextField.setText(wcisniete ? "Wciśnięte" : "Zwolnione");
            clutchStateTextField.setStyle(wcisniete ? "-fx-text-fill: green;" : "-fx-text-fill: black;");
        }

        if(speedTextField != null) {
            speedTextField.setText(String.format("%.2f m", aktualnySamochod.getPozycja().getX()));
        }

        // Ruch obrazka
        if (carImageView != null) {
            double przesuniecie = (aktualnySamochod.getPozycja().getX() / 5.0) % 600;
            carImageView.setLayoutX(20 + przesuniecie);
        }

        aktualizujPrzyciski();
    }

    private void aktualizujPrzyciski() {
        boolean silnikOn = aktualnySamochod.getSilnik().getObroty() > 0;
        boolean sprzegloOn = aktualnySamochod.getSprzeglo().getStanSprzegla();

        if(startButton != null) startButton.setDisable(silnikOn);
        if(stopButton != null) stopButton.setDisable(false); // Hamulec zawsze działa
        if(genericCarButton != null) genericCarButton.setDisable(czyJedzie || !silnikOn);

        if(throttleUpButton != null) throttleUpButton.setDisable(!silnikOn);
        if(throttleDownButton != null) throttleDownButton.setDisable(!silnikOn);

        if(clutchPressButton != null) clutchPressButton.setDisable(sprzegloOn);
        if(clutchReleaseButton != null) clutchReleaseButton.setDisable(!sprzegloOn);

        if(gearUpButton != null) gearUpButton.setDisable(false);
        if(gearDownButton != null) gearDownButton.setDisable(false);
    }

    private void zablokujWszystkiePrzyciski() {
        if(startButton != null) startButton.setDisable(true);
        if(stopButton != null) stopButton.setDisable(true);
        if(genericCarButton != null) genericCarButton.setDisable(true);
        if(gearUpButton != null) gearUpButton.setDisable(true);
    }
}