package com.example.pet_shelter.service;

import com.example.pet_shelter.listener.TelegramBotUpdatesListener;
import com.example.pet_shelter.model.Cats;
import com.example.pet_shelter.model.CatsFoto;
import com.example.pet_shelter.repository.CatsFotoRepository;
import com.example.pet_shelter.repository.CatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class CatsFotoService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final CatsRepository catsRepository;
    private final CatsFotoRepository catsFotoRepository;

    public CatsFotoService(CatsRepository catsRepository, CatsFotoRepository catsFotoRepository) {
        this.catsRepository = catsRepository;
        this.catsFotoRepository = catsFotoRepository;
    }

    @Value("${dir.path.FotoDogs}")
    private String FotoDogsDir;      // Директория фото собак

    /**
     * <b>Загрузка фото питомца в базу данных и на диск</b>
     * <br> Используется метод репозитория {@link JpaRepository#findById(Object)}
     * <br> Используется метод репозитория {@link JpaRepository#save(Object)}
     * <br> Используется метод репозитория {@link CatsFotoService#getExtensions(String)}
     *
     * @param id   идентификатор питомца
     * @param file считываемый файл
     * @throws IOException - может возникнуть исключение ввода/вывода
     */
    public CatsFoto uploadFotoCat(Long id, MultipartFile file) throws IOException {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        Cats cat = catsRepository.findById(id).orElseThrow();
        Path filePath = Path.of(FotoDogsDir, id + "." + getExtensions(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent()); // Создание директории если ее нет
        Files.deleteIfExists(filePath);  // Если такой файл существует, то мы его удаляем
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 2048);
                BufferedOutputStream bos = new BufferedOutputStream(os, 2048);
        ) {
            bis.transferTo(bos);
        }
        CatsFoto catsFoto = catsFotoRepository.findById(id).orElseGet(CatsFoto::new);
        catsFoto.setCat(cat);
        catsFoto.setFilePath(filePath.toString());
        catsFoto.setFileSize(file.getSize());
        catsFoto.setMediaType(file.getContentType());
        catsFoto.setFotoCat(file.getBytes());
        return catsFotoRepository.save(catsFoto);
    }

    /**
     * <b> Формирует имя файла </b>
     *
     * @return Возвращает имя файла
     */
    public String getExtensions(String fileName) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * <b>Поиск питомца по его id идентификатору</b>
     * <br> Используется метод репозитория {@link JpaRepository#findById(Object)}
     *
     * @param catId идентификатор питомца
     * @return Возвращает найденного питомца
     */
    public Cats findCat(Long catId) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        return catsRepository.findById(catId).orElseThrow();
    }

    /**
     * <b>Поиск фото питомца по id идентификатору питомца</b>
     * <br> Используется метод репозитория {@link JpaRepository#findById(Object)}
     *
     * @param catId идентификатор питомца
     * @return Возвращает объект фото
     * @throws IOException - может возникнуть исключение ввода/вывода
     */
    // Поиск фото питомца по id
    public CatsFoto findFotoCat(Long catId) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        return catsFotoRepository.findById(catId).orElseThrow();
    }
}
