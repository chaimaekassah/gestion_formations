package org.example.monprojetjavafx.monapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    // --- CHANGEMENTS IMPORTANTS ICI ---
    // 1. Le protocole est 'postgresql'
    // 2. Le port est 5432 (par défaut sur Postgres)
    // 3. Vérifie que la base 'db_ecole' existe bien dans ton pgAdmin ou console
    private static final String URL = "jdbc:postgresql://localhost:5432/db_ecole";

    // 4. L'utilisateur par défaut est souvent 'postgres' (vérifie ta config)
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Charge le driver PostgreSQL
                Class.forName("org.postgresql.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion à PostgreSQL réussie !");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException("Driver PostgreSQL introuvable.");
            }
        }
        return connection;
    }
}