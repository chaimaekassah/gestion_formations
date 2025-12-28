package org.example.monprojetjavafx.monapp.controller;
import org.example.monprojetjavafx.monapp.dao.DataManager;
import org.example.monprojetjavafx.monapp.model.Formation;
import org.example.monprojetjavafx.monapp.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainController {

    @FXML
    private ComboBox<Formation> cbFormations;
    @FXML
    private TableView<Student> tableStudents;
    @FXML
    private TableColumn<Student, String> colId;
    @FXML
    private TableColumn<Student, String> colName;
    @FXML
    private TableColumn<Student, Float> colMoyenne;
    @FXML
    private TableColumn<Student, Float> colMoyenneFormation;

    private DataManager dataManager = new DataManager();

    @FXML
    public void initialize() {
        // 1. Configurer les colonnes de la table
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMoyenne.setCellValueFactory(new PropertyValueFactory<>("moyenne"));
        colMoyenneFormation.setCellValueFactory(new PropertyValueFactory<>("formationMoyenne"));

        // 2. Charger les formations dans la ComboBox
        loadFormations();

        // 3. Ajouter un écouteur sur la sélection de la ComboBox
        cbFormations.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadStudents(newVal.getId());
            }
        });
    }

    private void loadFormations() {
        System.out.println("--- DÉBUT CHARGEMENT FORMATIONS ---");
        try {
            // 1. Appel au DAO
            List<Formation> formations = dataManager.getAllFormations();

            // 2. Vérification brute
            if (formations.isEmpty()) {
                System.err.println("ALERTE : La liste renvoyée par la BDD est VIDE !");
                System.err.println("Vérifiez que vous avez fait le COMMIT dans pgAdmin.");
            } else {
                System.out.println("SUCCÈS : " + formations.size() + " formations trouvées.");
                for (Formation f : formations) {
                    System.out.println(" -> Trouvé : " + f.getName());
                }
            }

            // 3. Mise à jour Interface
            ObservableList<Formation> list = FXCollections.observableArrayList(formations);
            cbFormations.setItems(list);

        } catch (SQLException e) {
            // Affiche l'erreur complète en rouge
            System.err.println("ERREUR SQL CRITIQUE :");
            e.printStackTrace();
            showAlert("Erreur BDD", "Problème SQL : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERREUR INCONNUE :");
            e.printStackTrace();
        }
    }

    public void loadStudents(String formationId) {
        try {
            ObservableList<Student> list = FXCollections.observableArrayList(dataManager.getStudentsByFormation(formationId));
            tableStudents.setItems(list);
        } catch (SQLException e) {
            showAlert("Erreur BDD", "Impossible de charger les étudiants : " + e.getMessage());
        }
    }

    @FXML
    void handleOpenAddStudent(ActionEvent event) {
        Formation selectedFormation = cbFormations.getSelectionModel().getSelectedItem();
        if (selectedFormation == null) {
            showAlert("Attention", "Veuillez d'abord sélectionner une formation.");
            return;
        }

        try {
            // --- CORRECTION DU CHEMIN ICI ---
            // On utilise le chemin complet basé sur tes dossiers
            // Vérifie bien si ton dossier s'appelle "View" ou "view" dans resources !
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/monprojetjavafx/monapp/View/AddStudent.fxml"));
            Parent root = loader.load();

            // Passer le contrôleur principal et la formation sélectionnée au contrôleur d'ajout
            AddStudentController addCtrl = loader.getController();
            addCtrl.initData(selectedFormation, this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajout Étudiant");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout : " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}