package com.example.pet_shelter.service;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.DogsFoto;
import com.example.pet_shelter.repository.DogsFotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {DogsFotoService.class})
@ExtendWith(SpringExtension.class)
class DogsFotoServiceTest {
    @MockBean
    private DogsFotoRepository dogsFotoRepository;

    @MockBean
    private DogsFotoService dogsFotoService;

    @Value("${dir.path.FotoDogs}")
    private String FotoDogsDir;

    @Test
    void uploadFotoDogsTest() throws Exception {
        String nickname = "Sharik";
        Long id = 11L;
        int age = -1;
        String info = "Info Dog";

        Dogs dog = new Dogs();
        dog.setId(id);
        dog.setNickname(nickname);
        dog.setAge(age);
        dog.setInfoDog(info);

        Path path = Paths.get("/path/to/the/file.txt");
        String name = "file.txt";
        String originalFileName = "file.txt";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);
        Path filePath = Path.of(FotoDogsDir, id + "." + path);

        DogsFoto dogsFoto = new DogsFoto();
        dogsFoto.setDog(dog);
        dogsFoto.setFilePath(filePath.toString());
        dogsFoto.setFileSize(3L);
        dogsFoto.setId(id);
        dogsFoto.setMediaType("Media Type");

        when(dogsFotoRepository.findById((Long) any())).thenReturn(Optional.of(dogsFoto));
        when(dogsFotoService.uploadFotoDog(any(Long.class), any(MultipartFile.class))).thenReturn(dogsFoto);

        assertSame(dogsFoto, dogsFotoService.uploadFotoDog(id, result));
    }

    @Test
    void getExtensionsTest() throws Exception {
        String name1 = "Name";
        String name2 = "NameFile.Name";
        String s = name2.substring(name2.lastIndexOf(".") + 1);

        when(dogsFotoService.getExtensions((String) any())).thenReturn(s);

        assertEquals(name1, dogsFotoService.getExtensions(s));
        verify(dogsFotoService).getExtensions((String) any());
    }


    @Test
    void testFindDogs() throws Exception {
        String nickname = "Sharik";
        Long id = 11L;
        int age = -1;
        String info = "Info Dog";

        Dogs dog = new Dogs();
        dog.setId(id);
        dog.setNickname(nickname);
        dog.setAge(age);
        dog.setInfoDog(info);

        DogsFoto dogsFoto = new DogsFoto();
        dogsFoto.setDog(dog);
        dogsFoto.setFilePath("/directory/foo.txt");
        dogsFoto.setFileSize(3L);
        dogsFoto.setId(id);
        dogsFoto.setMediaType("Media Type");

        when(dogsFotoService.findDog((Long) any())).thenReturn(dog);

        assertSame(dog, dogsFotoService.findDog(11L));
        verify(dogsFotoService).findDog((Long) any());
    }

    @Test
    void testFindFotoDogs() throws Exception {
        String nickname = "Sharik";
        Long id = 11L;
        int age = -1;
        String info = "Info Dog";

        Dogs dog = new Dogs();
        dog.setId(id);
        dog.setNickname(nickname);
        dog.setAge(age);
        dog.setInfoDog(info);

        DogsFoto dogsFoto = new DogsFoto();
        dogsFoto.setDog(dog);
        dogsFoto.setFilePath("/directory/foo.txt");
        dogsFoto.setFileSize(3L);
        dogsFoto.setId(id);
        dogsFoto.setMediaType("Media Type");

        when(dogsFotoService.findFotoDog((Long) any())).thenReturn(dogsFoto);

        assertSame(dogsFoto, dogsFotoService.findFotoDog(11L));
        verify(dogsFotoService).findFotoDog((Long) any());
    }
}

