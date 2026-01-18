package org.przyklad.samochod;

// Interfejs Obserwatora.
// GUI (Controller) to implementuje, żeby wiedzieć, kiedy odświeżyć widok.
public interface Listener {
    void update();
}