package com.example.pet_shelter.model;

import javax.persistence.*;

@Entity
@Table(name = "dogsfoto")
public class DogsFoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;               // Идентификатор

    @Column(name = "filepath")
    private String filePath;       // Название файла

    @Column(name = "filesize")
    private long fileSize;         // Размер файла

    @Column(name = "mediatype")
    private String MediaType;      // Тип файла

    @Lob
    private byte[] fotoDog;       // Массив для хранения фотографии

    @OneToOne                     // Связь один к одному
    private Dogs dog;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return MediaType;
    }

    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }

    public byte[] getFotoDog() {
        return fotoDog;
    }

    public void setFotoDog(byte[] fotoDog) {
        this.fotoDog = fotoDog;
    }

    public Dogs getDog() {
        return dog;
    }

    public void setDog(Dogs dog) {
        this.dog = dog;
    }

}
