package com.example.pet_shelter.model;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "Report")
public class ReportUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;                    // Идентификатор


    private Long chatId;                // Идентификатор чата
    private String telegramFileId;
    private String filePath;
    @OneToOne
    private BinaryContentFile binaryContentFile;
    private long fileSize;            // Размер файла
    private LocalDate time;           // Дата сдачи отчета
    private String commentsUser;      // Комментарии к отчету

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getTelegramFileId() {
        return telegramFileId;
    }

    public void setTelegramFileId(String telegramFileId) {
        this.telegramFileId = telegramFileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public BinaryContentFile getBinaryContentFile() {
        return binaryContentFile;
    }

    public void setBinaryContentFile(BinaryContentFile binaryContentFile) {
        this.binaryContentFile = binaryContentFile;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getCommentsUser() {
        return commentsUser;
    }

    public void setCommentsUser(String commentsUser) {
        this.commentsUser = commentsUser;
    }
}
