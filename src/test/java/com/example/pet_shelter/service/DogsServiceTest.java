package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.DogNullParameterValueException;
import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.repository.DogsRepository;
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

@ContextConfiguration(classes = {DogsService.class})
@ExtendWith(SpringExtension.class)
class DogsServiceTest {
    @MockBean
    private DogsRepository dogsRepository;

    @Autowired
    private DogsService dogsService;

    @Test
    void testGetAllDogs() {
        ArrayList<Dogs> dogsList = new ArrayList<>();
        when(dogsRepository.findAll()).thenReturn(dogsList);
        Collection<Dogs> actualAllDogs = dogsService.getAllDogs();
        assertSame(dogsList, actualAllDogs);
        assertTrue(actualAllDogs.isEmpty());
        verify(dogsRepository).findAll();
    }

    @Test
    void testGetAllDogs2() {
        when(dogsRepository.findAll()).thenThrow(new DogNullParameterValueException("Massage"));
        assertThrows(DogNullParameterValueException.class, () -> dogsService.getAllDogs());
        verify(dogsRepository).findAll();
    }

    @Test
    void testCreateDog() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");
        when(dogsRepository.save((Dogs) any())).thenReturn(dogs);

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");
        assertSame(dogs, dogsService.createDogInDB(dogs1));
        verify(dogsRepository).save((Dogs) any());
    }

    @Test
    void testCreateDog2() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        when(dogsRepository.save((Dogs) any())).thenReturn(dogs);

        Dogs dogs1 = mock(Dogs.class);

        when(dogs1.getNickname()).thenReturn("");

        doNothing().when(dogs1).setAge(anyInt());
        doNothing().when(dogs1).setId((Long) any());
        doNothing().when(dogs1).setInfoDog((String) any());
        doNothing().when(dogs1).setNickname((String) any());

        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        assertThrows(DogNullParameterValueException.class, () -> dogsService.createDogInDB(dogs1));

        verify(dogs1).getNickname();
        verify(dogs1).setAge(anyInt());
        verify(dogs1).setId((Long) any());
        verify(dogs1).setInfoDog((String) any());
        verify(dogs1).setNickname((String) any());
    }

    @Test
    void testDeleteDog() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");
        Optional<Dogs> ofResult = Optional.of(dogs);
        when(dogsRepository.findById((Long) any())).thenReturn(ofResult);
        doNothing().when(dogsRepository).deleteById((Long) any());
        assertSame(dogs, dogsService.deleteDog(11L));
        verify(dogsRepository).findById((Long) any());
        verify(dogsRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteDog2() {
        when(dogsRepository.findById((Long) any())).thenThrow(new DogNullParameterValueException("Massage"));
        doThrow(new DogNullParameterValueException("Massage")).when(dogsRepository).deleteById((Long) any());
        assertThrows(DogNullParameterValueException.class, () -> dogsService.deleteDog(11L));
        verify(dogsRepository).findById((Long) any());
    }


    @Test
    void testUpdateDog() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");
        Optional<Dogs> ofResult = Optional.of(dogs);

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");
        when(dogsRepository.save((Dogs) any())).thenReturn(dogs1);
        when(dogsRepository.findById((Long) any())).thenReturn(ofResult);

        Dogs dogs2 = new Dogs();
        dogs2.setAge(1);
        dogs2.setId(11L);
        dogs2.setInfoDog("Info Dog");
        dogs2.setNickname("Sharik");
        assertSame(dogs1, dogsService.updateDog(11L, dogs2));
        verify(dogsRepository).save((Dogs) any());
        verify(dogsRepository).findById((Long) any());
    }


    @Test
    void testUpdateDog2() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");
        Optional<Dogs> ofResult = Optional.of(dogs);
        when(dogsRepository.save((Dogs) any())).thenThrow(new DogNullParameterValueException("Massage"));
        when(dogsRepository.findById((Long) any())).thenReturn(ofResult);

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");
        assertThrows(DogNullParameterValueException.class, () -> dogsService.updateDog(11L, dogs1));
        verify(dogsRepository).save((Dogs) any());
        verify(dogsRepository).findById((Long) any());
    }

    @Test
    void testUpdateDog3() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");
        when(dogsRepository.save((Dogs) any())).thenReturn(dogs);
        when(dogsRepository.findById((Long) any())).thenReturn(Optional.empty());

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");
        assertThrows(DogNullParameterValueException.class, () -> dogsService.updateDog(11L, dogs1));
        verify(dogsRepository).findById((Long) any());
    }
}

