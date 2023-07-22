package com.example.pet_shelter.model;

import javax.persistence.*;

@Entity
public class BinaryContentFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;               // Идентификатор

    @Lob
    private byte[] fileBytes;      // Массив байтов файла

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
}
