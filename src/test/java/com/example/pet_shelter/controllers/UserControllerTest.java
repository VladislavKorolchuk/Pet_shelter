package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.Users;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatsRepository repositoryCats;

    @MockBean
    private ShelterRepository shelterRepository;

    @MockBean
    private UsersRepository repository;

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
    private CatsService serviceCats;

    @SpyBean
    private UsersService service;

    @SpyBean
    private DogsService serviceDogs;

    @SpyBean
    private CatsFotoService fotoServices;

    @SpyBean
    private DogsFotoService fotoServicesDogs;

    @SpyBean
    private ReportUsersService reportUsersService;

    @InjectMocks
    private UserController controller;

    @Test
    public void testGetAllUsers() throws Exception {
        when(service.getAllUsers()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testGetAllUsers2() throws Exception {
        when(service.getAllUsers()).thenReturn(new ArrayList<>());


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/getAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("Encoding"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testCreateUser() throws Exception {
        String name = "Jane";
        Long id = 11L;
        String lastName = "Doe";
        String email = "jane.doe@example.org";
        String phoneNumber = "4105551212";

        JSONObject userObject = new JSONObject();
        userObject.put("id", id);
        userObject.put("name", name);
        userObject.put("lastName", lastName);
        userObject.put("email", email);
        userObject.put("phoneNumber", phoneNumber);

        Users users = new Users();
        users.setFirstName("Jane");
        users.setId(11L);
        users.setLastName("Doe");
        users.setUserEmail("jane.doe@example.org");
        users.setUserPhoneNumber("4105551212");

        when(service.createUserInDb(any(Users.class))).thenReturn(users);
        when(repository.save(any(Users.class))).thenReturn(users);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(users));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/create")
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(name))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.userPhoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.userEmail").value(email));


    }

    @Test
    public void testCreateUser2() throws Exception {
        String name = "";
        Long id = 11L;
        String lastName = "Doe";
        String email = "jane.doe@example.org";
        String phoneNumber = "4105551212";

        JSONObject userObject = new JSONObject();
        userObject.put("id", id);
        userObject.put("name", name);
        userObject.put("lastName", lastName);
        userObject.put("email", email);
        userObject.put("phoneNumber", phoneNumber);

        Users users = new Users();
        users.setFirstName(name);
        users.setId(id);
        users.setLastName(lastName);
        users.setUserEmail(email);
        users.setUserPhoneNumber(phoneNumber);

        when(service.createUserInDb(any(Users.class))).thenReturn(users);
        when(repository.save(any(Users.class))).thenReturn(users);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(users));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/create")
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(name))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.userPhoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.userEmail").value(email));
    }

    @Test
    public void testDeleteUser() throws Exception {
        String name = "Jane";
        Long id = 11L;
        String lastName = "Doe";
        String email = "jane.doe@example.org";
        String phoneNumber = "4105551212";

        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        JSONObject userObject = new JSONObject();
        userObject.put("dogs", dogs);
        userObject.put("id", id);
        userObject.put("name", name);
        userObject.put("lastName", lastName);
        userObject.put("email", email);
        userObject.put("phoneNumber", phoneNumber);

        Users users = new Users();
        users.setDog(dogs);
        users.setFirstName(name);
        users.setId(id);
        users.setLastName(lastName);
        users.setUserEmail(email);
        users.setUserPhoneNumber(phoneNumber);

        when(service.deleteUser(any(Long.class))).thenReturn(users);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(users));
        doNothing().when(repository).deleteById(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/delete/" + id)
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(name))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.userPhoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.userEmail").value(email));
    }


    @Test
    public void testUpdateUser() throws Exception {
        String name = "Jane";
        Long id = 11L;
        String lastName = "Doe";
        String email = "jane.doe@example.org";
        String phoneNumber = "4105551212";

        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        JSONObject userObject = new JSONObject();
        userObject.put("dogs", dogs);
        userObject.put("id", id);
        userObject.put("name", name);
        userObject.put("lastName", lastName);
        userObject.put("email", email);
        userObject.put("phoneNumber", phoneNumber);

        Users users = new Users();
        users.setDog(dogs);
        users.setFirstName("Jane");
        users.setId(11L);
        users.setLastName("Doe");
        users.setUserEmail("jane.doe@example.org");
        users.setUserPhoneNumber("4105551212");

        when(service.updateUser(any(Long.class), any(Users.class))).thenReturn(users);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(users));

        Users users2 = new Users();
        users.setDog(dogs);
        users.setFirstName(name);
        users.setId(id);
        users.setLastName(lastName);
        users.setUserEmail(email);
        users.setUserPhoneNumber(phoneNumber);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/update/" + id, users2)
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "{\"id\":11,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"userPhoneNumber\":\"4105551212\",\"userEmail\":\"jane.doe"
                                + "@example.org\",\"dog\":{\"id\":11,\"nickname\":\"Sharik\",\"age\":1,\"infoDog\":\"Info Dog\"}}"));
    }
}
