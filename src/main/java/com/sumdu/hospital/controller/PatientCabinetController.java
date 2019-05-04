package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.DAO;
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
import javafx.util.StringConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

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
    public AnchorPane anchorPane;
    private DAO dao;
    private Helper helper;
    private ShowDialog showDialog;
    private ApplicationContext context;
    private Patient currentPatient;

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
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                helper = context.getBean(Helper.class);
                showDialog = context.getBean(ShowDialog.class);
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
                currentPatient.setDateOfBirth(java.sql.Date.valueOf(dateOfBirth.getValue()));
                currentPatient.setAddress(address.getText());
                currentPatient.setWorkPlace(workPlace.getText());
                currentPatient.setPhoneNumber(phoneNumber.getText());
                currentPatient.setAddressType(((RadioButton) addressType.getSelectedToggle()).getText());
                if (create) {
                    dao.createPatient(currentPatient);
                    patientID.setText(String.valueOf(currentPatient.getPatientID()));
                    showDialog.showInformationDialog("Запис про пацієнта успішно створена", anchorPane);
                } else {
                    dao.updatePatient(currentPatient);
                    showDialog.showInformationDialog("Запис про пацієнта " + currentPatient.getFullName() + " успішно оновлений!", anchorPane);
                }
                System.out.println("id current user: " + patientID.getText());
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

}
