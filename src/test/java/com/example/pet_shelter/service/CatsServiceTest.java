package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.CatNullParameterValueException;
import com.example.pet_shelter.model.Cats;
import com.example.pet_shelter.repository.CatsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CatsService.class})
@ExtendWith(SpringExtension.class)
class CatsServiceTest {
    @MockBean
    private CatsRepository catsRepository;

    @Autowired
    private CatsService catsService;

    @Test
    void testGetAllCats() {
        ArrayList<Cats> catsList = new ArrayList<>();
        when(catsRepository.findAll()).thenReturn(catsList);
        Collection<Cats> actualAllCats = catsService.getAllCats();
        assertSame(catsList, actualAllCats);
        assertTrue(actualAllCats.isEmpty());
        verify(catsRepository).findAll();
    }

    @Test
    void testGetAllCats2() {
        when(catsRepository.findAll()).thenThrow(new CatNullParameterValueException("Massage"));
        assertThrows(CatNullParameterValueException.class, () -> catsService.getAllCats());
        verify(catsRepository).findAll();
    }

    @Test
    void testCreateCat() {
        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(11L);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");

        when(catsRepository.save((Cats) any())).thenReturn(cats);

        Cats cats1 = new Cats();
        cats1.setAge(1);
        cats1.setId(11L);
        cats1.setInfoCat("Info Cat");
        cats1.setNickname("Murzik");
        assertSame(cats, catsService.createCatInDB(cats1));
        verify(catsRepository).save((Cats) any());
    }

    @Test
    void testCreateCat2() {
        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(11L);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");

        when(catsRepository.save((Cats) any())).thenReturn(cats);

        Cats cats1 = mock(Cats.class);

        when(cats1.getNickname()).thenReturn("");

        doNothing().when(cats1).setAge(anyInt());
        doNothing().when(cats1).setId((Long) any());
        doNothing().when(cats1).setInfoCat((String) any());
        doNothing().when(cats1).setNickname((String) any());

        cats1.setAge(1);
        cats1.setId(11L);
        cats1.setInfoCat("Info Cat");
        cats1.setNickname("Murzik");

        assertThrows(CatNullParameterValueException.class, () -> catsService.createCatInDB(cats1));

        verify(cats1).getNickname();
        verify(cats1).setAge(anyInt());
        verify(cats1).setId((Long) any());
        verify(cats1).setInfoCat((String) any());
        verify(cats1).setNickname((String) any());
    }

    @Test
    void testDeleteCat() {
        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(11L);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");

        when(catsRepository.findById((Long) any())).thenReturn(Optional.of(cats));
        doNothing().when(catsRepository).deleteById((Long) any());

        assertSame(cats, catsService.deleteCat(11L));
        verify(catsRepository).findById((Long) any());
        verify(catsRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteCat2() {
        when(catsRepository.findById((Long) any())).thenThrow(new CatNullParameterValueException("Massage"));
        doThrow(new CatNullParameterValueException("Massage")).when(catsRepository).deleteById((Long) any());
        assertThrows(CatNullParameterValueException.class, () -> catsService.deleteCat(11L));
        verify(catsRepository).findById((Long) any());
    }


    @Test
    void testUpdateCat() {
        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(11L);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");

        Cats cats1 = new Cats();
        cats1.setAge(1);
        cats1.setId(11L);
        cats1.setInfoCat("Info Cat");
        cats1.setNickname("Murzik");
        when(catsRepository.save((Cats) any())).thenReturn(cats1);
        when(catsRepository.findById((Long) any())).thenReturn(Optional.of(cats));

        assertSame(cats1, catsService.updateCat(11L, cats1));
        verify(catsRepository).save((Cats) any());
        verify(catsRepository).findById((Long) any());
    }


    @Test
    void testUpdateCat2() {
        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(11L);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");

        when(catsRepository.save((Cats) any())).thenThrow(new CatNullParameterValueException("Massage"));
        when(catsRepository.findById((Long) any())).thenReturn(Optional.of(cats));

        Cats cats1 = new Cats();
        cats1.setAge(1);
        cats1.setId(11L);
        cats1.setInfoCat("Info Cat");
        cats1.setNickname("Murzik");

        assertThrows(CatNullParameterValueException.class, () -> catsService.updateCat(11L, cats1));
        verify(catsRepository).save((Cats) any());
        verify(catsRepository).findById((Long) any());
    }

    @Test
    void testUpdateDog3() {
        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(11L);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");
        when(catsRepository.save((Cats) any())).thenReturn(cats);
        when(catsRepository.findById((Long) any())).thenReturn(Optional.empty());

        Cats cats1 = new Cats();
        cats1.setAge(1);
        cats1.setId(11L);
        cats1.setInfoCat("Info Cat");
        cats1.setNickname("Murzik");

        assertThrows(CatNullParameterValueException.class, () -> catsService.updateCat(11L, cats1));
        verify(catsRepository).findById((Long) any());
    }
}
