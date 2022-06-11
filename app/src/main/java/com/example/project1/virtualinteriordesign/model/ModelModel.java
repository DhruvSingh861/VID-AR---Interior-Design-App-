package com.example.project1.virtualinteriordesign.model;

public class ModelModel {
    String Name;
    String link;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ModelModel(String name, String link) {
        Name = name;
        this.link = link;
    }
}
