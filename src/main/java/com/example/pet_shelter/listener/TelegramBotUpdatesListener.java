package com.example.pet_shelter.listener;

import com.example.pet_shelter.MenuMaker.MenuMakerCat;
import com.example.pet_shelter.MenuMaker.MenuMakerDog;
import com.example.pet_shelter.configuration.MenuCatDescription;
import com.example.pet_shelter.configuration.MenuDogDescription;
import com.example.pet_shelter.exceptions.UsersNullParameterValueException;
import com.example.pet_shelter.model.ReportUsers;
import com.example.pet_shelter.model.Shelters;
import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.repository.BinaryContentFileRepository;
import com.example.pet_shelter.repository.ReportUsersRepository;
import com.example.pet_shelter.service.ReportUsersService;
import com.example.pet_shelter.service.ShelterService;
import com.example.pet_shelter.service.UsersService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private String startLine;
    private Long flagChoosingShelter; // Флаг выбора приюта
    private final ReportUsersRepository reportUsersRepository;
    private final BinaryContentFileRepository binaryContentFileRepository;
    private final UsersService usersService;
    private final MenuMakerDog menuMakerDog;
    private final MenuMakerCat menuMakerCat;
    private final ReportUsersService reportUsersService;
    private final ShelterService shelterService;

    public TelegramBotUpdatesListener(UsersService usersService, MenuMakerDog menuMakerDog, ReportUsersService reportUsersService,
                                      BinaryContentFileRepository binaryContentFileRepository,
                                      ReportUsersRepository reportUsersRepository, MenuMakerCat menuMakerCat, ShelterService shelterService) {
        this.usersService = usersService;
        this.menuMakerDog = menuMakerDog;
        this.reportUsersService = reportUsersService;
        this.binaryContentFileRepository = binaryContentFileRepository;
        this.reportUsersRepository = reportUsersRepository;
        this.menuMakerCat = menuMakerCat;
        this.shelterService = shelterService;
    }

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private boolean isWaitingUserData; //ожидаем сообщение с данными пользователя после нажатия кнопки "записать данные пользователя"
    private boolean isPhoto;           //ожидаем загрузку фото

    @Value("${dogSafetyFile}")
    String DOGSHELTER_SAFETY_FILE;
    @Value("${cynologistAdvice}")
    String CYNOLOGIST_ADVICE;

    @Value("${dogMeetingRules}")
    String DOG_MEETING_RULES;

    @Value("${docsForDog}")
    String DOCS_FOR_DOG;

    @Value("${dogsTransportation}")
    String DOGS_TRANSPORTATION;

    @Value("${homeForPuppy}")
    String HOME_FOR_PUPPY;

    @Value("${homeForAdultDog}")
    String HOME_FOR_ADULT_DOG;

    @Value("${forLimitedDogs}")
    String FOR_LIMITED_DOGS;

    @Value("${refuse}")
    String REFUSE;

    // значения для кошек
    @Value("${safetyRulesForCat}")
    String SAFETY_RULES_CATS;

    @Value("${catMeetingRules}")
    String CAT_MEETING_RULES;

    @Value("${docsToTakeCat}")
    String DOCS_TO_TAKE_CAT;

    @Value("${catTransportation}")
    String CAT_TRANSPORTATION;

    @Value("${homeForKitten}")
    String HOME_FOR_KITTEN;

    @Value("${homeForAdultCat}")
    String HOME_FOR_ADULT_CAT;

    @Value("${forLimitedCats}")
    String FOR_LIMITED_CATS;

    @Value("${catRefuse}")
    String CAT_REFUSE;

    @Value("${dogShelterNurseryLocation}")
    String DOG_SHELTER_NURSERY_LOCATION;

    @Value("${catShelterNurseryLocation}")
    String CAT_SHELTER_NURSERY_LOCATION;

    int NUMBER_CHARACTERS = 2048; // Max символов считываемое из файла

    @Autowired
    private TelegramBot telegramBot;

    /**
     * <i>Инициализация бота<i/> <br>
     * Инициализация меню {@code menuButton()}
     */
    @PostConstruct
    public void init() {
        menuButton();
        telegramBot.setUpdatesListener(this);
    }

    /**
     * <i>Обработка возможных апдейтов<i/>
     *
     * @param updates возможные апдейты
     * @return
     */
    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .forEach(update -> {
                    logger.info("Processing update: {}", update);
                    if (update.message() != null && update.message().text() != null || update.message() != null && update.message().photo() != null) {
                        Long chatId = update.message().chat().id();
                        processUpdate(chatId, update);

                    }
                    if (update.callbackQuery() != null) {
                        Long chatId = update.callbackQuery().message().chat().id();
                        startMenuDogs(chatId, update);
                        callBackUpdateMenu1(chatId, update);
                        callBackUpdateMenu2(chatId, update);
                        callBackDataListOfRecommendations(chatId, update);
                        startMenuCats(chatId, update);
                        menu1KeyboardCats(chatId, update);
                        menu2KeyboardCats(chatId, update);
                        listOfDocumentsForCats(chatId, update);
                    }
                });
        logger.info("Processing update: {}", updates);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * <i>Обработка колбэков первого меню</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void callBackUpdateMenu1(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();

            if (data.equals(MenuDogDescription.WRITECONTACS.name())) {
                isWaitingUserData = true;
                telegramBot.execute(new SendMessage(chatId,
                        "Введите данные пользователя в формате \"Имя Фамилия Телефон Почта (через пробел)\""));
            } else if (data.equals(MenuDogDescription.AboutPetShelterDocx.name())) {
                flagChoosingShelter = 1L;
                aboutTheShelter(chatId);
            } else if (data.equals(MenuDogDescription.SCHEDULE.name())) {
                sendLocationPhoto(chatId);
            } else if (data.equals(MenuDogDescription.VOLUNTEERCALL.name())) {
                telegramBot.execute(new SendMessage(chatId, "Волонтер позван"));
            }
            if (data.equals(MenuDogDescription.SAFETYRULES.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, DOGSHELTER_SAFETY_FILE);
            }
        }
    }

    /**
     * <i>Обработка колбэков 2-го меню</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void callBackUpdateMenu2(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals(MenuDogDescription.MENULISTOFDOCUMENTS.name())) {
                telegramBot.execute(new SendMessage(chatId, "Список документов").replyMarkup(menuMakerDog.inlineButtonsListOfRules(chatId)));
            } else if (data.equals(MenuDogDescription.CYNOLOGISTADVICE.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, CYNOLOGIST_ADVICE);
                telegramBot.execute(new SendMessage(chatId, "Ознакомьтесь с правилам обращения с собакой или выберите кинолога:").replyMarkup(menuMakerDog.buttonCynologist(chatId)));
            } else if (data.equals(MenuDogDescription.WRITECONTACS.name())) {
                isWaitingUserData = true;
            } else if (data.equals(MenuDogDescription.DOGMEETINGRULES.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, DOG_MEETING_RULES);
            }
        }
    }

    /**
     * <i>Обработка колбэков списка рекомендаций собак</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void callBackDataListOfRecommendations(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals(MenuDogDescription.DOCSTOTAKEDOG.name())) {
                sendPetsDocuments(chatId);
            } else if (data.equals(MenuDogDescription.DOGTRASPORTATION.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, DOGS_TRANSPORTATION);
            } else if (data.equals(MenuDogDescription.HOMEFORPUPPEY.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, HOME_FOR_PUPPY);
            } else if (data.equals(MenuDogDescription.HOMEFORADULTDOG.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, HOME_FOR_ADULT_DOG);
            } else if (data.equals(MenuDogDescription.LIMITEDDOG.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, FOR_LIMITED_DOGS);
            } else if (data.equals(MenuDogDescription.REFUSE.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, REFUSE);
            }
        }
    }
    /**
     * <i>Обработка колбэков стартового меню собак</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void startMenuDogs(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals(MenuDogDescription.DOGSHELTERENTER.name())) {
                telegramBot.execute(new SendMessage(chatId, startLine + ", Вы выбрали собачий приют").replyMarkup(menuMakerDog.afterStartDogKeyBoard()));
            } else if (data.equals(MenuDogDescription.AboutPetShelter.name())) {
                telegramBot.execute(new SendMessage(chatId, startLine + ", Вы выбрали пункт выбрать информацию о приюте").replyMarkup(menuMakerDog.menu1Keyboard()));
            } else if (data.equals(MenuDogDescription.HOWTOTAKEDOG.name())) {
                telegramBot.execute(new SendMessage(chatId, startLine + ", Вы выбрали пункт как взять информацию о питомце").replyMarkup(menuMakerDog.menu2Keyboard()));
            } else if (data.equals(MenuDogDescription.SENDDOGPHOTO.name())) {
                telegramBot.execute(new SendMessage(chatId, "Загрузите отчет").replyMarkup(menuMakerDog.menu3Keyboard()));
                isPhoto = true;
            }
        }
    }
    /**
     * <i>Обработка колбэков стартового меню кошек</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void startMenuCats(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals(MenuDogDescription.CATSHELTERENTER.name())) {
                telegramBot.execute(new SendMessage(chatId, startLine + ", Вы выбрали кошачий приют").replyMarkup(menuMakerCat.afterStartCatKeyBoard()));
            } else if (data.equals(MenuCatDescription.AboutCatPetShelter.name())) {
                telegramBot.execute(new SendMessage(chatId, startLine + ", Вы выбрали раздел о приюте").replyMarkup(menuMakerCat.menu1KeyboardCat()));
            } else if (data.equals(MenuCatDescription.CatVolunteer.name())) {
                telegramBot.execute(new SendMessage(chatId, "Волонтер позван"));
            } else if (data.equals(MenuCatDescription.HOWTOTAKECAT.name())) {
                telegramBot.execute(new SendMessage(chatId, startLine + ", Вы выбрали раздел как взять животное").replyMarkup(menuMakerCat.menu2KeyboardCat()));
            } else if (data.equals(MenuCatDescription.SENDPETREPORT.name())) {
                telegramBot.execute(new SendMessage(chatId, "Загрузите отчет").replyMarkup(menuMakerDog.menu3Keyboard()));
                isPhoto = true;
            }
        }
    }
    /**
     * <i>Обработка колбэков первого меню кошек</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void menu1KeyboardCats(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals(MenuCatDescription.AboutCatPetShelterDocx.name())) {
                flagChoosingShelter = 2L;
                aboutTheShelter(chatId);
            } else if (data.equals(MenuCatDescription.CATNURSERYLOCATION.name())) {
                sendCatNurseryLocationPhoto(chatId);
            } else if (data.equals(MenuCatDescription.WRITECONTACTSCAT.name())) {
                isWaitingUserData = true;
                telegramBot.execute(new SendMessage(chatId,
                        "Введите данные пользователя в формате \"Имя Фамилия Телефон Почта (через пробел)\""));
            }
        }
    }
    /**
     * <i>Обработка колбэков второго меню кошек</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void menu2KeyboardCats(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals(MenuCatDescription.CATDOCUMENTS.name())) {
                telegramBot.execute(new SendMessage(chatId, startLine + ", Вы выбрали списки документов:").replyMarkup(menuMakerCat.inlineCatButtonsListOfRules(chatId)));
            } else if (data.equals(MenuCatDescription.CATMEETINGRULES.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, CAT_MEETING_RULES);
            }
        }
    }
    /**
     * <i>Обработка колбэков списка рекомендаций кошек</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void listOfDocumentsForCats(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals(MenuCatDescription.DOCSTOTAKECAT.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, DOCS_TO_TAKE_CAT);
            } else if (data.equals(MenuCatDescription.CATTRANSPORTATIONRULES.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, CAT_TRANSPORTATION);
            } else if (data.equals(MenuCatDescription.HOMEFORKITTEN.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, HOME_FOR_KITTEN);
            } else if (data.equals(MenuCatDescription.HOMEFORADULTCAT.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, HOME_FOR_ADULT_CAT);
            } else if (data.equals(MenuCatDescription.FORLIMITEDCATS.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, FOR_LIMITED_CATS);
            } else if (data.equals(MenuCatDescription.CATREFUSE.name())) {
                fileRead(chatId, NUMBER_CHARACTERS, CAT_REFUSE);
            }
        }
    }

    /**
     * <i>Ловим апдейты</i>
     * Структура кнопок бота и откликов по ним
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.request.SendMessage
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     * @see com.pengrad.telegrambot.TelegramBot
     * @see com.pengrad.telegrambot.model.Update
     */
    private void processUpdate(Long chatId, Update update) {
        String userMessage = update.message().text();
        if (isWaitingUserData) {
            try {
                createUser(chatId, userMessage);
                telegramBot.execute(new SendMessage(chatId, "Данные успешно записаны"));
                isWaitingUserData = false;
            } catch (UsersNullParameterValueException e) {
                telegramBot.execute(new SendMessage(chatId, "Не удается распознать данные: " + e.getMessage() + ", попробуйте еще раз."));
            }
        } else if (isPhoto) {
            try {
                reportUsersService.uploadReportUser(update); // Сохранение отчета User
                telegramBot.execute(new SendMessage(chatId, "Изображение успешно сохранено"));
                isPhoto = false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            switch (userMessage) {
                case "/start":
                    startLine=greetings(chatId,update);
                    telegramBot.execute(new SendMessage(chatId, startLine + ", Выберите приют").replyMarkup(menuMakerDog.startMenuKeyboard()));
                    break;
                default:
                    telegramBot.execute(new SendMessage(chatId, startLine + ", Вы ввели что-то странное!"));
            }
        }
    }

    /**
     * <i>Приветственное сообщение пользователя в чате</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.request.SendMessage
     */
    public String greetings(Long chatId, Update update) {
        String firstName = update.message().chat().firstName();
        String lastName = update.message().chat().lastName();
        return firstName + " " + lastName;
    }

    /**
     * <i>Кнопка меню бота</i>
     *
     * @see com.pengrad.telegrambot.model.BotCommand
     * @see com.pengrad.telegrambot.request.SetMyCommands
     */
    public void menuButton() {
        SetMyCommands message = new SetMyCommands(
                new BotCommand("/start", "Запустить бота"));
        telegramBot.execute(message);
    }

    /**
     * <i> Вывод информации о приюте </i> <br>
     *
     * @param chatId идентификатор чата
     *               flagChoosingShelter - флаг выбранного приюта
     */
    private void aboutTheShelter(long chatId) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);

        Shelters shelters = new Shelters();
        shelters = shelterService.getShelter(flagChoosingShelter);
        telegramBot.execute(new SendMessage(chatId, shelters.getDescriptionShelter()));
    }

    /**Приют для собак<br>
     * <i>Высылает в чат сообщение с текстом, сообщение с изображением карты, навигацию по карте</i> <br>
     *
     * @param chatId идентификатор чата
     * @see com.pengrad.telegrambot.request.SendMessage
     * @see com.pengrad.telegrambot.request.SendLocation
     * @see com.pengrad.telegrambot.request.SendPhoto
     */
    public void sendLocationPhoto(Long chatId) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        float latitude = (float) 59.82225018231752;
        float longitude = (float) 30.178212453672643;
        telegramBot.execute(new SendMessage(chatId, "Мы работаем с 08:00 до 23:00"));
        File file = new File(DOG_SHELTER_NURSERY_LOCATION);
        telegramBot.execute(new SendPhoto(chatId, file));
        telegramBot.execute(new SendMessage(chatId, "Или используйте навигатор"));
        SendLocation location = new SendLocation(chatId, latitude, longitude);
        telegramBot.execute(location);
    }

    /** Приют для кошек<br>
     * <i>Высылает в чат сообщение с текстом, сообщение с изображением карты, навигацию по карте</i> <br>
     * @param chatId идентификатор чата
     */
    public void sendCatNurseryLocationPhoto(Long chatId) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        float latitude = (float) 60.01192431352728;
        float longitude = (float) 29.721648774315117;
        telegramBot.execute(new SendMessage(chatId, "Мы работаем с 08:00 до 22:00"));
        File file = new File(CAT_SHELTER_NURSERY_LOCATION);
        telegramBot.execute(new SendPhoto(chatId, file));
        telegramBot.execute(new SendMessage(chatId, "Или используйте навигатор"));
        SendLocation location = new SendLocation(chatId, latitude, longitude);
        telegramBot.execute(location);
    }

    /**
     * <i>Высылает в чат документ</i>
     * <br>
     * Использует java.io метод {@link File#File(URI)}  File} <br>
     * Использует pengrad метод {@link com.pengrad.telegrambot.request.SendMessage } <br>
     * Использует pengrad метод {@link com.pengrad.telegrambot.request.SendDocument } <br>
     *
     * @param chatId идентификатор чата
     */
    public void sendPetsDocuments(Long chatId) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        telegramBot.execute(new SendMessage(chatId, "Файл со списком нужных документов"));
        File file = new File(DOCS_FOR_DOG);
        telegramBot.execute(new SendDocument(chatId, file));
    }

    /**
     * <i>Добавление нового пользователя, полученного через Telegram Bot</i>
     *
     * @param chatId идентификатор чата
     * @param str    строка с данными пользователя в формате "Имя, Фамилия, Телефон, Почта"
     * @see UsersService
     */
    public void createUser(Long chatId, String str) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        //  logger.info("Метод" + Object.class.getEnclosingMethod());
        String[] strDivided = str.split("\\s*(\\s|,|!|\\.)\\s*"); // Разбивка строки данных пользователя
        Users user = new Users();
        user.setFirstName(strDivided[0]); // Если нет данных вылезает исключение ArrayIndexOutOfBoundsException
        user.setLastName(strDivided[1]);
        user.setUserPhoneNumber(strDivided[2]);
        user.setUserEmail(strDivided[3]);
        usersService.createUserInDb(user);
    }

    /**
     * Фоновое приложение для поиска User с не сданными отчетами
     */
    @Scheduled(fixedDelay = 1_000L * 60 * 60 * 24) // Раз в сутки
    public void run() {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);

        LocalDate date = LocalDate.now().plusMonths(1);  // К текущей дате прибавляем время

        List<ReportUsers> reportUsersList = new ArrayList<>();
        reportUsersList = reportUsersRepository.getAllBy();

        for (int i = 0; i < reportUsersList.size(); i++) {
            LocalDate date1 = reportUsersList.get(i).getTime();
            Period period = Period.between(date, date1); // Вычисление промежутка между датами
            long chatId = reportUsersList.get(i).getChatId();
            if (period.getDays() == 1) {
                String str = "Уважаемый владелец! Спасибо за вовремя сданный отчет!";
                SendResponse response = telegramBot.execute(new SendMessage(chatId, str));
            } else if (period.getDays() > 31 && period.getDays() < 46) {
                String str = "Уважаемый владелец! У вас идет испытательный срок!";
                SendResponse response = telegramBot.execute(new SendMessage(chatId, str));
            } else if (period.getDays() > 46) {
                String str = "Уважаемый владелец! У вас идет прошел испытательный срок! Необходимо вернуть питомца.";
                SendResponse response = telegramBot.execute(new SendMessage(chatId, str));
            } else if (date.equals(date1) || period.getDays() > 31) {
                String str = "Уважаемый владелец! пришлите отчет о питомце. Спасибо!";
                SendResponse response = telegramBot.execute(new SendMessage(chatId, str));
            }
        }
    }

    /**<i>Чтение текстового файла и вывод в чат его содержимое</i>
     *
     * @param chatId идентификатор чата
     * @param bytes макс длина
     * @param nameOfFile имя файла
     */

    private void fileRead(long chatId, int bytes, String nameOfFile) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        char[] buf = new char[bytes];
        try (FileReader reader = new FileReader(nameOfFile)) {
            reader.read(buf);
        } catch (IOException ex) {
        }
        telegramBot.execute(new SendMessage(chatId, new String(buf)));
    }

}
