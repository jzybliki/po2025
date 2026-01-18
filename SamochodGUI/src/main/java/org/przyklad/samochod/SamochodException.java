package org.przyklad.samochod;

// klasa błędu - żeby rzucać konkretny wyjątek, jak np. zmieniam bieg bez sprzęgła.
public class SamochodException extends Exception {
    public SamochodException(String message) {
        super(message); // Przekazujemy komunikat błędu wyżej
    }
}