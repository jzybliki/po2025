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

    public static ObservableList<Samochod> listaSamochodow = FXCollections.observableArrayList();
    private static HelloController instancja;

    // GUI Elementy
    @FXML private ComboBox<String> carComboBox;
    @FXML private Button addNewButton;

    // Pola tekstowe - Samochód
    @FXML private TextField modelTextField;
    @FXML private TextField registrationTextField;
    @FXML private TextField weightTextField;
    @FXML private TextField speedTextField; // Dystans/Pozycja

    // Pola tekstowe - Skrzynia
    @FXML private TextField gearboxNameTextField;
    @FXML private TextField gearTextField;

    // Pola tekstowe - Silnik
    @FXML private TextField engineNameTextField;
    @FXML private TextField rpmTextField;

    // Pola tekstowe - Sprzęgło
    @FXML private TextField clutchNameTextField;
    @FXML private TextField clutchStateTextField;

    // Przyciski sterowania
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button genericCarButton;
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
        System.out.println("HelloController initialized");

        // 1. Ładowanie ikony (zgodnie z instrukcją Lab 9)
        try {
            // Uwaga: upewnij się, że plik car-icon.png jest w resources/org/przyklad/samochodgui/
            // Instrukcja sugeruje ścieżkę /images/car.png, ale dostosowuję do Twojej struktury plików (car-icon.png obok kontrolera)
            if (getClass().getResource("car-icon.png") != null) {
                Image carImage = new Image(getClass().getResource("car-icon.png").toExternalForm());

                System.out.println("Image width: " + carImage.getWidth() + ", height: " + carImage.getHeight());

                if (carImageView != null) {
                    carImageView.setImage(carImage);
                    carImageView.setFitWidth(150); // Dostosowane wymiary
                    carImageView.setFitHeight(90);
                    carImageView.setTranslateX(0);
                    carImageView.setTranslateY(0);
                    carImageView.setLayoutY(30);
                    carImageView.setTranslateX(0);
                }
            }
        } catch (Exception e) {
            System.out.println("Brak ikony.");
        }

        // 2. Ustawienie pól jako tylko do odczytu
        setFieldsNotEditable();

        // 3. Dodanie przykładowych aut na start
        if(listaSamochodow.isEmpty()) {
            listaSamochodow.add(new Samochod(
                    "KRA 1234", "Fiat 126p", 120,
                    new Silnik("Fiat", "650cc", 5000, 100, 1000),
                    new SkrzyniaBiegow("Fiat", "Manual 4", 4, 40, 500),
                    new Sprzeglo("Valeo", "Std", 10, 200),
                    new Pozycja(0, 0)
            ));
        }

        // 4. Konfiguracja ComboBox
        if (carComboBox != null) {
            updateCarListCombo();
            carComboBox.setOnAction(e -> wybierzSamochod());
        }

        // 5. Podpięcie akcji przycisków
        if (addNewButton != null) addNewButton.setOnAction(e -> openAddCarWindow());

        if (startButton != null) startButton.setOnAction(e -> actionStartEngine());
        if (stopButton != null) stopButton.setOnAction(e -> actionBrake());
        if (genericCarButton != null) genericCarButton.setOnAction(e -> actionDriveLoop());

        if (gearUpButton != null) gearUpButton.setOnAction(e -> {
            if(aktualnySamochod != null) {
                aktualnySamochod.zwiekszBieg();
                System.out.println("Zwieksz bieg"); // Debug z instrukcji
                refresh();
            }
        });
        if (gearDownButton != null) gearDownButton.setOnAction(e -> {
            if(aktualnySamochod != null) {
                aktualnySamochod.zmniejszBieg();
                refresh();
            }
        });

        if (clutchPressButton != null) clutchPressButton.setOnAction(e -> {
            if(aktualnySamochod != null) {
                aktualnySamochod.getSprzeglo().wcisnij();
                refresh();
            }
        });
        if (clutchReleaseButton != null) clutchReleaseButton.setOnAction(e -> {
            if(aktualnySamochod != null) {
                aktualnySamochod.getSprzeglo().zwolnij();
                refresh();
            }
        });

        if (throttleUpButton != null) throttleUpButton.setOnAction(e -> {
            if(aktualnySamochod != null) {
                aktualnySamochod.zwiekszObroty();
                refresh();
            }
        });
        if (throttleDownButton != null) throttleDownButton.setOnAction(e -> {
            if(aktualnySamochod != null) {
                aktualnySamochod.zmniejszObroty();
                refresh();
            }
        });

        stworzTimer();
    }

    private void setFieldsNotEditable() {
        if(modelTextField != null) modelTextField.setEditable(false);
        if(registrationTextField != null) registrationTextField.setEditable(false);
        if(weightTextField != null) weightTextField.setEditable(false);
        if(speedTextField != null) speedTextField.setEditable(false);

        if(engineNameTextField != null) engineNameTextField.setEditable(false);
        if(rpmTextField != null) rpmTextField.setEditable(false);
        if(gearboxNameTextField != null) gearboxNameTextField.setEditable(false);
        if(gearTextField != null) gearTextField.setEditable(false);
        if(clutchNameTextField != null) clutchNameTextField.setEditable(false);
        if(clutchStateTextField != null) clutchStateTextField.setEditable(false);
    }

    // --- LOGIKA JAZDY CIĄGŁEJ ---
    private void stworzTimer() {
        timerJazdy = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (aktualnySamochod != null) {
                    aktualnySamochod.jedz();
                    refresh();
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
            timerJazdy.stop();
            czyJedzie = false;
        } else {
            if (aktualnySamochod != null) aktualnySamochod.wylacz();
        }
        refresh();
    }

    private void actionStartEngine() {
        if (aktualnySamochod != null) {
            aktualnySamochod.wlacz();
            refresh();
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

    // Metoda wywoływana z DodajSamochodController
    public static void addCarFromForm(String model, String reg, double weight, int maxSpeed) {
        // Tworzymy auto z domyślnymi komponentami, ponieważ formularz z instrukcji ich nie uwzględnia
        Samochod s = new Samochod(
                reg, model, maxSpeed,
                new Silnik("Generic", "Std Engine", 6000),
                new SkrzyniaBiegow("Generic", "5-Speed", 5),
                new Sprzeglo("Generic", "Std Clutch"),
                new Pozycja(0, 0)
        );

        listaSamochodow.add(s);
        if (instancja != null) instancja.updateCarListCombo();
    }

    private void updateCarListCombo() {
        if (carComboBox == null) return;
        carComboBox.getItems().clear();
        for (Samochod s : listaSamochodow) {
            carComboBox.getItems().add(s.getModel());
        }
    }

    private void wybierzSamochod() {
        int index = carComboBox.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < listaSamochodow.size()) {
            aktualnySamochod = listaSamochodow.get(index);
            if(carImageView != null) carImageView.setLayoutX(0); // Reset pozycji
            refresh();
        }
    }

    // --- METODA REFRESH WYMAGANA PRZEZ INSTRUKCJĘ ---
    private void refresh() {
        if (aktualnySamochod == null) return;

        // Dane Samochodu
        if(modelTextField != null) modelTextField.setText(aktualnySamochod.getModel());
        if(registrationTextField != null) registrationTextField.setText(aktualnySamochod.getNrRejest());
        if(weightTextField != null) weightTextField.setText(String.valueOf(aktualnySamochod.getWagaCalkowita()));
        if(speedTextField != null) speedTextField.setText(String.format("%.2f", aktualnySamochod.getPozycja().getX()));

        // Dane komponentów
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

        aktualizujPrzyciski();
        aktualizujPozycjeObrazka();
    }

    private void aktualizujPozycjeObrazka() {
        if (carImageView != null) {
            double przesuniecie = (aktualnySamochod.getPozycja().getX() / 5.0) % 600;
            carImageView.setTranslateX(przesuniecie);
        }
    }

    private void aktualizujPrzyciski() {
        boolean silnikOn = aktualnySamochod.getSilnik().getObroty() > 0;
        boolean sprzegloOn = aktualnySamochod.getSprzeglo().getStanSprzegla();

        if(startButton != null) startButton.setDisable(silnikOn);
        if(stopButton != null) stopButton.setDisable(false);
        if(genericCarButton != null) genericCarButton.setDisable(czyJedzie || !silnikOn);
        if(throttleUpButton != null) throttleUpButton.setDisable(!silnikOn);
        if(throttleDownButton != null) throttleDownButton.setDisable(!silnikOn);
        if(clutchPressButton != null) clutchPressButton.setDisable(sprzegloOn);
        if(clutchReleaseButton != null) clutchReleaseButton.setDisable(!sprzegloOn);
    }
}