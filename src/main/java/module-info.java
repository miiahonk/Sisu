module fi.tuni.prog3.sisu {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.json;

    opens fi.tuni.prog3.sisu to javafx.fxml;
    exports fi.tuni.prog3.sisu;
}
