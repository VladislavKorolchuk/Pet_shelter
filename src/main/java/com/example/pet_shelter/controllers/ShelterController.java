package com.example.pet_shelter.controllers;


import com.example.pet_shelter.model.Shelters;
import com.example.pet_shelter.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/shelter")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @Operation(summary = "Внесение данных о новом приюте\"",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о приюте занесена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Shelters.class))
                    ))}, tags = "SHELTER")
    // Внесение данных о новом приюте
    @PostMapping("/create")   // Создание новой записи о приюте

    public Shelters createShelter(@RequestBody Shelters shelter) {
        return this.shelterService.createShelter(shelter);
    }


    @Operation(summary = "Удаление информации о приюте",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о приюте удалена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))}, tags = "SHELTER")
    // Удаление информации о приюте
    @DeleteMapping("/delete/{id}")
    public Shelters deleteShelter(@Parameter(description = "Id приютa", example = "1") @PathVariable("id") Long id) {
        return this.shelterService.deleteShelter(id);
    }

    @Operation(summary = "Изменение данных о приюте",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о приюте изменена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Shelters.class))
                    ))}, tags = "SHELTER")
    // Изменение информации о приюте
    @PutMapping("/update/{id}")
    public Shelters updateShelter(@Parameter(description = "Id приютa", example = "1") @PathVariable("id") Long id,
                          @RequestBody Shelters shelter) {
        return this.shelterService.updateShelter(id, shelter);
    }


    @Operation(
            summary = "Получение всех приютов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список приютов",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Shelters.class))
                            )
                    )
            },tags = "SHELTER"
    )
    @GetMapping("/getAll")
    public Collection<Shelters> getAllShelters() {
        return this.shelterService.getAllShelters();
    }


    @Operation(summary = "Информации о приюте",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о приюте",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))}, tags = "SHELTER")

    @GetMapping("/get/{id}")
    public Shelters getShelter(@Parameter(description = "Id приютa", example = "1") @PathVariable("id") Long id) {
        return this.shelterService.getShelter(id);
    }

}
