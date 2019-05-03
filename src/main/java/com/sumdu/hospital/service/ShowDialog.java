package com.sumdu.hospital.service;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShowDialog {
    private ApplicationContext context;

    public void showInformationDialog(String information, Pane primaryStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Інформаційний діалог");
        alert.setHeaderText(null);
        alert.setContentText(information);
        alert.initOwner(primaryStage.getScene().getWindow());
        alert.showAndWait();
    }

    public Optional<String> showTextInputDialog(String title, String headerText, String contentText, Pane primaryStage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.initOwner(primaryStage.getScene().getWindow());
        return dialog.showAndWait();
    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }
}
