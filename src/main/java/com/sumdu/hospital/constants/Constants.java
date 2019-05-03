package com.sumdu.hospital.constants;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String TEXT = "TEXT";
    public static final String DATE = "DATE";
    public static final String INTEGER = "INTEGER";
    public static final String PATIENT_ID = "patientID";
    public static final Map<String, String> FIELD_NAME_RATIO = new HashMap<>();

    static {
        FIELD_NAME_RATIO.put("fullName", "ПІБ");
        FIELD_NAME_RATIO.put("passportID", "Номер документа");
        FIELD_NAME_RATIO.put("dateOfBirth", "Дата народження");
        FIELD_NAME_RATIO.put("address", "Місце проживання");
        FIELD_NAME_RATIO.put("phoneNumber", "Номер телефону");
        FIELD_NAME_RATIO.put("diagnosisMain", "Діагноз основний");
        FIELD_NAME_RATIO.put("workPlace", "Місце роботи, посада");
        FIELD_NAME_RATIO.put("patientID", "№");
        FIELD_NAME_RATIO.put("addressType", "Тип адреса");
    }
}
