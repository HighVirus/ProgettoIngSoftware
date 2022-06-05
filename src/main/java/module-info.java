module me.unipa.progettoingsoftware {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires com.zaxxer.hikari;
    requires lombok;
    requires java.sql;

    opens me.unipa.progettoingsoftware to javafx.fxml;
    exports me.unipa.progettoingsoftware;
}