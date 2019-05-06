package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Patient;
import com.sumdu.hospital.service.Export;
import com.sumdu.hospital.service.ShowDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

import static com.sumdu.hospital.constants.Constants.*;

@Component
public class AllPatientsController {
    private static final Logger LOGGER = Logger.getLogger(AllPatientsController.class);
    @FXML
    public Button deleteCurrent;
    @FXML
    private AnchorPane allPatientsPane;
    @FXML
    private TableView<Patient> allPatients;
    @FXML
    private TextField searchValue;
    @FXML
    private Button exportToXSLButton;
    private DAO dao;
    private ObservableList<Patient> patientObservableList = FXCollections.observableArrayList();
    private ApplicationContext context;
    private ShowDialog showDialog;

    @Autowired
    public AllPatientsController(DAO dao, ShowDialog showDialog) {
        System.out.println(this);
        LOGGER.debug("Run AllPatientsController");
        this.dao = dao;
        this.showDialog = showDialog;
    }

    @FXML
    private void initialize() {
        LOGGER.debug("Run initialize method");
        initTable(dao.getTableDefinition());
        initializeEventHandlers();

    }

    private void initTable(Map<String, String> definition) {
        List<TableColumn<Patient, ?>> collection = new ArrayList<>();
        label:
        for (Map.Entry<String, String> entry : definition.entrySet()) {
            TableColumn<Patient, ?> column;
            switch (entry.getValue()) {
                case TEXT:
                    column = new TableColumn<Patient, String>();
                    break;
                case INTEGER:
                    column = new TableColumn<Patient, Integer>();
                    break;
                case DATE:
                    column = new TableColumn<Patient, Date>();
                    break;
                default:
                    continue label;
            }
            if (entry.getKey().equals(PATIENT_ID)) {
                column.setVisible(false);
            }
            column.setText(FIELD_NAME_RATIO.get(entry.getKey()));
            column.setCellValueFactory(new PropertyValueFactory<>(entry.getKey()));
            collection.add(column);
        }
        Collections.reverse(collection);
        allPatients.getColumns().addAll(collection);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Редагувати");
        contextMenu.getItems().add(edit);
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (allPatients.getSelectionModel().isEmpty()) {
                    return;
                }
                Patient patient = allPatients.getSelectionModel().getSelectedItem();
                PatientCabinetController patientCabinetController = context.getBean(PatientCabinetController.class);
                patientCabinetController.setPatient(patient);
                MainController mainController = context.getBean(MainController.class);
                mainController.mainTabPane.getSelectionModel().select(mainController.patientCabinet);
            }
        });
        allPatients.setContextMenu(contextMenu);
    }

    private void initializeEventHandlers() {
        searchValue.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String inputText = searchValue.getText();
                List<Patient> patients = dao.getPatientByName(inputText);
                patientObservableList.clear();
                patientObservableList.addAll(patients);

            }
        });

        deleteCurrent.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (allPatients.getSelectionModel().isEmpty()) {
                    showDialog.showInformationDialog("Нічого не вибрано. Видалення неможливо!", allPatientsPane);
                    return;
                }
                Patient patient = allPatients.getSelectionModel().getSelectedItem();
                Optional<String> result = showDialog.showTextInputDialog("Підтвердити видалення",
                        "Ви дійсно хочете видалити запис про пацієнта - \"" + patient.getFullName()
                                + "\"\nВідновити дані буде неможливо!",
                        "Введіть повне ім'я паціента для підтвердження", allPatientsPane);
                if (result.isPresent()) {
                    if (result.get().equals(patient.getFullName())) {
                        dao.deleteByID(patient.getPatientID());
                        patientObservableList.remove(patient);
                        PatientCabinetController patientCabinetController = context.getBean(PatientCabinetController.class);
                        if (Integer.valueOf(patientCabinetController.patientID.getText()) == patient.getPatientID()) {
                            patientCabinetController.removePatient();
                        }
                        showDialog.showInformationDialog("Запис про пацієнта повністю вилучено!", allPatientsPane);
                    } else {
                        showDialog.showInformationDialog("Повне ім'я введено невірно, запис про пацієнт не буде видалений!", allPatientsPane);
                    }
                }

            }
        });
        exportToXSLButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Microsoft Office Excel (97-2003)", "*.xls");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(allPatientsPane.getScene().getWindow());
                if (file != null) {
                    Export export = context.getBean(Export.class);
                    export.export(dao, file);
                    showDialog.showInformationDialog("Дані успішно експортовані в Excel", allPatientsPane);
                }

            }
        });


    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }


    public void fillTableView() {
        patientObservableList.clear();
        patientObservableList.addAll(dao.getPatientByName(""));
        allPatients.setItems(patientObservableList);
    }


}