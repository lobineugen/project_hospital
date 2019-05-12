package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.service.ShowDialog;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class DiagnosisController {
    @FXML
    public VBox vBox;
    @FXML
    public JFXTextArea mainDiagnosis;
    @FXML
    public JFXTextArea complication;
    @FXML
    public JFXTextArea pvt;
    @FXML
    public JFXTextArea concomitant;
    @FXML
    public JFXButton save;
    private Card card;
    private DAO dao;
    private ShowDialog showDialog;
    private ApplicationContext context;

    @Autowired
    public DiagnosisController(DAO dao, ShowDialog showDialog) {
        this.dao = dao;
        this.showDialog = showDialog;
    }

    @FXML
    public void initialize() {
        mainDiagnosis.setText(card.getMainDiagnosis());
        complication.setText(card.getComplication());
        pvt.setText(card.getPvt());
        concomitant.setText(card.getConcomitant());
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                card.setMainDiagnosis(mainDiagnosis.getText());
                card.setConcomitant(concomitant.getText());
                card.setPvt(pvt.getText());
                card.setComplication(complication.getText());
                dao.updateCard(card);
                showDialog.showInformationDialog("Запис успішно оновлений!", vBox);
                PatientCabinetController patientCabinetController = context.getBean(PatientCabinetController.class);
                if (patientCabinetController.getCurrentPatient().getLastCard().equals(card)) {
                    patientCabinetController.setDiagnosis(card);
                }
            }
        });
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }
}
