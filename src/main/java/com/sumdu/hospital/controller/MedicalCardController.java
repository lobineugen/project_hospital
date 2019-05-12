package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXButton;
import com.sumdu.hospital.model.Card;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class MedicalCardController {
    private static final Logger LOGGER = Logger.getLogger(MedicalCardController.class);
    private ApplicationContext context;
    private Card card;
    private MainController mainController;
    @FXML
    public JFXButton diagnosis;
    @FXML
    public StackPane stackPane;
    @FXML
    public JFXButton treatment;
    @FXML
    public JFXButton labDiagnostics;
    @FXML
    public JFXButton expertConsultations;

    public MedicalCardController(ApplicationContext context, MainController mainController) {
        this.context = context;
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        diagnosis.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DiagnosisController diagnosisController = context.getBean(DiagnosisController.class);
                diagnosisController.setCard(card);
                mainController.setContent(diagnosisController, "/fxml/diagnosis.fxml");
                mainController.addBreadCrumb("Медична карта", stackPane, 2);
            }
        });
        treatment.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TreatmentController treatmentController = context.getBean(TreatmentController.class);
                treatmentController.setCard(card);
                mainController.setContent(treatmentController, "/fxml/treatment.fxml");
                mainController.addBreadCrumb("Медична карта", stackPane, 2);

            }
        });
        labDiagnostics.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                LabDiagnosticsController labDiagnosticsController = context.getBean(LabDiagnosticsController.class);
                mainController.setContent(labDiagnosticsController, "/fxml/labDiagnostics.fxml");
                mainController.addBreadCrumb("Медична карта", stackPane, 2);
            }
        });
        expertConsultations.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ExpertConsultationsController expertConsultationsController = context.getBean(ExpertConsultationsController.class);
                mainController.setContent(expertConsultationsController, "/fxml/expertConsultations.fxml");
                mainController.addBreadCrumb("Медична карта", stackPane, 2);
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
