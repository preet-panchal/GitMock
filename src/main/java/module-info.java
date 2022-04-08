module csci2020u.finalproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens csci2020u.finalproject to javafx.fxml;
    exports csci2020u.finalproject;
}