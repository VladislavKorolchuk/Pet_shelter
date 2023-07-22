package com.example.pet_shelter.controllers;


import com.example.pet_shelter.exceptions.CatNullParameterValueException;
import com.example.pet_shelter.model.Cats;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class CatControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatsRepository repository;

    @MockBean
    private ShelterRepository shelterRepository;

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

    @SpyBean
    private ShelterService shelterService;
    @SpyBean
    private CatsService service;

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
    private CatController controller;

    @Test
    public void testCreateCat() throws Exception {
        String nickname = "Murzik";
        Long id = 11L;
        int age = 1;
        String info = "Info Cat";

        JSONObject catObject = new JSONObject();
        catObject.put("id", id);
        catObject.put("nickname", nickname);
        catObject.put("age", age);
        catObject.put("infoCat", info);

        Cats cat = new Cats();
        cat.setId(id);
        cat.setNickname(nickname);
        cat.setAge(age);
        cat.setInfoCat(info);


        when(repository.save(any(Cats.class))).thenReturn(cat);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(cat));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cats/create")
                        .content(catObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.infoCat").value(info));

    }

    @Test
    public void testCreateCat2() {
        String nickname = "";
        Long id = 11L;
        int age = 1;
        String info = "Info Cat";

        Cats cat = new Cats();
        cat.setId(id);
        cat.setNickname(nickname);
        cat.setAge(age);
        cat.setInfoCat(info);

        when(repository.save(any(Cats.class))).thenReturn(cat);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(cat));

        assertThrows(CatNullParameterValueException.class, () -> service.createCatInDB(cat));
    }


    @Test
    public void testDeletCat() throws Exception {
        String nickname = "Murzik";
        Long id = 11L;
        int age = 1;
        String info = "Info Cat";

        JSONObject catObject = new JSONObject();
        catObject.put("id", id);
        catObject.put("nickname", nickname);
        catObject.put("age", age);
        catObject.put("infoCat", info);

        Cats cat = new Cats();
        cat.setId(id);
        cat.setNickname(nickname);
        cat.setAge(age);
        cat.setInfoCat(info);


        when(repository.save(any(Cats.class))).thenReturn(cat);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(cat));
        doNothing().when(repository).deleteById(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/cats/delete/" + 5L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(
                        "{\"id\":11,\"nickname\":\"Murzik\",\"age\":1,\"infoCat\":\"Info Cat\"}"));
    }


    @Test
    public void testUpdateCat() throws Exception {
        String nickname = "Murzik";
        Long id = 11L;
        int age = 1;
        String info = "Info Cat";

        JSONObject catObject = new JSONObject();
        catObject.put("id", id);
        catObject.put("nickname", nickname);
        catObject.put("age", age);
        catObject.put("infoCat", info);

        Cats cat = new Cats();
        cat.setId(id);
        cat.setNickname(nickname);
        cat.setAge(age);
        cat.setInfoCat(info);

        when(repository.save(any(Cats.class))).thenReturn(cat);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(cat));

        Cats cat2 = new Cats();
        cat2.setId(id);
        cat2.setNickname(nickname);
        cat2.setAge(age);
        cat2.setInfoCat(info);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/cats/update/" + id, cat)
                        .content(catObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string
                        ("{\"id\":11,\"nickname\":\"Murzik\",\"age\":1,\"infoCat\":\"Info Cat\"}"));
    }

    @Test
    public void testUploadFotoCat() throws Exception {
        ResponseEntity<String> actualUploadFotoCatResult = controller.uploadFotoCat(11L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AAAAAAAA".getBytes("UTF-8"))));

        assertEquals("Файл большого размера", actualUploadFotoCatResult.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, actualUploadFotoCatResult.getStatusCode());
        assertTrue(actualUploadFotoCatResult.getHeaders().isEmpty());
    }
}