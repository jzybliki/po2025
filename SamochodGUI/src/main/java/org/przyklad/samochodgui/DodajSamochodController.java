package org.przyklad.samochodgui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.przyklad.samochod.*;

public class DodajSamochodController {

    @FXML private TextField modelTextField;
    @FXML private TextField registrationTextField;
    @FXML private TextField weightTextField;
    @FXML private TextField speedTextField;

    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

    private HelloController mainController;

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void onConfirmButton() {
        String model = modelTextField.getText();
        String registration = registrationTextField.getText();
        double weight;
        int speed;

        try {
            weight = Double.parseDouble(weightTextField.getText());
            speed = Integer.parseInt(speedTextField.getText());

            if(model.isEmpty() || registration.isEmpty()) throw new Exception("Puste pola");

        } catch (Exception e) {
            // W razie błędu, używamy metody z głównego kontrolera (jeśli dostępna)
            if (mainController != null) {
                mainController.pokazBlad("Niepoprawne dane! Sprawdź liczby i puste pola.");
            } else {
                System.out.println("Błąd danych.");
            }
            return;
        }

        // Tworzymy NOWY obiekt samochodu (wątek uruchomi się w konstruktorze)
        Samochod noweAuto = new Samochod(
                registration, model, speed,
                new Silnik("Generic", "Standard", 6000),
                new SkrzyniaBiegow("Generic", "5-Speed", 5),
                new Sprzeglo("Generic", "Std"),
                new Pozycja(20, 50) // Pozycja startowa
        );

        // Dodajemy do głównego kontrolera
        if (mainController != null) {
            mainController.dodajSamochod(noweAuto);
        }

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}