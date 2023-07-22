package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.DogsFoto;
import com.example.pet_shelter.service.DogsFotoService;
import com.example.pet_shelter.service.DogsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/dogs")
public class DogController {

    @Value("${size.FotoDogs}")
    int SIZE_FOTO_DOG; // Максимальный размер файла

    private final DogsService dogsService;
    private final DogsFotoService dogsFotoService;

    public DogController(DogsService dogsService, DogsFotoService dogsFotoService) {
        this.dogsService = dogsService;
        this.dogsFotoService = dogsFotoService;
    }

    @Operation(summary = "Внесение данных о новой питомце",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о питомце занесена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Dogs.class))
                    ))}, tags = "DOG")
    @PostMapping("/create")
    public Dogs createDog(@RequestBody Dogs dog) {
        return this.dogsService.createDogInDB(dog);
    }


    @Operation(summary = "Удаление информации о питомце",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о питомце удалена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))}, tags = "DOG")
    @DeleteMapping("/delete/{id}")
    public Dogs deleteDog(@Parameter(description = "Id питомца", example = "1") @PathVariable("id") Long id) {
        return this.dogsService.deleteDog(id);
    }

    @Operation(summary = "Изменение данных о питомце",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о питомце изменена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Dogs.class))
                    ))}, tags = "DOG")
    @PutMapping("/update/{id}")
    public Dogs updateDog(@Parameter(description = "Id питомца", example = "1") @PathVariable("id") Long id, @RequestBody Dogs dog) {
        return this.dogsService.updateDog(id, dog);
    }

    @Operation(summary = "Загрузка фотографии питомца",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Фотография загружена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))}, tags = "DOG")
    @PostMapping(value = "/{id}/load/fotoDog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Прикрепление фото собаки
    public ResponseEntity<String> uploadFotoDog(@Parameter(description = "Id питомца", example = "1") @PathVariable Long id, @Parameter(description = "Путь к фотографии") @RequestParam MultipartFile fotoDog) throws

            IOException {
        if (fotoDog.getSize() >= SIZE_FOTO_DOG) {         // Ограничение размера файла
            return ResponseEntity.badRequest().body("Файл большого размера");
        }
        dogsFotoService.uploadFotoDog(id, fotoDog);
        return ResponseEntity.ok().build();

    }

    @Operation(summary = "Просмотр фотографии питомца",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Фотография найдена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))}, tags = "DOG")
    @GetMapping(value = "/{id}/fotoDog")
    public void downloadFotoDog(@Parameter(description = "Id питомца", example = "1") @PathVariable Long id, HttpServletResponse response) throws IOException {
        DogsFoto dogsFoto = dogsFotoService.findFotoDog(id);
        Path path = Path.of(dogsFoto.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(dogsFoto.getMediaType());
            response.setContentLength((int) dogsFoto.getFileSize());
            is.transferTo(os);
        }
    }

}
