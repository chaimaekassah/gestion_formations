module org.example.monprojetjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // --- CHANGEMENT ICI ---
    requires org.postgresql.jdbc;

    exports org.example.monprojetjavafx.monapp;
    opens org.example.monprojetjavafx.monapp.controller to javafx.fxml;
    opens org.example.monprojetjavafx.monapp.model to javafx.base;
}