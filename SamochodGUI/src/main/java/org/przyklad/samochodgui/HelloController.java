package org.przyklad.samochodgui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.przyklad.samochod.*;

import java.io.IOException;

// Główny "mózg" GUI. Implementuje Listener, żeby reagować na zmiany w samochodzie
public class HelloController implements Listener {

    // Lista aut
    private ObservableList<Samochod> listaSamochodow = FXCollections.observableArrayList();
    private Samochod aktualnySamochod;

    // GUI Elementy
    @FXML private ComboBox<Samochod> carComboBox; // Zmiana typu na <Samochod>
    @FXML private Button addNewButton; // dodawanie
    @FXML private Button deleteButton; // Przycisk usuwania
    @FXML private ImageView carImageView;

    // Panel mapy
    @FXML private Pane mapaPanel;

    // Pola tekstowe
    @FXML private TextField modelTextField;
    @FXML private TextField registrationTextField;
    @FXML private TextField weightTextField;
    @FXML private TextField speedTextField;
    @FXML private TextField gearboxNameTextField;
    @FXML private TextField gearTextField;
    @FXML private TextField engineNameTextField;
    @FXML private TextField rpmTextField;
    @FXML private TextField clutchNameTextField;
    @FXML private TextField clutchStateTextField;

    // Przyciski
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button genericCarButton;
    @FXML private Button gearUpButton;
    @FXML private Button gearDownButton;
    @FXML private Button clutchPressButton;
    @FXML private Button clutchReleaseButton;
    @FXML private Button throttleUpButton;
    @FXML private Button throttleDownButton;

    @FXML
    public void initialize() {
        System.out.println("HelloController initialized");
        // Blokada edycji pól - tylko do odczytu
        setFieldsNotEditable();

        // Ładowanie obrazka auta
        try {
            if (getClass().getResource("car-icon.png") != null) {
                Image carImage = new Image(getClass().getResource("car-icon.png").toExternalForm());
                if (carImageView != null) {
                    carImageView.setImage(carImage);
                    carImageView.setFitWidth(150);
                    carImageView.setFitHeight(90);
                    carImageView.setLayoutY(10);
                    carImageView.setTranslateX(0);
                }
            }
        } catch (Exception e) {
            System.out.println("Brak ikony.");
        }

        // 2. Obsługa wyboru z listy
        carComboBox.setItems(listaSamochodow);
        carComboBox.setOnAction(event -> {
            Samochod wybrany = carComboBox.getSelectionModel().getSelectedItem();
            if (wybrany != null) {
                // Odpinamy listener od starego auta
                if(aktualnySamochod != null) aktualnySamochod.removeListener(this);
                aktualnySamochod = wybrany;
                // Przypinamy listener do nowego
                aktualnySamochod.addListener(this);
                refresh();
            }
        });

        // Dodanie przykładowych aut na start
        Samochod s1 = new Samochod("KRA 1234", "Fiat 126p", 120, 800.0,
                new Silnik("Fiat", "650cc", 5000, 100, 1000),
                new SkrzyniaBiegow("Fiat", "Manual 4", 4, 40, 500),
                new Sprzeglo("Valeo", "Std", 10, 200),
                new Pozycja(20, 50));

        dodajSamochod(s1);
        aktualnySamochod = s1; // Ustawiamy Fiata jako aktualnie wybrane auto
        aktualnySamochod.addListener(this); // Rejestrujemy kontroler jako słuchacza
        refresh();

        // Podpinanie przycisków

        // dodawanie/usuwanie
        if (addNewButton != null) addNewButton.setOnAction(e -> openAddCarWindow());
        if (deleteButton != null) deleteButton.setOnAction(e -> usunSamochod());

        // start/stop silnika
        if (startButton != null) startButton.setOnAction(e -> { if(aktualnySamochod!=null) aktualnySamochod.wlacz(); });
        if (stopButton != null) stopButton.setOnAction(e -> { if(aktualnySamochod!=null) aktualnySamochod.wylacz(); });

        // Biegi(z obsługą wyjątków)
        if (gearUpButton != null) gearUpButton.setOnAction(e -> {
            if (aktualnySamochod != null) {
                try {
                    aktualnySamochod.zwiekszBieg(); // To teraz może rzucić błąd
                } catch (SamochodException ex) {
                    // Tutaj używamy Twojej gotowej metody do wyświetlania błędów
                    pokazBlad(ex.getMessage());
                }
            }
        });

        if (gearDownButton != null) gearDownButton.setOnAction(e -> {
            if (aktualnySamochod != null) {
                try {
                    aktualnySamochod.zmniejszBieg();
                } catch (SamochodException ex) {
                    pokazBlad(ex.getMessage());
                }
            }
        });

        // Sprzęgło
        if (clutchPressButton != null) clutchPressButton.setOnAction(e -> { if(aktualnySamochod!=null) aktualnySamochod.getSprzeglo().wcisnij(); });
        if (clutchReleaseButton != null) clutchReleaseButton.setOnAction(e -> { if(aktualnySamochod!=null) aktualnySamochod.getSprzeglo().zwolnij(); });

        //Gaz
        if (throttleUpButton != null) throttleUpButton.setOnAction(e -> { if(aktualnySamochod!=null) aktualnySamochod.zwiekszObroty(); });
        if (throttleDownButton != null) throttleDownButton.setOnAction(e -> { if(aktualnySamochod!=null) aktualnySamochod.zmniejszObroty(); });

        // klikanie w mapę - jedź do punktu
        if (mapaPanel != null) {
            mapaPanel.setOnMouseClicked(event -> {
                if (aktualnySamochod != null) {
                    double x = event.getX();
                    double y = event.getY();
                    aktualnySamochod.jedzDo(new Pozycja(x, y));
                }
            });
        }
    }

