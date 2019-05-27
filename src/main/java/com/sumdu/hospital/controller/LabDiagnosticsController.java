package com.sumdu.hospital.controller;

import com.sumdu.hospital.constants.WindowType;
import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Analysis;
import com.sumdu.hospital.model.Card;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

import java.util.List;

@Controller
public class LabDiagnosticsController {
    private static final Logger LOGGER = Logger.getLogger(MedicalCardController.class);
    private ApplicationContext context;
    private Card card;
    private MainController mainController;
    private DAO dao;
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton clinicBlood;

    @FXML
    private JFXButton biochemistryBlood;

    @FXML
    private JFXButton coagulogramBlood;

    @FXML
    private JFXButton specificDiagnostics;

    @FXML
    private JFXButton immunologicalStudies;

    @FXML
    private JFXButton hormonalPanel;

    @FXML
    private JFXButton clinicUrine;

    @FXML
    private JFXButton glucoseUrine;

    @FXML
    private JFXButton diastaseUrine;

    @FXML
    private JFXButton feces;

    @FXML
    private JFXButton coprogram;

    @FXML
    private JFXButton otherStudies;

    public LabDiagnosticsController(ApplicationContext context, MainController mainController, DAO dao) {
        this.context = context;
        this.mainController = mainController;
        this.dao = dao;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @FXML
    public void initialize() {
        clinicBlood.addEventHandler(MouseEvent.MOUSE_CLICKED, openNewTabWithAnalyzesList("clinicBlood"));
        biochemistryBlood.addEventHandler(MouseEvent.MOUSE_CLICKED, openNewTabWithAnalyzesList("biochemistryBlood"));
        coagulogramBlood.addEventHandler(MouseEvent.MOUSE_CLICKED, openNewTabWithAnalyzesList("coagulogramBlood"));
        diastaseUrine.addEventHandler(MouseEvent.MOUSE_CLICKED, createNewWindowWithSingleAnalysis("diastaseUrine", stackPane));
        glucoseUrine.addEventHandler(MouseEvent.MOUSE_CLICKED, createNewWindowWithSingleAnalysis("glucoseUrine", stackPane));
        feces.addEventHandler(MouseEvent.MOUSE_CLICKED, createNewWindowWithSingleAnalysis("feces", stackPane));
    }

    public EventHandler<MouseEvent> createNewWindowWithSingleAnalysis(String analysisType, Pane ownerPane) {
        return event -> {
            SingleAnalysisController controller = context.getBean(SingleAnalysisController.class);
            List<Analysis> analyzes = dao.getAnalyzes(analysisType, card.getCardID());
            Analysis analysis;
            WindowType windowType;
            if (analyzes.isEmpty()) {
                windowType = WindowType.CREATE;
                analysis = new Analysis();
                analysis.setCardID(card.getCardID());
                analysis.setType(analysisType);
            } else {
                analysis = analyzes.get(0);
                windowType = WindowType.EDIT;
            }
            controller.init(ownerPane.getScene().getWindow(), card, analysis, windowType);
            controller.show();
        };
    }

    public EventHandler<MouseEvent> openNewTabWithAnalyzesList(String analysisType) {
        return event -> {
            String title = ((Button)event.getSource()).getText();
            AnalyzesListController analyzesListController = context.getBean(AnalyzesListController.class);
            analyzesListController.setCard(card);
            analyzesListController.setAnalysisType(analysisType);
            mainController.setContent(analyzesListController, "/fxml/listCRUD.fxml");
            mainController.addBreadCrumb("Лабораторна діагностика", stackPane, 3);
            analyzesListController.setTitle(title);
        };
    }
}
