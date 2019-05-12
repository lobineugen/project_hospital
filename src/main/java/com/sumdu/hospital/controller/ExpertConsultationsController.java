package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXButton;
import com.sumdu.hospital.service.ShowDialog;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Optional;

@Controller
public class ExpertConsultationsController {
    private static final Logger LOGGER = Logger.getLogger(ExpertConsultationsController.class);
    @FXML
    public JFXButton addExpertConsultation;
    @FXML
    public VBox vBox;
    private ShowDialog showDialog;

    public ExpertConsultationsController(ShowDialog showDialog) {
        this.showDialog = showDialog;
    }

    @FXML
    public void initialize() {
        initializeEventHandlers();
    }

    private void initializeEventHandlers() {
        addExpertConsultation.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Optional t = showDialog.createExpertConsultation(vBox);
                } catch (IOException e) {
                    LOGGER.error("IOException:", e);
                }
            }
        });
    }
}
