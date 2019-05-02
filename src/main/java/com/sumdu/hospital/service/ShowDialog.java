package com.sumdu.hospital.service;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

@Service
public class ShowDialog {
    public void showInformationDialog(String information) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Інформаційний діалог");
        alert.setHeaderText(null);
        alert.setContentText(information);

        alert.showAndWait();
    }
}