    // Metoda z interfejsu Listener
    @Override
    public void update() {
        // Zapewnienie, że aktualizacja GUI wykona się w wątku JavaFX, a nie wątku Samochód
        Platform.runLater(this::refresh);
    }

    // Odświeżanie wszystkich pól tekstowych
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
        if (carImageView != null && aktualnySamochod != null) {
            carImageView.setTranslateX(aktualnySamochod.getPozycja().getX());
            carImageView.setTranslateY(aktualnySamochod.getPozycja().getY());
        }
    }

    // Żeby nie dało się kliknąć czegoś, czego nie wolno (np. Start jak silnik już chodzi)
    private void aktualizujPrzyciski() {
        boolean silnikOn = aktualnySamochod.getSilnik().getObroty() > 0;
        boolean sprzegloOn = aktualnySamochod.getSprzeglo().getStanSprzegla();

        if(startButton != null) startButton.setDisable(silnikOn);
        if(stopButton != null) stopButton.setDisable(false);
        if(throttleUpButton != null) throttleUpButton.setDisable(!silnikOn);
        if(throttleDownButton != null) throttleDownButton.setDisable(!silnikOn);
        if(clutchPressButton != null) clutchPressButton.setDisable(sprzegloOn);
        if(clutchReleaseButton != null) clutchReleaseButton.setDisable(!sprzegloOn);
    }

    // Zarządzanie listą aut

    public void dodajSamochod(Samochod s) {
        listaSamochodow.add(s);
        carComboBox.getSelectionModel().select(s);
    }

    private void usunSamochod() {
        if (aktualnySamochod != null) {
            aktualnySamochod.zakonczWatek(); // Zatrzymujemy wątek usuwanego auta
            aktualnySamochod.removeListener(this);
            listaSamochodow.remove(aktualnySamochod);

            if (!listaSamochodow.isEmpty()) {
                carComboBox.getSelectionModel().selectFirst();
            } else {
                aktualnySamochod = null;
                // Można wyczyścić pola GUI tutaj
            }
        }
    }

    // --- OKNO DODAWANIA ---
    public void openAddCarWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DodajSamochod.fxml"));
            Parent root = loader.load();

            // Pobieramy kontroler okna dodawania i przekazujemy mu referencję do 'this'
            DodajSamochodController addController = loader.getController();
            addController.setMainController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dodaj nowy samochód");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            pokazBlad("Nie udało się otworzyć okna dodawania.");
        }
    }

    // Metoda pomocnicza do błędów
    public void pokazBlad(String wiadomosc) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(wiadomosc);
        alert.showAndWait();
    }

    private void setFieldsNotEditable() {
        if(modelTextField != null) modelTextField.setEditable(false);
        // Pola samochodu
        if(modelTextField != null) modelTextField.setEditable(false);
        if(registrationTextField != null) registrationTextField.setEditable(false);
        if(weightTextField != null) weightTextField.setEditable(false);
        if(speedTextField != null) speedTextField.setEditable(false);

        // Pola silnika
        if(engineNameTextField != null) engineNameTextField.setEditable(false);
        if(rpmTextField != null) rpmTextField.setEditable(false);

        // Pola skrzyni biegów
        if(gearboxNameTextField != null) gearboxNameTextField.setEditable(false);
        if(gearTextField != null) gearTextField.setEditable(false);

        // Pola sprzęgła
        if(clutchNameTextField != null) clutchNameTextField.setEditable(false);
        if(clutchStateTextField != null) clutchStateTextField.setEditable(false);
    }
}