package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.CatNullParameterValueException;
import com.example.pet_shelter.listener.TelegramBotUpdatesListener;
import com.example.pet_shelter.model.Cats;
import com.example.pet_shelter.repository.CatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CatsService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final CatsRepository catsRepository;

    public CatsService(CatsRepository catsRepository) {
        this.catsRepository = catsRepository;
    }

    public Collection<Cats> getAllCats() {
        return this.catsRepository.findAll();
    }

    /**
     * <i>Заносит в базу созданный объект питомца.
     * Если объект пуст будет выкинуто исключение CatNullParameterValueException.</i>
     *
     * @param cat объект питомца
     * @see CatsRepository
     */
    public Cats createCatInDB(Cats cat) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        if (cat.getNickname().isBlank() || cat.getNickname().isEmpty()) {
            throw new CatNullParameterValueException("Кличка питомца не указана");
        }
        return catsRepository.save(cat);
    }

    /**
     * <i>Удаляет из базы питомца по id</i>
     *
     * @param id Id питомца в базе данных
     * @see CatsRepository
     */
    public Cats deleteCat(Long id) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        Cats deleteCat = catsRepository.findById(id).orElse(null);
        catsRepository.deleteById(id);
        return deleteCat;
    }

    /**
     * <i>Заменяет старые параметры питомца на те что были переданы.
     * Если объект по id не найден будет выкинуто исключение CatNullParameterValueException.
     * При отсутствии одного из полей у передаваемого объекта cat будет выкинуто исключение NullPointerException.
     * </i>
     *
     * @param id  Id питомца в базе данных
     * @param cat объект питомца
     * @see CatsRepository
     */
    public Cats updateCat(Long id, Cats cat) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        Cats updateCat = catsRepository.findById(id).orElse(null);
        if (updateCat != null) {
            updateCat.setNickname(cat.getNickname());
            updateCat.setAge(cat.getAge());
            updateCat.setInfoCat(cat.getInfoCat());
        } else {
            throw new CatNullParameterValueException("Недостаточно данных при попытке заменить данные у объекта Cats");
        }
        return catsRepository.save(updateCat);
    }
}
