module me.unipa.progettoingsoftware {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires com.zaxxer.hikari;
    requires lombok;
    requires java.sql;

    opens me.unipa.progettoingsoftware to javafx.fxml;
    exports me.unipa.progettoingsoftware;
    exports me.unipa.progettoingsoftware.autenticazione;
    opens me.unipa.progettoingsoftware.autenticazione to javafx.fxml;
    opens me.unipa.progettoingsoftware.gestioneareaaziendale to javafx.fxml;
    exports me.unipa.progettoingsoftware.gestioneareaaziendale;
}