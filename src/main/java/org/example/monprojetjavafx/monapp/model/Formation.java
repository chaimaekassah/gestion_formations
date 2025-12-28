package org.example.monprojetjavafx.monapp.model;


public class Formation {
    private String id;
    private String name;

    public Formation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters et Setters
    public String getId() { return id; }
    public String getName() { return name; }

    // Important pour l'affichage dans la ComboBox
    @Override
    public String toString() {
        return this.name;
    }
}