module org.example.countdownlatchtest {
    requires javafx.controls;
    requires javafx.fxml;


    opens toyLanguage to javafx.fxml;
    exports toyLanguage;
}