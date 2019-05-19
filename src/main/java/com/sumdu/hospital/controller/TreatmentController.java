package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
public class TreatmentController {
    @FXML
    private VBox vbox;
    @FXML
    private JFXTextArea etiotropicTherapy;
    @FXML
    private JFXTextArea secondTherapy;
    @FXML
    private JFXTextArea recommendations;
    @FXML
    private JFXTextField doctor;
    @FXML
    private JFXButton save;
    private ApplicationContext context;
    private Card card;
    private final DAO dao;
    private final ShowDialog showDialog;

    @Autowired
    public TreatmentController(DAO dao, ShowDialog showDialog) {
        this.dao = dao;
        this.showDialog = showDialog;
    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    @FXML
    public void initialize() {
        etiotropicTherapy.setText(card.getEtiotropicTherapy());
        secondTherapy.setText(card.getSecondTherapy());
        recommendations.setText(card.getRecommendations());
        doctor.setText(card.getDoctor());
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                card.setEtiotropicTherapy(etiotropicTherapy.getText());
                card.setSecondTherapy(secondTherapy.getText());
                card.setRecommendations(recommendations.getText());
                card.setDoctor(doctor.getText());
                dao.updateCard(card);
                showDialog.showInformationDialog("Запис успішно оновлений!", vbox);
            }
        });
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
