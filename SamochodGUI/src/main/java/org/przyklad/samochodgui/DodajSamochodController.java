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

    // Muszę mieć dostęp do głównego kontrolera, żeby wrzucić tam nowe auto
    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void onConfirmButton() {
        // Pobranie tekstów z pól formularza
        String model = modelTextField.getText();
        String registration = registrationTextField.getText();
        double weight;
        int speed;

        try {
            // Parsowanie wartości liczbowych z obsługą błędów
            weight = Double.parseDouble(weightTextField.getText());
            speed = Integer.parseInt(speedTextField.getText());

            // Walidacja czy pola tekstowe nie są puste
            if(model.isEmpty() || registration.isEmpty()) throw new Exception("Puste pola");

        } catch (Exception e) {
            // Wyświetlenie błędu w GUI, jeśli dane są niepoprawne
            if (mainController != null) {
                mainController.pokazBlad("Niepoprawne dane! Sprawdź liczby i puste pola.");
            } else {
                System.out.println("Błąd danych.");
            }
            return; // Przerywamy działanie metody, nie zamykamy okna
        }

        // Tworzę standardowe podzespoły (generic), bo formularz jest uproszczony
        Samochod noweAuto = new Samochod(
                registration, model, speed, weight,
                new Silnik("Generic", "Standard", 6000),
                new SkrzyniaBiegow("Generic", "5-Speed", 5),
                new Sprzeglo("Generic", "Std"),
                new Pozycja(20, 50) // Pozycja startowa
        );

        // Dodajemy do głównego kontrolera
        if (mainController != null) {
            mainController.dodajSamochod(noweAuto);
        }
        // Zamknięcie okna dodawania po pomyślnym zatwierdzeniu
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}