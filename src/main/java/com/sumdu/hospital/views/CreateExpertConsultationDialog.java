package com.sumdu.hospital.views;

import com.jfoenix.controls.*;
import com.sumdu.hospital.model.ExpertConsultation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

public class CreateExpertConsultationDialog {
    private Stage stage;
    @FXML
    private VBox vbox;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXTextField expert;

    @FXML
    private JFXTextArea conclusion;

    @FXML
    private JFXButton save;

    private ExpertConsultation expertConsultation;
    private boolean isOkClicked;

    public CreateExpertConsultationDialog(Window ownerStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CreateExpertConsultationDialog.class
                    .getResource("/fxml/createExpertConsultation.fxml"));
            fxmlLoader.setController(this);
            vbox = fxmlLoader.load();

            stage = new Stage();
            stage.initOwner(ownerStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(vbox));

            stage.setTitle("Вікно створення");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setExpertConsultation(ExpertConsultation consultation) {
        expertConsultation = consultation;
        if (expertConsultation != null) {
            if (expertConsultation.getDate() != null) {
                date.setValue(expertConsultation.getDate().toLocalDate());
            }
            expert.setText(expertConsultation.getDoctor());
            conclusion.setText(expertConsultation.getConclusion());
        }
    }

    @FXML
    void onSaveClick() {
        if (expertConsultation == null) {
            expertConsultation = new ExpertConsultation();
        }
        if (date.getValue() != null) {
            expertConsultation.setDate(Date.valueOf(date.getValue()));
        }
        expertConsultation.setDoctor(expert.getText());
        expertConsultation.setConclusion(conclusion.getText());
        isOkClicked = true;
        stage.close();
    }

    public Optional<ExpertConsultation> getExpertConsultation() {
        return Optional.ofNullable(expertConsultation);
    }

    public boolean showAndWait() {
        stage.showAndWait();
        return isOkClicked;
    }
}
