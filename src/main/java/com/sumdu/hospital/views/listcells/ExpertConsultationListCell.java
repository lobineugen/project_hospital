package com.sumdu.hospital.views.listcells;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sumdu.hospital.model.ExpertConsultation;
import com.sumdu.hospital.service.Helper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ExpertConsultationListCell extends JFXListCell<ExpertConsultation> {
    @FXML
    private VBox vbox;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXTextField expert;

    @FXML
    private JFXTextArea conclusion;

    private FXMLLoader mLLoader;

    public ExpertConsultationListCell() {
        if (mLLoader == null) {
            mLLoader = new FXMLLoader(getClass().getResource("/fxml/expertConsultationsListCell.fxml"));
            mLLoader.setController(this);
            try {
                mLLoader.load();
            } catch (IOException e) {
                throw new RuntimeException("Can't load ChatListCellView from resources.", e);
            }
        }
    }

    @Override
    protected void updateItem(ExpertConsultation expertConsultation, boolean empty) {
        super.updateItem(expertConsultation, empty);

        if(empty || expertConsultation == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (expertConsultation.getDate() != null) {
                date.setValue(expertConsultation.getDate().toLocalDate());
            }
            expert.setText(expertConsultation.getDoctor());
            conclusion.setText(expertConsultation.getConclusion());
            date.setOnMouseClicked(e -> {
                if(!date.isEditable() && date.isShowing())
                    date.hide();
            });
            setText(null);
            setGraphic(vbox);
        }
    }
}
