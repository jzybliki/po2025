module org.przyklad.samochodgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.przyklad.samochodgui to javafx.fxml;
    exports org.przyklad.samochodgui;
}