module com.example.presentationproject {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;


    opens usersettingsgui to javafx.fxml;
    exports usersettingsgui;
}