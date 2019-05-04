package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.InitStructure;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class);
    @FXML
    public Tab allPatientsTab;
    private ApplicationContext context;

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
}
