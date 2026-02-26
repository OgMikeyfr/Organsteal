package com.organstealer.models;

public class PlayerOrgans {
    private String organName;
    private int organValue;

    public PlayerOrgans(String organName, int organValue) {
        this.organName = organName;
        this.organValue = organValue;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public int getOrganValue() {
        return organValue;
    }

    public void setOrganValue(int organValue) {
        this.organValue = organValue;
    }
}