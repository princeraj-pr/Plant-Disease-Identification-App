package com.sih23.plantdiseaseidentifiers;

public class FruitEntry {

    private int id;
    private int iconId;

    public FruitEntry(int id, int iconId) {
        this.id = id;
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
