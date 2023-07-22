package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.DogNullParameterValueException;
import com.example.pet_shelter.listener.TelegramBotUpdatesListener;
import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.repository.DogsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DogsService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final DogsRepository dogsRepository;

    public DogsService(DogsRepository dogsRepository) {
        this.dogsRepository = dogsRepository;
    }

    public Collection<Dogs> getAllDogs() {
        return this.dogsRepository.findAll();
    }

    /**
     * <i>Заносит в базу созданный объект питомца.
     * Если объект пуст будует выкинуто исключение DogNullParameterValueException.</i>
     *
     * @param dog объект питомца
     * @see DogsRepository
     */
    public Dogs createDogInDB(Dogs dog) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        if (dog.getNickname().isBlank() || dog.getNickname().isEmpty()) {
            throw new DogNullParameterValueException("Кличка питомца не указана");
        }
        return dogsRepository.save(dog);
    }

    /**
     * <i>Удаляет из базы питомца по id</i>
     *
     * @param id Id питомца в базе данных
     * @see DogsRepository
     */
    public Dogs deleteDog(Long id) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        Dogs deleteDog = dogsRepository.findById(id).orElse(null);
        dogsRepository.deleteById(id);
        return deleteDog;
    }

    /**
     * <i>Заменяет старые параметры питомца на те что были переданы.
     * Если объект по id не найден будет выкинуто исключение DogNullParameterValueException.
     * При отсутсвии одного из полей у передаваемого объекта dog будет выкинуто исключение NullPointerException.
     * </i>
     *
     * @param id  Id питомца в базе данных
     * @param dog объект питомца
     * @see DogsRepository
     */
    public Dogs updateDog(Long id, Dogs dog) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        Dogs updateDog = dogsRepository.findById(id).orElse(null);
        if (updateDog != null) {
            updateDog.setNickname(dog.getNickname());
            updateDog.setAge(dog.getAge());
            updateDog.setInfoDog(dog.getInfoDog());
        } else {
            throw new DogNullParameterValueException("Недостаточно данных при попытке заменить данные у объекта dogs");
        }
        return dogsRepository.save(updateDog);
    }
}
