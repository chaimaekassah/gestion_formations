package org.example.monprojetjavafx.monapp.dao;

import org.example.monprojetjavafx.monapp.model.Formation;
import org.example.monprojetjavafx.monapp.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    // Récupérer toutes les formations
    public List<Formation> getAllFormations() throws SQLException {
        List<Formation> list = new ArrayList<>();
        String sql = "SELECT * FROM Formation";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Formation(rs.getString("id"), rs.getString("name")));
            }
        }
        return list;
    }

    // Récupérer les étudiants d'une formation
    public List<Student> getStudentsByFormation(String formationId) throws SQLException {
        List<Student> list = new ArrayList<>();
        // On récupère les étudiants
        String sql = "SELECT * FROM Student WHERE formation_id = ?";

        // Calculons d'abord la moyenne de la classe (requête séparée ou sous-requête)
        String sqlAvg = "SELECT AVG(moyenne) as moy_gen FROM Student WHERE formation_id = ?";
        float avgFormation = 0;

        try (Connection conn = DBConnection.getConnection()) {

            // 1. Calcul moyenne formation
            try (PreparedStatement pstAvg = conn.prepareStatement(sqlAvg)) {
                pstAvg.setString(1, formationId);
                ResultSet rsAvg = pstAvg.executeQuery();
                if(rsAvg.next()) avgFormation = rsAvg.getFloat("moy_gen");
            }

            // 2. Liste étudiants
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, formationId);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Student s = new Student(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getFloat("moyenne"),
                            rs.getString("formation_id")
                    );
                    s.setFormationMoyenne(avgFormation); // On injecte la moyenne de classe
                    list.add(s);
                }
            }
        }
        return list;
    }

    // Ajouter un étudiant
    public void addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO Student (id, name, moyenne, formation_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, s.getId());
            pst.setString(2, s.getName());
            pst.setFloat(3, s.getMoyenne());
            pst.setString(4, s.getFormationId());
            pst.executeUpdate();
        }
    }
}