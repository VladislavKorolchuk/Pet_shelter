package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.ReportUsers;
import com.example.pet_shelter.service.ReportUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/report")
public class ReportUserController {

    ReportUsersService reportUsersService;

    public ReportUserController(ReportUsersService reportUsersService) {
        this.reportUsersService = reportUsersService;
    }

    @Operation(summary = "Просмотр фотографии питомца(отчет)",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Фотография найдена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))}, tags = "REPORT")
    @GetMapping(value = "/{id}/reportUser")
    public void downloadReportUser(@Parameter(description = "Id питомца", example = "1") @PathVariable Long id, HttpServletResponse response) throws IOException {
        ReportUsers reportUsers = reportUsersService.findReportUsers(id);
        Path path = Path.of(reportUsers.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType("image/jpeg");
            response.setContentLength((int) reportUsers.getFileSize());
            is.transferTo(os);
        }
    }

}
