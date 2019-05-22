package com.sumdu.hospital.constants;

import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Constants {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
    public static final SimpleDateFormat MILLISECOND_FORMAT = new SimpleDateFormat("SSS");
    public static final List<String> EXPERT_LIST = new ArrayList<>();
    public static final Map<String, String> EPIDEMIOLOGICAL_HISTORY = new HashMap<>();
    public static final Map<String, String> CLINICAL_DATA = new HashMap<>();
    public static Map<String, Map<String, String>> analyzesTypesToFields = new LinkedHashMap<>();

    static {
        EPIDEMIOLOGICAL_HISTORY.put("1", "Рецепієнт Крові");
        EPIDEMIOLOGICAL_HISTORY.put("2", "Донор Крові");
        EPIDEMIOLOGICAL_HISTORY.put("3", "Татуювання");
        EPIDEMIOLOGICAL_HISTORY.put("4", "Пірсинг");
        EPIDEMIOLOGICAL_HISTORY.put("5", "Манікюр ті інші процедури");
        EPIDEMIOLOGICAL_HISTORY.put("6", "ПІН");
        EPIDEMIOLOGICAL_HISTORY.put("7", "Гемодіаліз");
        EPIDEMIOLOGICAL_HISTORY.put("8", "Мед. працівник");
        EPIDEMIOLOGICAL_HISTORY.put("9", "Оперативне лікування");
        EPIDEMIOLOGICAL_HISTORY.put("10", "Гострий вірусний гепатит");
        EPIDEMIOLOGICAL_HISTORY.put("11", "Контакт з хворими (в т.ч. статевий)");

        CLINICAL_DATA.put("1","Гіркота в роті");
        CLINICAL_DATA.put("2","Тяжість в прявому підребер'ї");
        CLINICAL_DATA.put("3","Біль в прявому підребер'ї");
        CLINICAL_DATA.put("4","Іктеричність склер");
        CLINICAL_DATA.put("5","Жовтяничність шкіри");
        CLINICAL_DATA.put("6","Артрапгії");
        CLINICAL_DATA.put("7","Міалгії");
        CLINICAL_DATA.put("8","Висип");
        CLINICAL_DATA.put("9","Нудота");
        CLINICAL_DATA.put("10","Блювання");
        CLINICAL_DATA.put("11","Біль в епігастрії");
        CLINICAL_DATA.put("12","Інші локалізації болю в животі");
        CLINICAL_DATA.put("13","Кровоточивість ясен");
        CLINICAL_DATA.put("14","Інші кровотечі");
        CLINICAL_DATA.put("15","Набряки н/к");
        CLINICAL_DATA.put("16","Гепатомегапія");
        CLINICAL_DATA.put("17","Загальна слабкість");
        CLINICAL_DATA.put("18","Зниження продуктивності");
        CLINICAL_DATA.put("19","Зниження апетиту");

        EXPERT_LIST.add("Кардіолог");
        EXPERT_LIST.add("Невропатолог");
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
        FIELD_NAME_RATIO.put("pvtStart", "Початок ПВТ");
        FIELD_NAME_RATIO.put("pvtEnd", "Закінчення ПВТ");
        FIELD_NAME_RATIO.put("repeatPvtStart", "Початок повторної ПВТ");
        FIELD_NAME_RATIO.put("repeatPvtEnd", "Закінчення повторної ПВТ");
        FIELD_NAME_RATIO.put("ogkSurvey", "Обстеження ОГК");
        FIELD_NAME_RATIO.put("allergicReactions", "Алергічні реакції");

        String[] clinicBloodArray = {"Лейкоцити", "Еритроцити", "Гемоглобін", "Гематокрит",
                "Середній об'єм еритроцитів", "Середній вміст гемоглобіну",
                "Середня концентрація гемоглобіну", "Тромбоцити", "Зміна розмірів тромбоцитів",
                "Мієлоцити", "Метамієлоцити", "Палочкоядерні", "Сегментоядерні", "Еозинофіли",
                "Базофіли", "Лімфоцити", "Моноцити", "Плазматичні клітини", "ШОЕ", "ПТІ",
                "Час згортання: почат.", "Час згортання: закінч.", "Тривалість кровотечі"
        };
        analyzesTypesToFields.put("clinicBlood", convertArrayToMap(clinicBloodArray));

        String[] biochemistryBloodArray = {"Загальний білок", "Альбумін", "Креатинін", "Сечовина", "Сечова кислота",
                "Білірубін загальний", "АЛАТ(аланін-амінотрансфераза)", "АСАТ(аспаргат-амінотрансфераза)", "Лужна фостафаза",
                "y-ГТТ глутамінтрансфераза"
        };
        analyzesTypesToFields.put("biochemistryBlood", convertArrayToMap(biochemistryBloodArray));

        String[] coagulogramBlood = {"Фібриноген", "Тробмотест"};
        analyzesTypesToFields.put("coagulogramBlood", convertArrayToMap(coagulogramBlood));

        String[] diastaseUrine = {"\u03B1-аміпаза"};
        analyzesTypesToFields.put("diastaseUrine", convertArrayToMap(diastaseUrine));

        String[] glucoseUrine = {"Глюкоза сечі"};
        analyzesTypesToFields.put("glucoseUrine", convertArrayToMap(glucoseUrine));

        String[] feces = {"Аналіз калу на я/г + опісторхоз"};
        analyzesTypesToFields.put("feces", convertArrayToMap(feces));

    }

    public static Map<String, String> convertArrayToMap(String[] array) {
        Map<String , String> map = new TreeMap<>();
        for (int i = 0; i < array.length; i++) {
            map.put(String.valueOf(i), array[i]);
        }
        return map;
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
