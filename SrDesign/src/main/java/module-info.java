module srdesign.team11 {
    requires javafx.controls;
    requires javafx.fxml;


    opens srdesign.team11 to javafx.fxml;
    exports srdesign.team11;
}