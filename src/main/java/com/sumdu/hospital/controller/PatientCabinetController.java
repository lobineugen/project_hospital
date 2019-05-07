package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.model.Patient;
import com.sumdu.hospital.service.Helper;
import com.sumdu.hospital.service.ShowDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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
    @FXML
    public Button save;
    @FXML
    public DatePicker pvtStart;
    @FXML
    public DatePicker repeatPvtStart;
    @FXML
    public DatePicker pvtEnd;
    @FXML
    public DatePicker repeatPvtEnd;
    @FXML
    public TextField fullName;
    @FXML
    public RadioButton cityRadio;
    @FXML
    public RadioButton villageRadio;
    @FXML
    public TextField address;
    @FXML
    public TextField phoneNumber;
    @FXML
    public ToggleGroup addressType;
    @FXML
    public TextField workPlace;
    @FXML
    public TextField patientID;
    @FXML
    public AnchorPane patientCabinet;
    @FXML
    public Button addMedicalCardToStationaryPatient;
    @FXML
    public HBox medicalCardsForStationaryPatientsContainer;
    private DAO dao;
    private Helper helper;
    private ShowDialog showDialog;
    private ApplicationContext context;
    private Patient currentPatient;

    public PatientCabinetController(DAO dao, Helper helper, ShowDialog showDialog) {
        this.dao = dao;
        this.showDialog = showDialog;
        this.helper = helper;
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
                if (newValue == null) {
                    return;
                }
                Period diff = Period.between(newValue, now);
                age.setText(String.valueOf(diff.getYears()));
            }
        });
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int id;
                boolean create = false;
                if (!helper.isNotEmpty(fullName)
                        || !helper.isNotEmpty(passportID)
                        || !helper.isNotEmpty(dateOfBirth)) {
                    return;
                }
                if (patientID.getText().isEmpty()) {
                    id = helper.getUniqueID();
                    create = true;
                } else {
                    id = Integer.parseInt(patientID.getText());
                }

                currentPatient = new Patient(id,
                        fullName.getText(),
                        passportID.getText());
                if (dateOfBirth.getValue() != null) {
                    currentPatient.setDateOfBirth(Date.valueOf(dateOfBirth.getValue()));
                }
                if (!address.getText().isEmpty()) {
                    currentPatient.setAddress(address.getText());
                }
                if (!workPlace.getText().isEmpty()) {
                    currentPatient.setWorkPlace(workPlace.getText());
                }
                if (!phoneNumber.getText().isEmpty()) {
                    currentPatient.setPhoneNumber(phoneNumber.getText());
                }
                currentPatient.setAddressType(((RadioButton) addressType.getSelectedToggle()).getText());
                if (create) {
                    dao.createPatient(currentPatient);
                    patientID.setText(String.valueOf(currentPatient.getPatientID()));
                    showDialog.showInformationDialog("Запис про пацієнта успішно створена", patientCabinet);
                } else {
                    dao.updatePatient(currentPatient);
                    showDialog.showInformationDialog("Запис про пацієнта " + currentPatient.getFullName() + " успішно оновлений!", patientCabinet);
                }
                System.out.println("id current user: " + patientID.getText());
            }
        });
        addMedicalCardToStationaryPatient.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!patientID.getText().isEmpty()) {
                    Optional<Card> optional = showDialog.createCard(patientCabinet);
                    if (optional.isPresent()) {
                        Card card = optional.get();
                        card.setPatientID(Integer.parseInt(patientID.getText()));
                        Button newCard = new Button(String.format("№ = %s\n Тиждень лікування = %s\n Дата виписки = %tF\n Дата госпіталізації = %tF",
                                card.getCardNumber(), card.getWeek(), card.getDateOut(), card.getDateIn()));
                        dao.createCard(card);
                        newCard.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                MainController mainController = context.getBean(MainController.class);
                                try {
                                    mainController.content.setContent(FXMLLoader.load(getClass().getResource("/fxml/medicalCard.fxml")));
                                } catch (IOException e) {
                                    LOGGER.error("IOException:", e);
                                }
                                Button button = new Button("Кабиент пациент >");
                                mainController.addBreadCrumb(button, patientCabinet, 1);
                            }
                        });
                        newCard.setPrefHeight(91);
                        newCard.setMinWidth(100);
                        medicalCardsForStationaryPatientsContainer.getChildren().add(newCard);
                        currentPatient.addCard(card);
                    }
                }
            }
        });
    }

    private void initializeDateElements() {
        StringConverter<LocalDate> stringConverter = getStringConverter();
        dateOfBirth.setConverter(stringConverter);
        pvtEnd.setConverter(stringConverter);
        pvtStart.setConverter(stringConverter);
        repeatPvtEnd.setConverter(stringConverter);
        repeatPvtStart.setConverter(stringConverter);
    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    public void removePatient() {
        currentPatient = null;
        patientID.setText("");
        fullName.setText("");
        passportID.setText("");
        dateOfBirth.setValue(null);
        phoneNumber.setText("");
        workPlace.setText("");
        age.setText("");
        address.setText("");
    }

    public void setPatient(Patient patient) {
        currentPatient = patient;
        patientID.setText(String.valueOf(patient.getPatientID()));
        fullName.setText(patient.getFullName());
        passportID.setText(patient.getPassportID());
        dateOfBirth.setValue(patient.getDateOfBirth().toLocalDate());
        phoneNumber.setText(patient.getPhoneNumber());
        workPlace.setText(patient.getWorkPlace());
        address.setText(patient.getAddress());
        for (Toggle toggle : addressType.getToggles()) {
            if (((RadioButton) toggle).getText().equals(patient.getAddressType())) {
                toggle.setSelected(true);
            }
        }
    }

}
