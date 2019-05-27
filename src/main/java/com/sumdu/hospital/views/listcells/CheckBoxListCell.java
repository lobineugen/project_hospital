package com.sumdu.hospital.views.listcells;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXTextField;
import com.sumdu.hospital.model.CheckBoxValue;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class CheckBoxListCell extends JFXListCell<CheckBoxValue> {
    @FXML
    private HBox pane;
    @FXML
    private JFXCheckBox checkBox;
    @FXML
    private JFXTextField textField;
    private FXMLLoader mLLoader;
    private CheckBoxValue item;


    public CheckBoxListCell() {
        if (mLLoader == null) {
            mLLoader = new FXMLLoader(getClass().getResource("/fxml/checkBoxListCell.fxml"));
            mLLoader.setController(this);
            try {
                mLLoader.load();
            } catch (IOException e) {
                throw new RuntimeException("Can't load CheckBoxListCell from resources.", e);
            }
        }
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                item.setLabel(newValue);
            }
        });
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                item.setValue(newValue);
            }
        });
    }

    @Override
    protected void updateItem(CheckBoxValue item, boolean empty) {
        this.item = item;
        setText(null);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            textField.setText(item.getLabel());
            if (item.isValue()) {
                checkBox.setSelected(true);
            }
            checkBox.setId(String.valueOf(item.getId()));
            setGraphic(pane);
        }
    }
}
