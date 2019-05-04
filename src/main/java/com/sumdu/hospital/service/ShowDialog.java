package com.sumdu.hospital.service;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sumdu.hospital.constants.Constants.INFORMATION_DIALOG;
import static com.sumdu.hospital.constants.Constants.WARNING_DIALOG;

@Service
public class ShowDialog {

    public void showInformationDialog(String information, Pane primaryStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INFORMATION_DIALOG);
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

    public void showErrorDialog(String header, String information, Pane primaryStage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(WARNING_DIALOG);
        alert.setHeaderText(header);
        alert.setContentText(information);
        alert.initOwner(primaryStage.getScene().getWindow());
        alert.showAndWait();
    }
}
