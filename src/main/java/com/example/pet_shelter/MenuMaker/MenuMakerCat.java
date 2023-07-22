package com.example.pet_shelter.MenuMaker;

import com.example.pet_shelter.configuration.MenuCatDescription;
import com.example.pet_shelter.configuration.MenuDogDescription;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;

@Component
public class MenuMakerCat {
    /**
     * <i>Клавиатура после выбора приюта кошек</i>
     *
     * @return
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     */
    public InlineKeyboardMarkup afterStartCatKeyBoard() {
        InlineKeyboardButton one = new InlineKeyboardButton("Узнать информацию о приюте").callbackData(MenuCatDescription.AboutCatPetShelter.name());
        InlineKeyboardButton two = new InlineKeyboardButton("Как взять животное из приюта").callbackData(MenuCatDescription.HOWTOTAKECAT.name());
        InlineKeyboardButton three = new InlineKeyboardButton("Прислать отчет о питомце").callbackData(MenuCatDescription.SENDPETREPORT.name());
        InlineKeyboardButton four = new InlineKeyboardButton("Позвать волонтёра").callbackData(MenuCatDescription.CatVolunteer.name());
        InlineKeyboardMarkup startDogKeyBoard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{one},
                new InlineKeyboardButton[]{two},
                new InlineKeyboardButton[]{three},
                new InlineKeyboardButton[]{four});
        return startDogKeyBoard;
    }

    /**
     * <i>Клавиатура menu1Cat</i>
     *
     * @return клавиатура этапа 1
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     */
    public InlineKeyboardMarkup menu1KeyboardCat() {
        InlineKeyboardButton one = new InlineKeyboardButton("рассказать о приюте").callbackData(MenuCatDescription.AboutCatPetShelterDocx.name());
        InlineKeyboardButton two = new InlineKeyboardButton("расписание работы приюта и адрес, схему проезда").callbackData(MenuCatDescription.CATNURSERYLOCATION.name());
        InlineKeyboardButton three = new InlineKeyboardButton("техника безопасности на территории приюта").callbackData(MenuCatDescription.SAFETYRULES.name());
        InlineKeyboardButton four = new InlineKeyboardButton("записать контактные данные для связи").callbackData(MenuCatDescription.WRITECONTACTSCAT.name());
        InlineKeyboardButton five = new InlineKeyboardButton("позвать волонтера").callbackData(MenuCatDescription.CatVolunteer.name());
        InlineKeyboardMarkup keyboardMenu1Cat = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{one},
                new InlineKeyboardButton[]{two},
                new InlineKeyboardButton[]{three},
                new InlineKeyboardButton[]{four},
                new InlineKeyboardButton[]{five});
        return keyboardMenu1Cat;
    }

    /**
     * <i>Клавиатура menu2Cat</i>
     *
     * @return
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     */
    public InlineKeyboardMarkup menu2KeyboardCat() {
        InlineKeyboardButton first = new InlineKeyboardButton("правила знакомства с кошкой").callbackData(MenuCatDescription.CATMEETINGRULES.name());
        InlineKeyboardButton second = new InlineKeyboardButton("список документов").callbackData(MenuCatDescription.CATDOCUMENTS.name());
        InlineKeyboardButton fourth = new InlineKeyboardButton("принять и записать контактные данные").callbackData(MenuDogDescription.WRITECONTACS.name());
        InlineKeyboardButton fifth = new InlineKeyboardButton("позвать волонтера").callbackData(MenuDogDescription.VOLUNTEERCALL.name());
        InlineKeyboardMarkup keyboard2Cat = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{first},
                new InlineKeyboardButton[]{second},
                new InlineKeyboardButton[]{fourth},
                new InlineKeyboardButton[]{fifth});
        return keyboard2Cat;
    }

    /**
     * <i>Клавиатура списка правил для кошек</i>
     *
     * @param chatId идентификатор чата
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     * @see com.pengrad.telegrambot.TelegramBot
     */
    public InlineKeyboardMarkup inlineCatButtonsListOfRules(Long chatId) {
        InlineKeyboardButton first = new InlineKeyboardButton("документы, чтобы взять кошку").callbackData(MenuCatDescription.DOCSTOTAKECAT.name());
        InlineKeyboardButton second = new InlineKeyboardButton("Транспортировка животного").callbackData(MenuCatDescription.CATTRANSPORTATIONRULES.name());
        InlineKeyboardButton third = new InlineKeyboardButton("Обустройство дома для котенка").callbackData(MenuCatDescription.HOMEFORKITTEN.name());
        InlineKeyboardButton fourth = new InlineKeyboardButton("Обустройство дома для взрослой кошки").callbackData(MenuCatDescription.HOMEFORADULTCAT.name());
        InlineKeyboardButton fifth = new InlineKeyboardButton("Для кошек с ограниченными возможностями").callbackData(MenuCatDescription.FORLIMITEDCATS.name());
        InlineKeyboardButton six = new InlineKeyboardButton("причины отказа ").callbackData(MenuCatDescription.CATREFUSE.name());
        InlineKeyboardMarkup recommendationKeyboardCat = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{first},
                new InlineKeyboardButton[]{second},
                new InlineKeyboardButton[]{third},
                new InlineKeyboardButton[]{fourth},
                new InlineKeyboardButton[]{fifth},
                new InlineKeyboardButton[]{six});
        return recommendationKeyboardCat;
    }
}
