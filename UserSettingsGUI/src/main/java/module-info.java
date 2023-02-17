module com.example.presentationproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;


    opens usersettingsgui to javafx.fxml;
    exports usersettingsgui;
}