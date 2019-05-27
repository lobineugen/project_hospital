package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Patient;
import com.sumdu.hospital.service.Export;
import com.sumdu.hospital.service.ShowDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.*;

import static com.sumdu.hospital.constants.Constants.*;

@Controller
public class AllPatientsController {
    private static final Logger LOGGER = Logger.getLogger(AllPatientsController.class);
    @FXML
    public Button deleteCurrent;
    @FXML
    public Button createPatient;
    @FXML
    public JFXTreeTableView<Patient> treeTableView;
    @FXML
    private AnchorPane allPatientsPane;
    @FXML
    private TextField searchValue;
    @FXML
    private Button exportToXSLButton;
    private DAO dao;
    private ObservableList<Patient> patientObservableList = FXCollections.observableArrayList();
    private TreeItem<Patient> root = new RecursiveTreeItem<>(patientObservableList, RecursiveTreeObject::getChildren);
    private ApplicationContext context;
    private ShowDialog showDialog;

    @Autowired
    public AllPatientsController(DAO dao, ShowDialog showDialog) {
        this.dao = dao;
        this.showDialog = showDialog;
    }

    @FXML
    private void initialize() {
        initTable(dao.getTableDefinition());
        initializeEventHandlers();

    }

    private void initTable(Map<String, String> definition) {
        List<TreeTableColumn<Patient, ?>> collection = new ArrayList<>();
        label:
        for (Map.Entry<String, String> entry : definition.entrySet()) {
            TreeTableColumn<Patient, ?> column;
            switch (entry.getValue()) {
                case TEXT:
                    column = new TreeTableColumn<Patient, String>();
                    break;
                case INTEGER:
                    column = new TreeTableColumn<Patient, Integer>();
                    break;
                case DATE:
                    column = new TreeTableColumn<Patient, Date>();
                    break;
                default:
                    continue label;
            }
            if (entry.getKey().equals(PATIENT_ID) || entry.getKey().equals(CARD_ID) || !FIELD_NAME_RATIO.containsKey(entry.getKey())) {
                column.setVisible(false);
            }
            column.setText(FIELD_NAME_RATIO.get(entry.getKey()));
            column.setCellValueFactory(new TreeItemPropertyValueFactory<>(entry.getKey()));
            makeHeaderWrappable(column);
            collection.add(column);
        }
        treeTableView.getColumns().addAll(collection);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Редагувати");
        contextMenu.getItems().add(edit);
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (treeTableView.getSelectionModel().isEmpty()) {
                    return;
                }
                Patient patient = treeTableView.getSelectionModel().getSelectedItem().getValue();
                PatientCabinetController patientCabinetController = context.getBean(PatientCabinetController.class);
                patientCabinetController.setPatient(patient);
                MainController mainController = context.getBean(MainController.class);
                mainController.mainTabPane.getSelectionModel().select(mainController.patientCabinet);
            }
        });
        treeTableView.setContextMenu(contextMenu);
        treeTableView.setShowRoot(false);
    }

    private void initializeEventHandlers() {
//        searchValue.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                String inputText = searchValue.getText();
//                List<Patient> patients = dao.getPatientByName(inputText);
//                patientObservableList.clear();
//                patientObservableList.addAll(patients);
//
//            }
//        });

        searchValue.textProperty().addListener((o, oldVal, newVal) -> {
            treeTableView.setPredicate(userProp -> {
                final Patient patient = userProp.getValue();
                return patient.getFullName().contains(newVal)
                        || patient.getDateOfBirth().toString().contains(newVal)
                        || patient.getAddress().contains(newVal)
                        || patient.getWorkPlace().contains(newVal);
            });
        });

        createPatient.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MainController mainController = context.getBean(MainController.class);
                mainController.mainTabPane.getSelectionModel().select(mainController.patientCabinet);
                PatientCabinetController patientCabinetController = context.getBean(PatientCabinetController.class);
                patientCabinetController.removePatient();
            }
        });
        deleteCurrent.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (treeTableView.getSelectionModel().isEmpty()) {
                    showDialog.showInformationDialog("Нічого не вибрано. Видалення неможливо!", allPatientsPane);
                    return;
                }
                Patient patient = treeTableView.getSelectionModel().getSelectedItem().getValue();
                Optional<String> result = showDialog.showTextInputDialog("Підтвердити видалення",
                        "Ви дійсно хочете видалити запис про пацієнта - \"" + patient.getFullName()
                                + "\"\nВідновити дані буде неможливо!",
                        "Введіть повне ім'я паціента для підтвердження", allPatientsPane);
                if (result.isPresent()) {
                    if (result.get().equals(patient.getFullName())) {
                        dao.deleteByID(patient.getPatientID(), patient);
                        patientObservableList.remove(patient);
                        PatientCabinetController patientCabinetController = context.getBean(PatientCabinetController.class);
                        if (!patientCabinetController.patientID.getText().isEmpty()
                                && Integer.valueOf(patientCabinetController.patientID.getText()) == patient.getPatientID()) {
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
        treeTableView.setRoot(root);
    }

    private void makeHeaderWrappable(TreeTableColumn col) {
        Label label = new Label(col.getText());
        label.setStyle("-fx-padding: 4px;");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.JUSTIFY);

        StackPane stack = new StackPane();
        stack.getChildren().add(label);
        stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
        label.prefWidthProperty().bind(stack.prefWidthProperty());
        col.setText(null);
        col.setGraphic(stack);
    }


}