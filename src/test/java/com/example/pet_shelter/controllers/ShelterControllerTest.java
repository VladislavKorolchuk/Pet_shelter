package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Shelters;
import com.example.pet_shelter.repository.*;
import com.example.pet_shelter.service.*;
import com.pengrad.telegrambot.TelegramBot;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
public class ShelterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShelterRepository repository;
    @MockBean
    private CatsRepository repositoryCats;
    @MockBean
    private UsersRepository repositoryUsers;
    @MockBean
    private DogsRepository repositoryDogs;
    @MockBean
    private CatsFotoRepository repositoryFoto;
    @MockBean
    private DogsFotoRepository repositoryFotoDogs;
    @MockBean
    private ReportUsersRepository reportUsersRepository;
    @MockBean
    private TelegramBot bot;
    @MockBean
    private BinaryContentFileRepository binaryContentFileRepository;
    @MockBean
    private ShelterService service;
    @SpyBean
    private CatsService serviceCats;
    @SpyBean
    private UsersService serviceUsers;
    @SpyBean
    private DogsService serviceDogs;
    @SpyBean
    private CatsFotoService fotoServices;
    @SpyBean
    private DogsFotoService fotoServicesDogs;
    @SpyBean
    private ReportUsersService reportUsersService;

    @InjectMocks
    private ShelterController controller;

    @Test
    public void testGetAllShelters() throws Exception {
        when(service.getAllShelters()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testGetAllShelters2() throws Exception {
        when(service.getAllShelters()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/getAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("Encoding"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testGetSheltersById() throws Exception {
        Long id = 11L;
        String nameShelter = "Shelter \"Cool dogs\"";
        String descriptionShelter = "Take the dogs away!";

        JSONObject sheltersObject = new JSONObject();
        sheltersObject.put("id", id);
        sheltersObject.put("nameShelter", nameShelter);
        sheltersObject.put("descriptionShelter", descriptionShelter);

        Shelters shelters = new Shelters();
        shelters.setId(id);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);

        when(service.getShelter(any(Long.class))).thenReturn(shelters);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/get/" + id)
                        .content(sheltersObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nameShelter").value(nameShelter))
                .andExpect(jsonPath("$.descriptionShelter").value(descriptionShelter));
    }

    @Test
    public void testCreateShelters() throws Exception {
        Long id = 11L;
        String nameShelter = "";
        String descriptionShelter = "Take the dogs away!";

        JSONObject sheltersObject = new JSONObject();
        sheltersObject.put("id", id);
        sheltersObject.put("nameShelter", nameShelter);
        sheltersObject.put("descriptionShelter", descriptionShelter);

        Shelters shelters = new Shelters();
        shelters.setId(11L);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);

        when(service.createShelter(any(Shelters.class))).thenReturn(shelters);
        when(repository.save(any(Shelters.class))).thenReturn(shelters);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(shelters));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelter/create")
                        .content(sheltersObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nameShelter").value(nameShelter))
                .andExpect(jsonPath("$.descriptionShelter").value(descriptionShelter));

    }

    @Test
    public void testCreateShelters2() throws Exception {
        Long id = 11L;
        String nameShelter = "Shelter \"Cool dogs\"";
        String descriptionShelter = "Take the dogs away!";

        JSONObject sheltersObject = new JSONObject();
        sheltersObject.put("id", id);
        sheltersObject.put("nameShelter", nameShelter);
        sheltersObject.put("descriptionShelter", descriptionShelter);

        Shelters shelters = new Shelters();
        shelters.setId(11L);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);

        when(service.createShelter(any(Shelters.class))).thenReturn(shelters);
        when(repository.save(any(Shelters.class))).thenReturn(shelters);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(shelters));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelter/create")
                        .content(sheltersObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nameShelter").value(nameShelter))
                .andExpect(jsonPath("$.descriptionShelter").value(descriptionShelter));
    }

    @Test
    public void testDeleteShelters() throws Exception {
        Long id = 11L;
        String nameShelter = "Shelter \"Cool dogs\"";
        String descriptionShelter = "Take the dogs away!";

        JSONObject sheltersObject = new JSONObject();
        sheltersObject.put("id", id);
        sheltersObject.put("nameShelter", nameShelter);
        sheltersObject.put("descriptionShelter", descriptionShelter);

        Shelters shelters = new Shelters();
        shelters.setId(11L);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);

        when(service.deleteShelter(any(Long.class))).thenReturn(shelters);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(shelters));
        doNothing().when(repository).deleteById(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/shelter/delete/" + id)
                        .content(sheltersObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nameShelter").value(nameShelter))
                .andExpect(jsonPath("$.descriptionShelter").value(descriptionShelter));
    }


    @Test
    public void testUpdateShelters() throws Exception {
        Long id = 11L;
        String nameShelter = "Shelter Cool dogs";
        String descriptionShelter = "Take the dogs away!";

        JSONObject sheltersObject = new JSONObject();
        sheltersObject.put("id", id);
        sheltersObject.put("nameShelter", nameShelter);
        sheltersObject.put("descriptionShelter", descriptionShelter);

        Shelters shelters = new Shelters();
        shelters.setId(11L);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);

        when(service.updateShelter(any(Long.class), any(Shelters.class))).thenReturn(shelters);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(shelters));

        Shelters shelters2 = new Shelters();
        shelters2.setId(11L);
        shelters2.setNameShelter(nameShelter);
        shelters2.setDescriptionShelter(descriptionShelter);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/shelter/update/" + id, shelters2)
                        .content(sheltersObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "{\"id\":11,\"nameShelter\":\"Shelter Cool dogs\",\"descriptionShelter\":\"Take the dogs away!\"}"));
    }
}
