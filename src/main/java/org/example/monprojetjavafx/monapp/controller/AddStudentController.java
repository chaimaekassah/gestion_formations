package org.example.monprojetjavafx.monapp.controller;


import org.example.monprojetjavafx.monapp.dao.DataManager;
import org.example.monprojetjavafx.monapp.model.Formation;
import org.example.monprojetjavafx.monapp.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddStudentController {

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtMoyenne;

    private Formation currentFormation;
    private MainController mainController;
    private DataManager dataManager = new DataManager();

    // Méthode pour recevoir les données du MainController
    public void initData(Formation formation, MainController mainCtrl) {
        this.currentFormation = formation;
        this.mainController = mainCtrl;
    }

    @FXML
    void handleSave() {
        try {
            String id = txtId.getText();
            String name = txtName.getText();
            float moyenne = Float.parseFloat(txtMoyenne.getText());

            // 1. Création de l'objet Student (La validation 0-20 se fait ici via le Setter du modèle)
            Student s = new Student(id, name, moyenne, currentFormation.getId());

            // 2. Envoi en Base de données
            dataManager.addStudent(s);

            // 3. Mise à jour de la table principale
            mainController.loadStudents(currentFormation.getId());

            // 4. Fermer la fenêtre
            ((Stage) txtId.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "La moyenne doit être un nombre valide.");
        } catch (IllegalArgumentException e) {
            // C'est ici qu'on attrape l'exception levée par le modèle Student (0-20)
            showAlert("Validation échouée", e.getMessage());
        } catch (SQLException e) {
            if(e.getMessage().contains("chk_moyenne")) {
                showAlert("Erreur SQL", "La base de données a rejeté la moyenne (Hors limites).");
            } else {
                showAlert("Erreur SQL", "Problème lors de l'insertion : " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
