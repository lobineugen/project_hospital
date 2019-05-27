package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.model.CheckBoxValue;
import com.sumdu.hospital.service.CheckBoxHelper;
import com.sumdu.hospital.service.ShowDialog;
import com.sumdu.hospital.views.listcells.CheckBoxListCell;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static com.sumdu.hospital.constants.Constants.CLINICAL_DATA;

@Controller
public class ClinicalDataController {
    private final DAO dao;
    private final ShowDialog showDialog;
    private final CheckBoxHelper helper;
    @FXML
    public JFXListView<CheckBoxValue> listView;
    @FXML
    public JFXButton save;
    @FXML
    public GridPane pane;
    private Card card;

    @Autowired
    public ClinicalDataController(DAO dao, ShowDialog showDialog, CheckBoxHelper helper) {
        this.dao = dao;
        this.showDialog = showDialog;
        this.helper = helper;
    }

    @FXML
    public void initialize() {
        listView.setCellFactory(param -> new CheckBoxListCell());
        listView.setItems(helper.initListView(card.getClinicalData(), CLINICAL_DATA));
        listView.getChildrenUnmodifiable().clear();
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String clinicalData = helper.parseToString(listView.getItems());
                card.setClinicalData(clinicalData);
                dao.updateCard(card);
                showDialog.showInformationDialog("Інформація успішно оновлена", pane);
            }
        });
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
