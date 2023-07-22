package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.service.UsersService;
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
@RequestMapping("/user")
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Operation(
            summary = "Получение списка всех пользователей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список пользователей",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Users.class))
                            )
                    )
            },tags = "USER"
    )
    @GetMapping("/getAll")
    public Collection<Users> getAllUsers() {
        return this.usersService.getAllUsers();
    }

    @Operation(
            summary = "Внесение данных о новом пользователе",
            description = "Внесение полных данных пользователя в определённом формате",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вернёт объект пользователя с данными, которые он внёс",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Users.class)
                            )),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка со стороны сервиса(Так же при неполных данных пользователя)"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка при внесении пользователя в базу из-за неверного формата данных"
                    )
            },tags = "USER")
    @PostMapping("/create")
    public Users createUserInDb(@Parameter(
            description = "Полные данные пользователя",
            example = "{firstName: Name, lastName : LastName, userPhoneNumber : +75558804420, userEmail: mail@mail.ru}")
                                @RequestBody Users user) {
        return this.usersService.createUserInDb(user);
    }

    @Operation(
            summary = "Удаление данных о пользователе",
            description = "Удаляет пользователя по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вернёт объект пользователя по указанному id, предварительно удалив его из базы",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Users.class)
                            )),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Пользователь не найден в базе"
                    )
            },tags = "USER")

    @DeleteMapping("/delete/{id}")
    public Users deleteUser(@Parameter(
            description = "Id пользователя, которого необходимо удалить",
            example = "1")
                            @PathVariable("id") Long id) {
        return this.usersService.deleteUser(id);
    }

    @Operation(
            summary = "Изменение данных о пользователе",
            description = "Ищет пользователя по id, затем заменяет в нем данные на те, что записаны в передаваемом user."
                    + " Если в одном из параметров у dog указана пустая строка этот параметр не будет изменён.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вернёт объект users с обновленными данными",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Users.class)
                            )),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Недостаточно данных для обновления объекта users или объект с таким id не найден"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка при обновлении объекта users из-за неверного формата данных"
                    )},tags = "USER"
    )
    @PutMapping("/update/{id}")
    public Users updateUsers(@Parameter(
            description = "Id пользователя",
            example = "1") @PathVariable("id") Long id,
                             @Parameter(
                                     description = "Полные данные пользователя",
                                     example = "{firstName: Name, lastName : LastName, userPhoneNumber : +75558804420, userEmail: mail@mail.ru}")
                             @RequestBody Users user) {
        return this.usersService.updateUser(id, user);
    }

}
