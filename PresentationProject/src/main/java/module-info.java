module com.example.presentationproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;


    opens presentationproject to javafx.fxml;
    exports presentationproject;
}