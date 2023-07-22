package com.example.pet_shelter.model;

import javax.persistence.*;

@Entity
public class Shelters {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String nameShelter;          // Название приюта
    private String descriptionShelter;   // Описание приюта

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameShelter() {
        return nameShelter;
    }

    public void setNameShelter(String nameShelter) {
        this.nameShelter = nameShelter;
    }

    public String getDescriptionShelter() {
        return descriptionShelter;
    }

    public void setDescriptionShelter(String descriptionShelter) {
        this.descriptionShelter = descriptionShelter;
    }

}
