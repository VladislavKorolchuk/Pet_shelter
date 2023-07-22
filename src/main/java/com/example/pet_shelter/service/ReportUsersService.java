package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.DogNullParameterValueException;
import com.example.pet_shelter.listener.TelegramBotUpdatesListener;
import com.example.pet_shelter.model.BinaryContentFile;
import com.example.pet_shelter.model.ReportUsers;
import com.example.pet_shelter.repository.BinaryContentFileRepository;
import com.example.pet_shelter.repository.ReportUsersRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

@Service
public class ReportUsersService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private TelegramBot telegramBot;
    private BinaryContentFileRepository binaryContentFileRepository;
    private ReportUsersRepository reportUsersRepository;

    public ReportUsersService(TelegramBot telegramBot, BinaryContentFileRepository binaryContentFileRepository, ReportUsersRepository reportUsersRepository) {
        this.telegramBot = telegramBot;
        this.binaryContentFileRepository = binaryContentFileRepository;
        this.reportUsersRepository = reportUsersRepository;
    }

    /**
     * <i>Загрузка файла из чата в директорию и сохранение полного отчета фото+описание в бд</i>
     * Использует метод создания файла {@link ReportUsersService#createBinaryFile(byte[], Update, Long, String, File)}
     *
     * @param update апдейт
     * @throws IOException
     */
    public void uploadReportUser(Update update) throws IOException {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        Long chatId = update.message().chat().id();
        String file_id = update.message().photo()[update.message().photo().length - 1].fileId();
        URL url = new URL("https://api.telegram.org/bot" + telegramBot.getToken() + "/getFile?file_id=" + file_id);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String getFileResponse = br.readLine();
        JSONObject jresult = new JSONObject(getFileResponse);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        File localFile = new File("./src/main/resources/" + file_path);
        InputStream is = new URL("https://api.telegram.org/file/bot" + telegramBot.getToken() + "/" + file_path).openStream();
        byte[] b = is.readAllBytes();
        copyInputStreamToFile(is, localFile);
        br.close();
        is.close();
        createBinaryFile(b, update, chatId, file_id, localFile);
    }

    /**
     * Метод сохранения изображения и описания в бд
     * <br> Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param bytes     массив байтов изображения из апи
     * @param update    апдейт
     * @param chatId    Идентификатор чата
     * @param file_id   Идентификатор изображения
     * @param localFile Директория локального фото
     * @see com.pengrad.telegrambot.request.SendMessage
     */
    public void createBinaryFile(byte[] bytes, Update update, Long chatId, String file_id, File localFile) {
        BinaryContentFile binaryContentFile = new BinaryContentFile();
        binaryContentFile.setFileBytes(bytes);
        binaryContentFileRepository.save(binaryContentFile);
        ReportUsers reportUsers = new ReportUsers();
        reportUsers.setBinaryContentFile(binaryContentFile);
        reportUsers.setFilePath(localFile.getPath());
        reportUsers.setTelegramFileId(file_id);
        reportUsers.setFileSize(bytes.length);
        reportUsers.setTime(LocalDate.now());
        reportUsers.setChatId(update.message().chat().id());
        if (update.message().caption() == null) {
            telegramBot.execute(new SendMessage(chatId, "Вы забыли описать состояние питомца!"));
        }
        reportUsers.setCommentsUser(update.message().caption());
        reportUsersRepository.save(reportUsers);
    }

    /**
     * <b>Поиск отчета по его id идентификатору</b>
     * <br> Используется метод репозитория {@link JpaRepository#findById(Object)}
     *
     * @param id идентификатор отчета
     * @return Возвращает найденый отчет
     */
    public ReportUsers findReportUsers(Long id) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        return reportUsersRepository.findById(id).orElseThrow();
    }


    public ReportUsers updateReport(Long id, ReportUsers reportUsers) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        ReportUsers updateReportUsers = reportUsersRepository.findById(id).orElse(null);
        if (updateReportUsers != null) {
            updateReportUsers.setCommentsUser(reportUsers.getCommentsUser());
            updateReportUsers.setTime(reportUsers.getTime());
            updateReportUsers.setFilePath(reportUsers.getFilePath());
            updateReportUsers.setChatId(reportUsers.getChatId());
            updateReportUsers.setFileSize(reportUsers.getFileSize());
            updateReportUsers.setBinaryContentFile(reportUsers.getBinaryContentFile());

        } else {
            throw new DogNullParameterValueException("Недостаточно данных при попытке заменить данные у объекта Report");
        }
        return reportUsersRepository.save(updateReportUsers);
    }

}