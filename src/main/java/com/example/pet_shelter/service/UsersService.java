package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.UsersNullParameterValueException;
import com.example.pet_shelter.listener.TelegramBotUpdatesListener;
import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.regex.Pattern;

@Service
public class UsersService {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Collection<Users> getAllUsers() {
        return this.usersRepository.findAll();
    }

    /**
     * <i>Проверка параметров пользователя, занесение пользователя в базу данных</i>
     *
     * @param user передает пользователя
     * @see UsersRepository
     * @see Users
     */
    public Users createUserInDb(Users user) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        String userNewNumber = user.getUserPhoneNumber();
        if (user.getFirstName().isBlank() || user.getFirstName().isBlank()) {
            throw new UsersNullParameterValueException("Имя пользователя не указано");
        }
        if (user.getFirstName().isBlank() || user.getFirstName().isBlank()) {
            throw new UsersNullParameterValueException("Фамилия пользователя не указана");
        }
        // Форматирование телефона пользователя, если телефон указан неверно получаем null
        if (userNewNumber != null && MatchingPhoneNumber(userNewNumber) != null) {
            user.setUserPhoneNumber(MatchingPhoneNumber(userNewNumber));
        } else {
            throw new UsersNullParameterValueException("Телефон пользователя не указан или не соответствует формату");
        }
        if (!ValidityEmail(user.getUserEmail())) {
            throw new UsersNullParameterValueException("Почта пользователя не указана или не соответствует формату");
        }
        return this.usersRepository.save(user);
    }

    /**
     * <i>Удаляет из базы питомца по id</i>
     *
     * @param id Id пользователя в базе данных
     * @see UsersRepository
     */
    public Users deleteUser(Long id) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        Users deleteUser = usersRepository.findById(id).orElse(null);
        usersRepository.deleteById(id);
        return deleteUser;
    }

    /**
     * <i>Заменяет старые параметры пользователя на те что были переданы.
     * Если объект по id не найден будет выкинуто исключение UsersNullParameterValueException.
     * При отсутсвии одного из полей у передаваемого объекта user будет выкинуто исключение NullPointerException.
     * </i>
     *
     * @param id   Id пользователя в базе данных
     * @param user объект пользователя
     * @see com.example.pet_shelter.repository.DogsRepository
     * @see Users
     */
    public Users updateUser(Long id, Users user) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        Users updateUser = usersRepository.findById(id).orElse(null);
        if (updateUser != null) {
            updateUser.setFirstName(user.getFirstName());
            updateUser.setLastName(user.getLastName());
            updateUser.setUserPhoneNumber(user.getUserPhoneNumber());
            updateUser.setUserEmail(user.getUserEmail());
        } else {
            throw new UsersNullParameterValueException("Недостаточно данных при попытке заменить данные у объекта users");
        }
        return usersRepository.save(updateUser);
    }

    /**
     * <i>Проверяет правильность написания номера телефона, если номер указан неверно, метод возвращает null
     * Если номер указан верно, он подгоняется под общий формат </i>
     *
     * @param telefone номер телефона пользователя
     * @see UsersRepository
     */
    public String MatchingPhoneNumber(String telefone) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        if (telefone.chars().filter(Character::isDigit).count() == 11) {
            String str = telefone.replaceAll("\\D+", "");
            String firstCharacter = str.substring(0, 1);
            if (firstCharacter.equals("8")) {
                return (str.charAt(0) + "(" + str.substring(1, 4) + ")" + str.substring(4, 7)
                        + "-" + str.substring(7, 9) + "-" + str.substring(9, 11));
            } else {
                return ("+" + str.charAt(0) + "(" + str.substring(1, 4) + ")" + str.substring(4, 7)
                        + "-" + str.substring(7, 9) + "-" + str.substring(9, 11));
            }
        } else {
            return null;
        }
    }

    /**
     * <i>Проверяет правильность написания электронной почты пользователя, если почта указана неверно, метод возвращает false</i>
     *
     * @param eMail электронная почта пользователя
     * @see UsersRepository
     */
    public boolean ValidityEmail(String eMail) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        if (eMail == null) {
            return false;
        } else {
            String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
            return patternMatches(eMail, regexPattern);
        }
    }

    /**
     * <i>Вспомогательный метод, соответствующий шаблону регулярных выражений</i>
     *
     * @param TheStringBeingChecked строка которую нужно проверить
     * @param regexPattern          паттерн для проверки строки
     * @see Pattern
     * @see java.util.regex.Matcher
     */
    public static boolean patternMatches(String TheStringBeingChecked, String regexPattern) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);

        return Pattern.compile(regexPattern)
                .matcher(TheStringBeingChecked)
                .matches();
    }

}
