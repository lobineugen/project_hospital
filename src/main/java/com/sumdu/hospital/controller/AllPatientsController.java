package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Patient;
import com.sumdu.hospital.service.Export;
import com.sumdu.hospital.service.ShowDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import java.util.List;

@Component
public class AllPatientsController {
    private static final Logger LOGGER = Logger.getLogger(AllPatientsController.class);
    @FXML
    private AnchorPane allPatientsPane;
    @FXML
    private TableView<Patient> allPatients;
    @FXML
    private TableColumn<Patient, String> fullName;
    @FXML
    private TableColumn<Patient, String> id;
    @FXML
    private TableColumn<Patient, Integer> age;
    @FXML
    private TextField searchValue;
    @FXML
    private Button exportToXSLButton;
    private DAO dao;
    private ObservableList<Patient> patientObservableList = FXCollections.observableArrayList();
    private ApplicationContext context;

    @Autowired
    public AllPatientsController(DAO dao) {
        LOGGER.debug("Run AllPatientsController");
        this.dao = dao;
    }

    @FXML
    private void initialize() {
        LOGGER.debug("Run initialize method");
        fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        patientObservableList.addAll(dao.getAllByName(""));
        allPatients.setItems(patientObservableList);

        initializeEventHandlers();
    }

    private void initializeEventHandlers() {
        searchValue.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String inputText = searchValue.getText();
                List<Patient> patients = dao.getAllByName(inputText);
                patientObservableList.clear();
                patientObservableList.addAll(patients);

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
                    ShowDialog showDialog = context.getBean(ShowDialog.class);
                    showDialog.showInformationDialog("Дані успішно експортовані в Excel");
                }

            }
        });


    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

}