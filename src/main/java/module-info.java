module me.unipa.progettoingsoftware {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires com.zaxxer.hikari;
    requires lombok;
    requires java.sql;
    requires org.slf4j;

    opens me.unipa.progettoingsoftware to javafx.fxml;
    exports me.unipa.progettoingsoftware;
    exports me.unipa.progettoingsoftware.autenticazione;
    opens me.unipa.progettoingsoftware.autenticazione to javafx.fxml;
    opens me.unipa.progettoingsoftware.gestioneareaaziendale to javafx.fxml;
    exports me.unipa.progettoingsoftware.gestioneareaaziendale;
    opens me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo to javafx.fxml;
    exports me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

    opens me.unipa.progettoingsoftware.gestioneareaaziendale.gestionemagazzinoaziendale to javafx.fxml;
    exports me.unipa.progettoingsoftware.gestioneareaaziendale.gestionemagazzinoaziendale;

    opens me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali to javafx.fxml;
    exports me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali;

    opens me.unipa.progettoingsoftware.utils to javafx.fxml;
    exports me.unipa.progettoingsoftware.utils;
    exports me.unipa.progettoingsoftware.utils.entity;
    opens me.unipa.progettoingsoftware.utils.entity to javafx.fxml;
}