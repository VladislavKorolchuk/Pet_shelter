package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.SheltersNullParameterValueException;
import com.example.pet_shelter.model.Shelters;
import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.repository.ShelterRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ShelterService.class})
@ExtendWith(SpringExtension.class)
public class ShelterServiceTest {

    @MockBean
    private ShelterRepository shelterRepository;

    @Autowired
    private ShelterService shelterService;

    @Test
    void testGetAllShelters() {
        ArrayList<Shelters> sheltersArrayList = new ArrayList<>();
        when(shelterRepository.findAll()).thenReturn(sheltersArrayList);
        Collection<Shelters> actualAllShelters = shelterService.getAllShelters();
        assertSame(sheltersArrayList, actualAllShelters);
        assertTrue(actualAllShelters.isEmpty());
        verify(shelterRepository).findAll();
    }


    @Test
    void testGetAllShelters2() {
        when(shelterRepository.findAll()).thenThrow(new SheltersNullParameterValueException("Massage"));
        assertThrows(SheltersNullParameterValueException.class, () -> shelterService.getAllShelters());
        verify(shelterRepository).findAll();
    }


    @Test
    void testCreateShelters() {
        Long id = 11L;
        String nameShelter = "Shelter \"Cool dogs\"";
        String descriptionShelter = "Take the dogs away!";

        Shelters shelters = new Shelters();
        shelters.setId(id);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);

        when(shelterRepository.save((Shelters) any())).thenReturn(shelters);

        Shelters shelters1 = mock(Shelters.class);

        when(shelters1.getNameShelter())
                .thenThrow(new SheltersNullParameterValueException(
                        "Имя приюта не указано"));
        when(shelters1.getDescriptionShelter()).thenReturn(descriptionShelter);

        doNothing().when(shelters1).setId((Long) any());
        doNothing().when(shelters1).setNameShelter((String) any());
        doNothing().when(shelters1).setDescriptionShelter((String) any());

        shelters1.setId(id);
        shelters1.setNameShelter(nameShelter);
        shelters1.setDescriptionShelter(descriptionShelter);

        assertThrows(SheltersNullParameterValueException.class, () -> shelterService.createShelter(shelters1));

