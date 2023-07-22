package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.UsersNullParameterValueException;
import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.repository.UsersRepository;
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

@ContextConfiguration(classes = {UsersService.class})
@ExtendWith(SpringExtension.class)
class UsersServiceTest {
    @MockBean
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    @Test
    void testGetAllUsers() {
        ArrayList<Users> usersList = new ArrayList<>();
        when(usersRepository.findAll()).thenReturn(usersList);
        Collection<Users> actualAllUsers = usersService.getAllUsers();
        assertSame(usersList, actualAllUsers);
        assertTrue(actualAllUsers.isEmpty());
        verify(usersRepository).findAll();
    }


    @Test
    void testGetAllUsers2() {
        when(usersRepository.findAll()).thenThrow(new UsersNullParameterValueException("Massage"));
        assertThrows(UsersNullParameterValueException.class, () -> usersService.getAllUsers());
        verify(usersRepository).findAll();
    }


    @Test
    void testCreateUser() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        Users users = new Users();
        users.setDog(dogs);
        users.setFirstName("Jane");
        users.setId(11L);
        users.setLastName("Doe");
        users.setUserEmail("jane.doe@example.org");
        users.setUserPhoneNumber("4105551212");

        when(usersRepository.save((Users) any())).thenReturn(users);

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        Users users1 = mock(Users.class);

        when(users1.getUserEmail())
                .thenThrow(new UsersNullParameterValueException(
                        "Почта пользователя не указана или не соответсвует формату"));
        when(users1.getUserPhoneNumber()).thenReturn("4105551212");
        when(users1.getFirstName()).thenReturn("Jane");
        when(users1.getUserPhoneNumber()).thenReturn("Jane");

        doNothing().when(users1).setDog((Dogs) any());
        doNothing().when(users1).setFirstName((String) any());
        doNothing().when(users1).setId((Long) any());
        doNothing().when(users1).setLastName((String) any());
        doNothing().when(users1).setUserEmail((String) any());
        doNothing().when(users1).setUserPhoneNumber((String) any());

        users1.setDog(dogs1);
        users1.setFirstName("Jane");
        users1.setId(11L);
        users1.setLastName("Doe");
        users1.setUserEmail("jane.doe@example.org");
        users1.setUserPhoneNumber("4105551212");

        assertThrows(UsersNullParameterValueException.class, () -> usersService.createUserInDb(users1));

