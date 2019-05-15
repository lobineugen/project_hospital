package com.sumdu.hospital.constants;

import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final String TEXT = "TEXT";
    public static final String DATE = "DATE";
    public static final String INTEGER = "INTEGER";
    public static final String PATIENT_ID = "patientID";
    public static final String CARD_ID = "cardID";
    public static final Map<String, String> FIELD_NAME_RATIO = new HashMap<>();
    public static final String INFORMATION_DIALOG = "Інформаційний діалог";
    public static final String WARNING_DIALOG = "Попереджувальний діалог";
    public static final String REQUIRED_FIELD = "Поле обов'язкове для заповнення!";
    public static final String EMPTY = "";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final SimpleDateFormat MILLISECOND_FORMAT = new SimpleDateFormat("SSS");
    public static final List<String> EXPERT_LIST = new ArrayList<>();

    static {
        EXPERT_LIST.add("Кардіолог");
        EXPERT_LIST.add("Невропатоло");
        EXPERT_LIST.add("Гастроентеролог");
        FIELD_NAME_RATIO.put("fullName", "ПІБ");
        FIELD_NAME_RATIO.put("passportID", "Номер документа");
        FIELD_NAME_RATIO.put("dateOfBirth", "Дата народження");
        FIELD_NAME_RATIO.put("address", "Місце проживання");
        FIELD_NAME_RATIO.put("phoneNumber", "Номер телефону");
        FIELD_NAME_RATIO.put("workPlace", "Місце роботи, посада");
        FIELD_NAME_RATIO.put("patientID", "№");
        FIELD_NAME_RATIO.put("addressType", "Тип адреса");
        FIELD_NAME_RATIO.put("cardNumber", "Номер карти");
        FIELD_NAME_RATIO.put("week", "Тиждень лікування");
        FIELD_NAME_RATIO.put("dateIn", "Дата госпіталізації");
        FIELD_NAME_RATIO.put("dateOut", "Дата виписки");
        FIELD_NAME_RATIO.put("mainDiagnosis", "Діагноз основний");
        FIELD_NAME_RATIO.put("complication", "Ускладнення основного діагнозу");
        FIELD_NAME_RATIO.put("pvt", "Ускладнення ПВТ");
        FIELD_NAME_RATIO.put("concomitant", "Діагноз супутній");
        FIELD_NAME_RATIO.put("date", "Дата");
        FIELD_NAME_RATIO.put("doctor", "Спеціаліст");
        FIELD_NAME_RATIO.put("conclusion", "Заключення");

    }

    public static StringConverter<LocalDate> getStringConverter() {
        return new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object == null)
                    return "";
                return DATE_TIME_FORMATTER.format(object);
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(string, DATE_TIME_FORMATTER);
            }
        };
    }
}
