package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.InitStructure;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class);
    @FXML
    public Tab allPatientsTab;
    @FXML
    public Tab patientCabinet;
    @FXML
    public TabPane mainTabPane;
    @FXML
    public HBox breadCrumbsContainer;
    @FXML
    public ScrollPane content;
    private ApplicationContext context;
    private HashMap<Integer, Node> breadCrumbs = new HashMap<>();

    @Autowired
    public MainController(InitStructure InitStructure) {
        LOGGER.debug("Init MainController");
        InitStructure.initStructure();
    }

    @FXML
    private void initialize() {
        LOGGER.debug("Run initialize method");
        allPatientsTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (allPatientsTab.isSelected()) {
                    AllPatientsController allPatientsController = context.getBean(AllPatientsController.class);
                    allPatientsController.fillTableView();
                }

            }
        });

    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    public void addBreadCrumb(Button button, Node node, int level) {
        LOGGER.debug("add: " + button);
        breadCrumbs.put(level, button);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                content.setContent(node);
                breadCrumbsContainer.getChildren().remove(button);
            }
        });
        breadCrumbsContainer.getChildren().add(button);
    }

}
