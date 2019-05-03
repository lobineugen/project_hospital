package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.DAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

import static com.sumdu.hospital.constants.Constants.DATE_TIME_FORMATTER;
import static com.sumdu.hospital.constants.Constants.getStringConverter;

@Component
public class PatientCabinetController {
    private static final Logger LOGGER = Logger.getLogger(PatientCabinetController.class);
    @FXML
    public TextField passportID;
    @FXML
    public DatePicker dateOfBirth;
    @FXML
    public Label age;
    private DAO dao;

    @Autowired
    public PatientCabinetController(DAO dao) {
        this.dao = dao;
    }

    @FXML
    private void initialize() {
        LOGGER.debug("Run initialize method");
        initializeDateElements();
        initializeEventHandlers();
    }


    private void initializeEventHandlers() {
        dateOfBirth.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                LocalDate now = LocalDate.now();
                Period diff = Period.between(newValue, now);
                age.setText(String.valueOf(diff.getYears()));
            }
        });
    }

    private void initializeDateElements() {
        dateOfBirth.setConverter(getStringConverter());
    }

}