        verify(users1, atLeast(1)).getFirstName();
        verify(users1, atLeast(1)).getUserPhoneNumber();
        verify(users1).setDog((Dogs) any());
        verify(users1).setFirstName((String) any());
        verify(users1).setId((Long) any());
        verify(users1).setLastName((String) any());
        verify(users1).setUserEmail((String) any());
        verify(users1, atLeast(1)).setUserPhoneNumber((String) any());
    }

    @Test
    void testCreateUser2() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        Users users = new Users();
        users.setDog(dogs);
        users.setFirstName("Jane");
        users.setId(11L);
        users.setLastName("Doe");
        users.setUserEmail("jane.doe@example.org");
        users.setUserPhoneNumber("4105551212");
        when(usersRepository.save((Users) any())).thenReturn(users);

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        Users users1 = mock(Users.class);

        when(users1.getUserEmail()).thenReturn("foo");
        when(users1.getUserPhoneNumber()).thenReturn("4105551212");
        when(users1.getFirstName()).thenReturn("Jane");
        doNothing().when(users1).setDog((Dogs) any());
        doNothing().when(users1).setFirstName((String) any());
        doNothing().when(users1).setId((Long) any());
        doNothing().when(users1).setLastName((String) any());
        doNothing().when(users1).setUserEmail((String) any());
        doNothing().when(users1).setUserPhoneNumber((String) any());

        users1.setDog(dogs1);
        users1.setFirstName("Jane");
        users1.setId(11L);
        users1.setLastName("Doe");
        users1.setUserEmail("jane.doe@example.org");
        users1.setUserPhoneNumber("4105551212");

        assertThrows(UsersNullParameterValueException.class, () -> usersService.createUserInDb(users1));
        verify(users1, atLeast(1)).getFirstName();
        verify(users1, atLeast(1)).getUserPhoneNumber();
        verify(users1).setDog((Dogs) any());
        verify(users1).setFirstName((String) any());
        verify(users1).setId((Long) any());
        verify(users1).setLastName((String) any());
        verify(users1).setUserEmail((String) any());
        verify(users1, atLeast(1)).setUserPhoneNumber((String) any());
    }


    @Test
    void testCreateUser3() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        Users users = new Users();
        users.setDog(dogs);
        users.setFirstName("Jane");
        users.setId(11L);
        users.setLastName("Doe");
        users.setUserEmail("jane.doe@example.org");
        users.setUserPhoneNumber("4105551212");
        when(usersRepository.save((Users) any())).thenReturn(users);

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        Users users1 = mock(Users.class);
        when(users1.getUserEmail()).thenReturn("jane.doe@example.org");
        when(users1.getUserPhoneNumber()).thenReturn("4105551212");
        when(users1.getFirstName()).thenReturn("");
        doNothing().when(users1).setDog((Dogs) any());
        doNothing().when(users1).setFirstName((String) any());
        doNothing().when(users1).setId((Long) any());
        doNothing().when(users1).setLastName((String) any());
        doNothing().when(users1).setUserEmail((String) any());
        doNothing().when(users1).setUserPhoneNumber((String) any());

        users1.setDog(dogs1);
        users1.setFirstName("Jane");
        users1.setId(11L);
        users1.setLastName("Doe");
        users1.setUserEmail("jane.doe@example.org");
        users1.setUserPhoneNumber("4105551212");

        assertThrows(UsersNullParameterValueException.class, () -> usersService.createUserInDb(users1));
        verify(users1).getFirstName();
        verify(users1).setDog((Dogs) any());
        verify(users1).setFirstName((String) any());
        verify(users1).setId((Long) any());
        verify(users1).setLastName((String) any());
        verify(users1).setUserEmail((String) any());
        verify(users1).setUserPhoneNumber((String) any());
    }


    @Test
    void testDeleteUser() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        Users users = new Users();
        users.setDog(dogs);
        users.setFirstName("Jane");
        users.setId(11L);
        users.setLastName("Doe");
        users.setUserEmail("jane.doe@example.org");
        users.setUserPhoneNumber("4105551212");
        Optional<Users> ofResult = Optional.of(users);

        when(usersRepository.findById((Long) any())).thenReturn(ofResult);
        doNothing().when(usersRepository).deleteById((Long) any());

        assertSame(users, usersService.deleteUser(11L));
        verify(usersRepository).findById((Long) any());
        verify(usersRepository).deleteById((Long) any());
    }


    @Test
    void testDeleteUser2() {
        when(usersRepository.findById((Long) any())).thenThrow(new UsersNullParameterValueException("Massage"));
        doThrow(new UsersNullParameterValueException("Massage")).when(usersRepository).deleteById((Long) any());
        assertThrows(UsersNullParameterValueException.class, () -> usersService.deleteUser(11L));
        verify(usersRepository).findById((Long) any());
    }


    @Test
    void testUpdateUser() {
        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        Users users1 = new Users();
        users1.setDog(dogs1);
        users1.setFirstName("Jane");
        users1.setId(11L);
        users1.setLastName("Doe");
        users1.setUserEmail("jane.doe@example.org");
        users1.setUserPhoneNumber("4105551212");

        when(usersRepository.save((Users) any())).thenReturn(users1);
        when(usersRepository.findById((Long) any())).thenReturn(Optional.of(users1));

        Dogs dogs2 = new Dogs();
        dogs2.setAge(1);
        dogs2.setId(11L);
        dogs2.setInfoDog("Info Dog");
        dogs2.setNickname("Sharik");

        Users users2 = new Users();
        users2.setDog(dogs2);
        users2.setFirstName("Jane");
        users2.setId(11L);
        users2.setLastName("Doe");
        users2.setUserEmail("jane.doe@example.org");
        users2.setUserPhoneNumber("4105551212");

        assertSame(users1, usersService.updateUser(11L, users2));
        verify(usersRepository).save((Users) any());
        verify(usersRepository).findById((Long) any());
    }

    @Test
    void testUpdateUser2() {
        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        Users users1 = new Users();
        users1.setDog(dogs1);
        users1.setFirstName("Jane");
        users1.setId(11L);
        users1.setLastName("Doe");
        users1.setUserEmail("jane.doe@example.org");
        users1.setUserPhoneNumber("4105551212");

        when(usersRepository.save((Users) any())).thenThrow(new UsersNullParameterValueException("Massage"));
        when(usersRepository.findById((Long) any())).thenReturn(Optional.of(users1));

        assertThrows(UsersNullParameterValueException.class, () -> usersService.updateUser(11L, users1));
        verify(usersRepository).save((Users) any());
        verify(usersRepository).findById((Long) any());
    }

    @Test
    void testUpdateUser3() {
        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        Users users1 = new Users();
        users1.setDog(dogs1);
        users1.setFirstName("");
        users1.setId(11L);
        users1.setLastName("Doe");
        users1.setUserEmail("jane.doe@example.org");
        users1.setUserPhoneNumber("4105551212");

        when(usersRepository.save((Users) any())).thenReturn(users1);
        when(usersRepository.findById((Long) any())).thenReturn(Optional.empty());

        assertThrows(UsersNullParameterValueException.class, () -> usersService.updateUser(11L, users1));
        verify(usersRepository).findById((Long) any());
    }


    @Test
    void testMatchingPhoneNumber() {
        assertEquals("+7(222)334-00-80", usersService.MatchingPhoneNumber("72223340080"));
        assertNull(usersService.MatchingPhoneNumber("89113214568794"));
    }


    @Test
    void testValidityEmail() {
        assertFalse(usersService.ValidityEmail("E Mail"));
        assertTrue(usersService.ValidityEmail("U@U"));
        assertFalse(usersService.ValidityEmail(null));
    }

    @Test
    void testPatternMatches() {
        assertTrue(UsersService.patternMatches("The String Being Checked", ".*"));
        assertFalse(UsersService.patternMatches("The String Being Checked", "U"));
    }
}

