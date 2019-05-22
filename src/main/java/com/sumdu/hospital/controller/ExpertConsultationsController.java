package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.model.ExpertConsultation;
import com.sumdu.hospital.service.Helper;
import com.sumdu.hospital.service.ShowDialog;
import com.sumdu.hospital.views.CreateExpertConsultationDialog;
import com.sumdu.hospital.views.listcells.ExpertConsultationListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ExpertConsultationsController {
    private static final Logger LOGGER = Logger.getLogger(ExpertConsultationsController.class);
    @FXML
    public JFXButton addButton;
    @FXML
    public VBox vBox;
    @FXML
    private JFXListView<ExpertConsultation> listView;
    @FXML
    public JFXButton deleteButton;
    @FXML
    public JFXButton editButton;
    private ObservableList<ExpertConsultation> expertConsultations = FXCollections.observableArrayList();
    private ApplicationContext context;
    private ShowDialog showDialog;
    private Card card;
    private DAO dao;

    @Autowired
    public ExpertConsultationsController(ShowDialog showDialog, DAO dao) {
        this.showDialog = showDialog;
        this.dao = dao;
    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    @FXML
    public void initialize() {
        initTable();
        initializeEventHandlers();
        listView.setCellFactory(param -> new ExpertConsultationListCell());
    }

    private void initTable() {
        expertConsultations.clear();
        if (card.getExpertConsultationList() != null) {
            expertConsultations.addAll(card.getExpertConsultationList());
        }
        listView.setItems(expertConsultations);
    }

    private void initializeEventHandlers() {
        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            CreateExpertConsultationDialog dialog = new CreateExpertConsultationDialog(vBox.getScene().getWindow());
            boolean isOkClicked = dialog.showAndWait();
            if (isOkClicked) {
                Optional<ExpertConsultation> optional = dialog.getExpertConsultation();
                if (optional.isPresent()) {
                    ExpertConsultation result = optional.get();
                    result.setConsID(context.getBean(Helper.class).getUniqueID());
                    result.setCardID(card.getCardID());
                    dao.createExpertConsultation(result);
                    expertConsultations.add(result);
                }
            }
            /*ExpertConsultation expertConsultation = new ExpertConsultation(context.getBean(Helper.class).getUniqueID());
            expertConsultation.setCardID(card.getCardID());
            dao.createExpertConsultation(expertConsultation);
            expertConsultations.add(expertConsultation);*/
        });
        deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (listView.getSelectionModel().isEmpty()) {
                showDialog.showInformationDialog("Нічого не вибрано. Видалення неможливо!", vBox);
                return;
            }
            ExpertConsultation expertConsultation = listView.getSelectionModel().getSelectedItem();
            dao.deleteByID(expertConsultation.getConsID(), expertConsultation);
            expertConsultations.remove(expertConsultation);
            showDialog.showInformationDialog("Запис усішно вилучено!", vBox);
        });
        editButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            ExpertConsultation expertConsultation = listView.getSelectionModel().getSelectedItem();
            if (expertConsultation == null) {
                showDialog.showInformationDialog("Нічого не вибрано. Редагування неможливо!", vBox);
                return;
            }
            CreateExpertConsultationDialog dialog = new CreateExpertConsultationDialog(vBox.getScene().getWindow());
            dialog.setExpertConsultation(expertConsultation);
            boolean isOkClicked = dialog.showAndWait();
            if (isOkClicked) {
                Optional<ExpertConsultation> optional = dialog.getExpertConsultation();
                if (optional.isPresent()) {
                    ExpertConsultation result = optional.get();
                    int index = listView.getSelectionModel().getSelectedIndex();
                    /*expertConsultations.remove(expertConsultation);
                    expertConsultations.add(index, result);*/
                    expertConsultations.set(index, result);
                    System.out.println(expertConsultations);
                    listView.refresh();
                    dao.updateExpertConsultation(result);
                }
            }
        });
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
