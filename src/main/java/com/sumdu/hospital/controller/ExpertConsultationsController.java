package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXButton;
import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.model.ExpertConsultation;
import com.sumdu.hospital.service.ShowDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static com.sumdu.hospital.constants.Constants.FIELD_NAME_RATIO;

@Controller
public class ExpertConsultationsController {
    private static final Logger LOGGER = Logger.getLogger(ExpertConsultationsController.class);
    @FXML
    public JFXButton addExpertConsultation;
    @FXML
    public VBox vBox;
    @FXML
    public TableView<ExpertConsultation> tableView;
    @FXML
    public JFXButton deleteExpertConsultation;
    @FXML
    public JFXButton editExpertConsultation;
    private ObservableList<ExpertConsultation> expertConsultations = FXCollections.observableArrayList();
    private ShowDialog showDialog;
    private Card card;
    private DAO dao;

    @Autowired
    public ExpertConsultationsController(ShowDialog showDialog, DAO dao) {
        this.showDialog = showDialog;
        this.dao = dao;
    }

    @FXML
    public void initialize() {
        initTable();
        initializeEventHandlers();
    }

    private void initTable() {
        TableColumn<ExpertConsultation, Date> date = new TableColumn<>();
        TableColumn<ExpertConsultation, String> doctor = new TableColumn<>();
        TableColumn<ExpertConsultation, String> conclusion = new TableColumn<>();
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        doctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        conclusion.setCellValueFactory(new PropertyValueFactory<>("conclusion"));
        date.setText(FIELD_NAME_RATIO.get("date"));
        doctor.setText(FIELD_NAME_RATIO.get("doctor"));
        conclusion.setText(FIELD_NAME_RATIO.get("conclusion"));
        tableView.getColumns().add(date);
        tableView.getColumns().add(doctor);
        tableView.getColumns().add(conclusion);
        date.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        doctor.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        conclusion.prefWidthProperty().bind(tableView.widthProperty().multiply(0.5));
        expertConsultations.clear();
        if (card.getExpertConsultationList() != null) {
            expertConsultations.addAll(card.getExpertConsultationList());
        }
        tableView.setItems(expertConsultations);
    }

    private void initializeEventHandlers() {
        addExpertConsultation.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Optional<ExpertConsultation> optional = showDialog.createExpertConsultation(vBox, null);
                    if (optional.isPresent()) {
                        ExpertConsultation expertConsultation = optional.get();
                        expertConsultation.setCardID(card.getCardID());
                        dao.createExpertConsultation(expertConsultation);
                        expertConsultations.add(expertConsultation);
                    }
                } catch (IOException e) {
                    LOGGER.error("IOException:", e);
                }
            }
        });
        deleteExpertConsultation.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (tableView.getSelectionModel().isEmpty()) {
                    showDialog.showInformationDialog("Нічого не вибрано. Видалення неможливо!", vBox);
                    return;
                }
                ExpertConsultation expertConsultation = tableView.getSelectionModel().getSelectedItem();
                dao.deleteByID(expertConsultation.getConsID(), expertConsultation);
                expertConsultations.remove(expertConsultation);
                showDialog.showInformationDialog("Запис усішно вилучено!", vBox);
            }
        });
        editExpertConsultation.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (tableView.getSelectionModel().isEmpty()) {
                    showDialog.showInformationDialog("Нічого не вибрано. Редагування неможливо!", vBox);
                    return;
                }
                ExpertConsultation expertConsultation = tableView.getSelectionModel().getSelectedItem();
                try {
                    Optional<ExpertConsultation> optional = showDialog.createExpertConsultation(vBox, expertConsultation);
                    if (optional.isPresent()) {
                        tableView.refresh();
                        dao.updateExpertConsultation(optional.get());
                    }
                } catch (IOException e) {
                    LOGGER.error("IOException:", e);
                }
            }
        });
    }


    public void setCard(Card card) {
        this.card = card;
    }
}
