package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Cats;
import com.example.pet_shelter.model.CatsFoto;
import com.example.pet_shelter.service.CatsFotoService;
import com.example.pet_shelter.service.CatsService;
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
@RequestMapping("/cats")
public class CatController {

    @Value("${size.FotoDogs}")
    int SIZE_FOTO; // Максимальный размер файла

    private final CatsService catsService;
    private final CatsFotoService catsFotoService;

    public CatController(CatsService catsService, CatsFotoService catsFotoService) {
        this.catsService = catsService;
        this.catsFotoService = catsFotoService;
    }

    @Operation(summary = "Внесение данных о новой питомце\"",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о питомце занесена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Cats.class))
                    ))}, tags = "CAT")
    @PostMapping("/create")   // Создание новой записи о питомце
    public Cats createCat(@RequestBody Cats cat) {
        return this.catsService.createCatInDB(cat);
    }


    @Operation(summary = "Удаление информации о питомце",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о питомце удалена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))}, tags = "CAT")
    @DeleteMapping("/delete/{id}")
    public Cats deleteCat(@Parameter(description = "Id питомца", example = "1") @PathVariable("id") Long id) {
        return this.catsService.deleteCat(id);
    }

    @Operation(summary = "Изменение данных о питомце",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о питомце изменена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Cats.class))
                    ))}, tags = "CAT")
    // Изменение информации о питомце
    @PutMapping("/update/{id}")
    public Cats updateCat(@Parameter(description = "Id питомца", example = "1") @PathVariable("id") Long id,
                          @RequestBody Cats cat) {
        return this.catsService.updateCat(id, cat);
    }

    @Operation(summary = "Загрузка фотографии питомца",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Фотография загружена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))}, tags = "CAT")

    // Загрузка фотографии питомца
    @PostMapping(value = "/{id}/load/fotoCat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Прикрепление фото собаки
    public ResponseEntity<String> uploadFotoCat(@Parameter(description = "Id питомца", example = "1")
                                                @PathVariable Long id, @Parameter(description = "Путь к фотографии")
                                                @RequestParam MultipartFile fotoCat) throws

            IOException {
        if (fotoCat.getSize() >= SIZE_FOTO) {         // Ограничение размера файла
            return ResponseEntity.badRequest().body("Файл большого размера");
        }
        catsFotoService.uploadFotoCat(id, fotoCat);
        return ResponseEntity.ok().build();

    }

    @Operation(summary = "Просмотр фотографии питомца",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Фотография найдена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))}, tags = "CAT")
    @GetMapping(value = "/{id}/fotoCat")
    public void downloadFotoCat(@Parameter(description = "Id питомца", example = "1") @PathVariable Long id,
                                HttpServletResponse response) throws IOException {
        CatsFoto catsFoto = catsFotoService.findFotoCat(id);
        Path path = Path.of(catsFoto.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(catsFoto.getMediaType());
            response.setContentLength((int) catsFoto.getFileSize());
            is.transferTo(os);
        }
    }

}
