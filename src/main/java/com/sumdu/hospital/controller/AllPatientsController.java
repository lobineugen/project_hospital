package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllPatientsController {
    private static final Logger LOGGER = Logger.getLogger(AllPatientsController.class);
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
    private DAO dao;
    private ObservableList<Patient> patientObservableList = FXCollections.observableArrayList();


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

    }
}