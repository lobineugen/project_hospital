package com.sumdu.hospital.constants;

import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String TEXT = "TEXT";
    public static final String DATE = "DATE";
    public static final String INTEGER = "INTEGER";
    public static final String PATIENT_ID = "patientID";
    public static final Map<String, String> FIELD_NAME_RATIO = new HashMap<>();
    public static final String INFORMATION_DIALOG = "Інформаційний діалог";
    public static final String WARNING_DIALOG = "Попереджувальний діалог";
    public static final String REQUIRED_FIELD = "Поле обов'язкове для заповнення!";
    public static final String EMPTY = "";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final SimpleDateFormat MILLISECOND_FORMAT = new SimpleDateFormat("SSS");

    static {
        FIELD_NAME_RATIO.put("fullName", "ПІБ");
        FIELD_NAME_RATIO.put("passportID", "Номер документа");
        FIELD_NAME_RATIO.put("dateOfBirth", "Дата народження");
        FIELD_NAME_RATIO.put("address", "Місце проживання");
        FIELD_NAME_RATIO.put("phoneNumber", "Номер телефону");
        FIELD_NAME_RATIO.put("workPlace", "Місце роботи, посада");
        FIELD_NAME_RATIO.put("patientID", "№");
        FIELD_NAME_RATIO.put("addressType", "Тип адреса");
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
