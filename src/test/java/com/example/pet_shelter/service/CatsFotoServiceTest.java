package com.example.pet_shelter.service;

import com.example.pet_shelter.model.Cats;
import com.example.pet_shelter.model.CatsFoto;
import com.example.pet_shelter.repository.CatsFotoRepository;
import com.example.pet_shelter.repository.CatsRepository;
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

@ContextConfiguration(classes = {CatsFotoService.class})
@ExtendWith(SpringExtension.class)
class CatsFotoServiceTest {
    @MockBean
    private CatsFotoRepository catsFotoRepository;

    @MockBean
    private CatsFotoService catsFotoService;

    @MockBean
    private CatsRepository catsRepository;

    @Value("${dir.path.FotoDogs}")
    private String FotoDogsDir;

    @Test
    void uploadFotoCatTest() throws Exception {
        Long id = 11L;

        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(id);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");

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

        CatsFoto catsFoto = new CatsFoto();
        catsFoto.setCat(cats);
        catsFoto.setFilePath(filePath.toString());
        catsFoto.setFileSize(3L);
        catsFoto.setId(id);
        catsFoto.setMediaType("Media Type");

        when(catsFotoRepository.findById((Long) any())).thenReturn(Optional.of(catsFoto));
        when(catsFotoService.uploadFotoCat(any(Long.class), any(MultipartFile.class))).thenReturn(catsFoto);

        assertSame(catsFoto, catsFotoService.uploadFotoCat(id, result));
    }

    @Test
    void getExtensionsTest() throws Exception {
        String name1 = "Name";
        String name2 = "NameFile.Name";
        String s = name2.substring(name2.lastIndexOf(".") + 1);

        when(catsFotoService.getExtensions((String) any())).thenReturn(s);

        assertEquals(name1, catsFotoService.getExtensions(s));
        verify(catsFotoService).getExtensions((String) any());
    }


    @Test
    void testFindCat() throws Exception {
        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(11L);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");

        CatsFoto catsFoto = new CatsFoto();
        catsFoto.setCat(cats);
        catsFoto.setFilePath("/directory/foo.txt");
        catsFoto.setFileSize(3L);
        catsFoto.setId(11L);
        catsFoto.setMediaType("Media Type");

        when(catsFotoService.findCat((Long) any())).thenReturn(cats);

        assertSame(cats, catsFotoService.findCat(11L));
        verify(catsFotoService).findCat((Long) any());
    }

    @Test
    void testFindFotoCat() throws Exception {
        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(11L);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");

        CatsFoto catsFoto = new CatsFoto();
        catsFoto.setCat(cats);
        catsFoto.setFilePath("/directory/foo.txt");
        catsFoto.setFileSize(3L);
        catsFoto.setId(11L);
        catsFoto.setMediaType("Media Type");

        when(catsFotoService.findFotoCat((Long) any())).thenReturn(catsFoto);

        assertSame(catsFoto, catsFotoService.findFotoCat(11L));
        verify(catsFotoService).findFotoCat((Long) any());
    }
}