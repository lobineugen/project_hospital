package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.InitStructure;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
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
    private int breadcrumbMaxLevel;

    @Autowired
    public MainController(InitStructure InitStructure) {
        InitStructure.initStructure();
    }

    @FXML
    private void initialize() {
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

    public void addBreadCrumb(String buttonName, Node node, int level) {
        Button button = new Button(buttonName);
        breadCrumbs.put(level, button);
        breadcrumbMaxLevel = level > breadcrumbMaxLevel ? level : breadcrumbMaxLevel;
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                content.setContent(node);

                if (level <= breadcrumbMaxLevel) {
                    for (Map.Entry<Integer, Node> entry : breadCrumbs.entrySet()) {
                        if (entry.getKey() >= level) {
                            breadCrumbsContainer.getChildren().remove(entry.getValue());
                        }
                    }
                }
            }
        });
        breadCrumbsContainer.getChildren().add(button);
    }

    public void setContent(Object controller, String url) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    url));
            fxmlLoader.setController(controller);
            Pane pane = fxmlLoader.load();
            this.content.setContent(pane);
        } catch (IOException e) {
            LOGGER.error("IOException:", e);
        }
    }
}
