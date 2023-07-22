package com.example.pet_shelter.service;

import com.example.pet_shelter.listener.TelegramBotUpdatesListener;
import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.DogsFoto;
import com.example.pet_shelter.repository.DogsFotoRepository;
import com.example.pet_shelter.repository.DogsRepository;
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
public class DogsFotoService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final DogsRepository dogsRepository;
    private final DogsFotoRepository dogsFotoRepository;

    public DogsFotoService(DogsRepository dogsRepository, DogsFotoRepository dogsFotoRepository) {
        this.dogsRepository = dogsRepository;
        this.dogsFotoRepository = dogsFotoRepository;
    }

    @Value("${dir.path.FotoDogs}")
    private String FotoDogsDir;      // Директория фото собак

    /**
     * <b>Загрузка фото питомца в базу данных и на диск</b>
     * <br> Используется метод репозитория {@link JpaRepository#findById(Object)}
     * <br> Используется метод репозитория {@link JpaRepository#save(Object)}
     * <br> Используется метод репозитория {@link DogsFotoService#getExtensions(String)}
     *
     * @param id   идентификатор питомца
     * @param file считываемый файл
     * @throws IOException - может возникнуть исключение ввода/вывода
     */
    public DogsFoto uploadFotoDog(Long id, MultipartFile file) throws IOException {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        Dogs dog = dogsRepository.findById(id).orElseThrow();
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
        DogsFoto dogsFoto = dogsFotoRepository.findById(id).orElseGet(DogsFoto::new);
        dogsFoto.setDog(dog);
        dogsFoto.setFilePath(filePath.toString());
        dogsFoto.setFileSize(file.getSize());
        dogsFoto.setMediaType(file.getContentType());
        dogsFoto.setFotoDog(file.getBytes());
        return dogsFotoRepository.save(dogsFoto);
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
     * @param dogId идентификатор питомца
     * @return Возвращает найденного питомца
     */
    public Dogs findDog(Long dogId) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        return dogsRepository.findById(dogId).orElseThrow();
    }

    /**
     * <b>Поиск фото собаки по id идентификатору питомца</b>
     * <br> Используется метод репозитория {@link JpaRepository#findById(Object)}
     *
     * @param dogId идентификатор питомца
     * @return Возвращает объект фото
     * @throws IOException - может возникнуть исключение ввода/вывода
     */
    // Поиск фото собаки по id
    public DogsFoto findFotoDog(Long dogId) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        return dogsFotoRepository.findById(dogId).orElseThrow();
    }

}
