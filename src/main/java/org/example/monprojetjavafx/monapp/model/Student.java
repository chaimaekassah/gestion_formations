package org.example.monprojetjavafx.monapp.model;

public class Student {
    private String id;
    private String name;
    private float moyenne;
    private String formationId;

    // Champ calculé pour l'affichage (Moyenne de la formation)
    private float formationMoyenne;

    public Student(String id, String name, float moyenne, String formationId) {
        this.id = id;
        this.name = name;
        setMoyenne(moyenne); // Utilise le setter pour valider
        this.formationId = formationId;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public float getMoyenne() { return moyenne; }
    public String getFormationId() { return formationId; }
    public float getFormationMoyenne() { return formationMoyenne; }

    public void setFormationMoyenne(float formationMoyenne) {
        this.formationMoyenne = formationMoyenne;
    }

    // Setter avec Validation
    public void setMoyenne(float moyenne) {
        if (moyenne < 0 || moyenne > 20) {
            throw new IllegalArgumentException("La moyenne doit être comprise entre 0 et 20.");
        }
        this.moyenne = moyenne;
    }
}