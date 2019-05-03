package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.InitStructure;
import javafx.fxml.FXML;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class);
    private ApplicationContext context;

    @Autowired
    public MainController(InitStructure InitStructure) {
        LOGGER.debug("Init MainController");
        InitStructure.initStructure();
    }
    @FXML
    private void initialize() {
        LOGGER.debug("Run initialize method");

    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }
}
