module org.example.uap_proglan {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.uap_proglan to javafx.fxml;
    exports org.example.uap_proglan;
}