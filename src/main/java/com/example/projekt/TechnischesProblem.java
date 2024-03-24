package com.example.projekt;

public class TechnischesProblem {
    private String problemID;
    private String beschreibung;
    private boolean gelöst;

    public TechnischesProblem(String problemID, String beschreibung) {
        this.problemID = problemID;
        this.beschreibung = beschreibung;
        this.gelöst = false;
    }

    // Getter-Methoden
    public String getProblemID() {
        return this.problemID;
    }

    public String getBeschreibung() {
        return this.beschreibung;
    }

    public boolean isGelöst() {
        return this.gelöst;
    }

    // Setter-Methoden
    public void setProblemID(String problemID) {
        this.problemID = problemID;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setGelöst(boolean gelöst) {
        this.gelöst = gelöst;
    }
}


