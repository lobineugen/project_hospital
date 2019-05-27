package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.model.Patient;
import com.sumdu.hospital.service.Helper;
import com.sumdu.hospital.service.ShowDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import static com.sumdu.hospital.constants.Constants.*;

@Controller
public class PatientCabinetController {
    private static final Logger LOGGER = Logger.getLogger(PatientCabinetController.class);
    @FXML
    public JFXTextField passportID;
    @FXML
    public JFXDatePicker dateOfBirth;
    @FXML
    public Label age;
    @FXML
    public JFXButton save;
    @FXML
    public JFXDatePicker pvtStart;
    @FXML
    public JFXDatePicker repeatPvtStart;
    @FXML
    public JFXDatePicker pvtEnd;
    @FXML
    public JFXDatePicker repeatPvtEnd;
    @FXML
    public JFXTextField fullName;
    @FXML
    public RadioButton cityRadio;
    @FXML
    public RadioButton villageRadio;
    @FXML
    public JFXTextField address;
    @FXML
    public JFXTextField phoneNumber;
    @FXML
    public ToggleGroup addressType;
    @FXML
    public JFXTextField workPlace;
    @FXML
    public TextField patientID;
    @FXML
    public AnchorPane patientCabinet;
    @FXML
    public JFXButton addMedicalCardToStationaryPatient;
    @FXML
    public HBox medicalCardsForStationaryPatientsContainer;
    @FXML
    public JFXTextArea mainDiagnosis;
    @FXML
    public JFXTextArea complication;
    @FXML
    public JFXTextArea pvt;
    @FXML
    public JFXTextArea concomitant;
    @FXML
    public JFXTextField allergicReactions;
    @FXML
    public JFXTextField ogkSurvey;
    public JFXButton medicalCardOfAmbulatoryPatient;
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
                if (patientID.getText().isEmpty()) {
                    id = helper.getUniqueID();
                    create = true;
                    currentPatient = new Patient(id,
                            fullName.getText(),
                            passportID.getText());

                } else {
                    currentPatient.setFullName(fullName.getText());
                    currentPatient.setPassportID(passportID.getText());
                    if (currentPatient.getLastCard() != null) {
                        currentPatient.getLastCard().setMainDiagnosis(mainDiagnosis.getText());
                        currentPatient.getLastCard().setPvt(pvt.getText());
                        currentPatient.getLastCard().setComplication(complication.getText());
                        currentPatient.getLastCard().setConcomitant(concomitant.getText());
                    }

                }
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
                if (pvtStart.getValue() != null) {
                    currentPatient.setPvtStart(Date.valueOf(pvtStart.getValue()));
                }
                if (repeatPvtStart.getValue() != null) {
                    currentPatient.setRepeatPvtStart(Date.valueOf(repeatPvtStart.getValue()));
                }
                if (pvtEnd.getValue() != null) {
                    currentPatient.setPvtEnd(Date.valueOf(pvtEnd.getValue()));
                }
                if (repeatPvtEnd.getValue() != null) {
                    currentPatient.setRepeatPvtEnd(Date.valueOf(repeatPvtEnd.getValue()));
                }
                if (allergicReactions.getText() != null) {
                    currentPatient.setAllergicReactions(allergicReactions.getText());
                }
                if (ogkSurvey.getText() != null) {
                    currentPatient.setOgkSurvey(ogkSurvey.getText());
                }
                if (create) {
                    dao.createPatient(currentPatient);
                    patientID.setText(String.valueOf(currentPatient.getPatientID()));
                    showDialog.showInformationDialog("Запис про пацієнта успішно створена", patientCabinet);
                    medicalCardOfAmbulatoryPatient.setVisible(true);
                    medicalCardOfAmbulatoryPatient.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            MainController mainController = context.getBean(MainController.class);
                            MedicalCardController medicalCardController = context.getBean(MedicalCardController.class);
                            medicalCardController.setCard(currentPatient.getAmbulatoryCard());
                            mainController.setContent(medicalCardController, "/fxml/medicalCard.fxml");
                            mainController.addBreadCrumb("Кабінет пацієнта", patientCabinet, 1);
                        }
                    });
                } else {
                    dao.updatePatient(currentPatient);
                    if (currentPatient.getLastCard() != null) {
                        dao.updateCard(currentPatient.getLastCard());
                    }
                    showDialog.showInformationDialog("Запис про пацієнта " + currentPatient.getFullName() + " успішно оновлений!", patientCabinet);
                }
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
                        card.setCardType(STATIONARY);
                        dao.createCard(card);
                        medicalCardsForStationaryPatientsContainer.getChildren().add(getCardButton(card));
                        currentPatient.addCard(card);
                        currentPatient.setLastCard(card);
                        setDiagnosis(card);
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
        patientID.setText(EMPTY);
        fullName.setText(EMPTY);
        passportID.setText(EMPTY);
        dateOfBirth.setValue(null);
        phoneNumber.setText(EMPTY);
        workPlace.setText(EMPTY);
        age.setText(EMPTY);
        address.setText(EMPTY);
        medicalCardsForStationaryPatientsContainer.getChildren().clear();
        mainDiagnosis.setText(EMPTY);
        concomitant.setText(EMPTY);
        pvt.setText(EMPTY);
        complication.setText(EMPTY);
        pvtStart.setValue(null);
        pvtEnd.setValue(null);
        repeatPvtEnd.setValue(null);
        repeatPvtStart.setValue(null);
        allergicReactions.setText(EMPTY);
        ogkSurvey.setText(EMPTY);
        MainController mainController = context.getBean(MainController.class);
        mainController.breadCrumbsContainer.getChildren().clear();
        mainController.content.setContent(patientCabinet);
        medicalCardOfAmbulatoryPatient.setVisible(false);
    }

    public void setPatient(Patient patient) {
        removePatient();
        currentPatient = patient;
        medicalCardOfAmbulatoryPatient.setVisible(true);
        medicalCardOfAmbulatoryPatient.addEventHandler(MouseEvent.MOUSE_CLICKED, getEventHandler(patient.getAmbulatoryCard()));
        patientID.setText(String.valueOf(patient.getPatientID()));
        fullName.setText(patient.getFullName());
        passportID.setText(patient.getPassportID());
        dateOfBirth.setValue(patient.getDateOfBirth() == null ? null : patient.getDateOfBirth().toLocalDate());
        phoneNumber.setText(patient.getPhoneNumber());
        workPlace.setText(patient.getWorkPlace());
        address.setText(patient.getAddress());
        for (Toggle toggle : addressType.getToggles()) {
            if (((RadioButton) toggle).getText().equals(patient.getAddressType())) {
                toggle.setSelected(true);
            }
        }
        for (Card card : patient.getCardsList()) {
            medicalCardsForStationaryPatientsContainer.getChildren().add(getCardButton(card));
        }
        if (patient.getLastCard() != null) {
            setDiagnosis(patient.getLastCard());
        }
        pvtStart.setValue(patient.getPvtStart() == null ? null : patient.getPvtStart().toLocalDate());
        pvtEnd.setValue(patient.getPvtEnd() == null ? null : patient.getPvtEnd().toLocalDate());
        repeatPvtEnd.setValue(patient.getRepeatPvtEnd() == null ? null : patient.getRepeatPvtEnd().toLocalDate());
        repeatPvtStart.setValue(patient.getRepeatPvtStart() == null ? null : patient.getRepeatPvtStart().toLocalDate());
        allergicReactions.setText(patient.getAllergicReactions());
        ogkSurvey.setText(patient.getOgkSurvey());

    }

    public void setDiagnosis(Card card) {
        mainDiagnosis.setText(card.getMainDiagnosis());
        complication.setText(card.getComplication());
        pvt.setText(card.getPvt());
        concomitant.setText(card.getConcomitant());
    }

    private JFXButton getCardButton(Card card) {
        JFXButton newCard = new JFXButton(String.format("№ = %s\n Тиждень лікування = %s\n Дата виписки = %tF\n Дата госпіталізації = %tF",
                card.getCardNumber(), card.getWeek(), card.getDateOut(), card.getDateIn()));
        newCard.addEventHandler(MouseEvent.MOUSE_CLICKED, getEventHandler(card));
        newCard.setPrefHeight(91);
        newCard.setMinWidth(100);
        newCard.setMaxHeight(200);
        return newCard;
    }

    private EventHandler<MouseEvent> getEventHandler(Card card) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MainController mainController = context.getBean(MainController.class);
                MedicalCardController medicalCardController = context.getBean(MedicalCardController.class);
                medicalCardController.setCard(card);
                mainController.setContent(medicalCardController, "/fxml/medicalCard.fxml");
                mainController.addBreadCrumb("Кабінет пацієнта", patientCabinet, 1);
            }
        };
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
        this.currentPatient = currentPatient;
    }


}
