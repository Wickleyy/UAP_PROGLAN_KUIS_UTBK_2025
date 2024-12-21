module org.example.UAP_PROGLAN {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;

    opens org.example.uap_proglan to javafx.fxml;
    exports org.example.uap_proglan;
}