        verify(shelters1, atLeast(1)).getNameShelter();
        verify(shelters1).setId((Long) any());
        verify(shelters1).setDescriptionShelter((String) any());
    }

    @Test
    void testCreateShelters2() {
        Long id = 11L;
        String nameShelter = "Shelter \"Cool dogs\"";
        String descriptionShelter = "Take the dogs away!";

        Shelters shelters = new Shelters();
        shelters.setId(id);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);

        when(shelterRepository.save((Shelters) any())).thenReturn(shelters);

        Shelters shelters1 = mock(Shelters.class);

        when(shelters1.getNameShelter()).thenReturn("");
        when(shelters1.getDescriptionShelter()).thenReturn(descriptionShelter);
        doNothing().when(shelters1).setNameShelter((String) any());
        doNothing().when(shelters1).setDescriptionShelter((String) any());
        doNothing().when(shelters1).setId((Long) any());

        shelters1.setNameShelter(nameShelter);
        shelters1.setId(id);
        shelters1.setDescriptionShelter(descriptionShelter);

        assertThrows(SheltersNullParameterValueException.class, () -> shelterService.createShelter(shelters1));

        verify(shelters1, atLeast(1)).getNameShelter();
        verify(shelters1).setId((Long) any());
        verify(shelters1).setDescriptionShelter((String) any());
    }


    @Test
    void testCreateShelters3() {
        Long id = 11L;
        String nameShelter = "Shelter \"Cool dogs\"";
        String descriptionShelter = "Take the dogs away!";

        Shelters shelters = new Shelters();
        shelters.setId(id);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);


        when(shelterRepository.save((Shelters) any())).thenReturn(shelters);


        Shelters shelters1 = mock(Shelters.class);

        when(shelters1.getNameShelter()).thenReturn("");
        doNothing().when(shelters1).setNameShelter((String) any());
        doNothing().when(shelters1).setId((Long) any());
        doNothing().when(shelters1).setDescriptionShelter((String) any());

        shelters1.setId(id);
        shelters1.setNameShelter(nameShelter);
        shelters1.setDescriptionShelter(descriptionShelter);

        assertThrows(SheltersNullParameterValueException.class, () -> shelterService.createShelter(shelters1));
        verify(shelters1).getNameShelter();
        verify(shelters1).setNameShelter((String) any());
        verify(shelters1).setDescriptionShelter((String) any());
        verify(shelters1).setId((Long) any());
    }


    @Test
    void testDeleteShelters() {
        Long id = 11L;
        String nameShelter = "Shelter \"Cool dogs\"";
        String descriptionShelter = "Take the dogs away!";

        Shelters shelters = new Shelters();
        shelters.setId(id);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);

        Users users = new Users();


        Optional<Shelters> ofResult = Optional.of(shelters);

        when(shelterRepository.findById((Long) any())).thenReturn(ofResult);
        doNothing().when(shelterRepository).deleteById((Long) any());

        assertSame(shelters, shelterService.deleteShelter(id));
        verify(shelterRepository).findById((Long) any());
        verify(shelterRepository).deleteById((Long) any());
    }


    @Test
    void testDeleteShelters2() {
        when(shelterRepository.findById((Long) any())).thenThrow(new SheltersNullParameterValueException("Massage"));
        doThrow(new SheltersNullParameterValueException("Massage")).when(shelterRepository).deleteById((Long) any());
        assertThrows(SheltersNullParameterValueException.class, () -> shelterService.deleteShelter(11L));
        verify(shelterRepository).findById((Long) any());
    }


    @Test
    void testUpdateShelters() {
        Long id = 11L;
        String nameShelter = "Shelter \"Cool dogs\"";
        String descriptionShelter = "Take the dogs away!";

        Shelters shelters = new Shelters();
        shelters.setId(id);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);

        when(shelterRepository.save((Shelters) any())).thenReturn(shelters);
        when(shelterRepository.findById((Long) any())).thenReturn(Optional.of(shelters));

        Shelters shelters1 = new Shelters();
        shelters1.setId(id);
        shelters1.setNameShelter(nameShelter);
        shelters1.setDescriptionShelter(descriptionShelter);

        assertSame(shelters, shelterService.updateShelter(id, shelters1));
        verify(shelterRepository).save((Shelters) any());
        verify(shelterRepository).findById((Long) any());
    }

    @Test
    void testUpdateShelters2() {
        Long id = 11L;
        String nameShelter = "Shelter \"Cool dogs\"";
        String descriptionShelter = "Take the dogs away!";

        Shelters shelters = new Shelters();
        shelters.setId(id);
        shelters.setNameShelter(nameShelter);
        shelters.setDescriptionShelter(descriptionShelter);

        when(shelterRepository.save((Shelters) any())).thenThrow(new SheltersNullParameterValueException("Massage"));
        when(shelterRepository.findById((Long) any())).thenReturn(Optional.of(shelters));

        assertThrows(SheltersNullParameterValueException.class, () -> shelterService.updateShelter(id, shelters));
        verify(shelterRepository).save((Shelters) any());
        verify(shelterRepository).findById((Long) any());
    }

    @Test
    void testUpdateShelters3() {
        Long id = 11L;
        String nameShelter = "Shelter \"Cool dogs\"";
        String descriptionShelter = "Take the dogs away!";

        Shelters shelters = new Shelters();
        shelters.setNameShelter("");
        shelters.setId(id);

        when(shelterRepository.save((Shelters) any())).thenReturn(shelters);
        when(shelterRepository.findById((Long) any())).thenReturn(Optional.empty());

        assertThrows(SheltersNullParameterValueException.class, () -> shelterService.updateShelter(id, shelters));
        verify(shelterRepository).findById((Long) any());
    }
}
