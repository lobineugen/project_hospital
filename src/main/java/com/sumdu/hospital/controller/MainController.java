package com.sumdu.hospital.controller;

import com.sumdu.hospital.database.InitStructure;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class);

    @Autowired
    public MainController(InitStructure InitStructure) {
        LOGGER.debug("Init Main Controller");
        InitStructure.initStructure();
    }
}
