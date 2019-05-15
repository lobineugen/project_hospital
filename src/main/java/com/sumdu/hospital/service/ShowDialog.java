package com.sumdu.hospital.service;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.model.ExpertConsultation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sumdu.hospital.constants.Constants.*;

@Service
public class ShowDialog {
    private Helper helper;
    private ApplicationContext context;


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

    public Optional<Card> createCard(Pane primaryStage) {
        Button createButton;
        JFXTextField number = new JFXTextField();
        JFXTextField week = new JFXTextField();
        JFXDatePicker dateIn = new JFXDatePicker();
        JFXDatePicker dateOut = new JFXDatePicker();
        helper = context.getBean(Helper.class);
        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle("Вікно створення");
        dialog.setHeaderText("Заповніть всі поля щоб створити нову карту");
        dialog.initOwner(primaryStage.getScene().getWindow());
        Image img = new Image(getClass().getResource("/img/icon_create.png").toString());
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        dialog.setGraphic(imageView);
        dateIn.setConverter(getStringConverter());
        dateOut.setConverter(getStringConverter());


        number.setMinWidth(240);
        dateIn.setMinWidth(240);
        dateOut.setMinWidth(240);
        week.setMinWidth(240);

        helper.addRequiredValidator(number);
        helper.addRequiredValidator(dateOut);
        helper.addRequiredValidator(dateIn);
        helper.addRequiredValidator(week);

        ButtonType createButtonType = new ButtonType("Створити", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        createButton = (Button) dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(30);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("№:"), 0, 0);
        grid.add(number, 1, 0);
        grid.add(new Label("Тиждень лікування:"), 0, 1);
        grid.add(week, 1, 1);
        grid.add(new Label("Дата виписки:"), 0, 2);
        grid.add(dateOut, 1, 2);
        grid.add(new Label("Дата госпіталізації:"), 0, 3);
        grid.add(dateIn, 1, 3);

        number.textProperty().addListener(getStringListener(number, grid.getChildren(), createButton, 4));
        week.textProperty().addListener(getStringListener(week, grid.getChildren(), createButton, 4));
        dateIn.valueProperty().addListener(getDateListener(dateIn, grid.getChildren(), createButton, 4));
        dateOut.valueProperty().addListener(getDateListener(dateOut, grid.getChildren(), createButton, 4));

        dialog.setResultConverter(new Callback<ButtonType, Card>() {
            @Override
            public Card call(ButtonType param) {
                if (param == createButtonType) {
                    return new Card(helper.getUniqueID(), number.getText(), Date.valueOf(dateIn.getValue()), Date.valueOf(dateOut.getValue()), week.getText());
                }
                return null;
            }
        });

        dialog.getDialogPane().setContent(grid);
        return dialog.showAndWait();
    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    private ChangeListener<String> getStringListener(TextInputControl textField, List<Node> list, Button button, int count) {
        return new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int cnt = check(list);
                if (cnt == count) {
                    button.setDisable(false);
                } else {
                    button.setDisable(true);
                }
                if (textField instanceof JFXTextArea) {
                    ((JFXTextArea) textField).validate();
                } else if (textField instanceof JFXTextField) {
                    ((JFXTextField) textField).validate();
                }
            }
        };
    }

    private ChangeListener<LocalDate> getDateListener(JFXDatePicker datePicker, List<Node> list, Button button, int count) {
        return new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                int cnt = check(list);
                if (cnt == count) {
                    button.setDisable(false);
                } else {
                    button.setDisable(true);
                }
                datePicker.validate();
            }
        };
    }


    private int check(List<Node> list) {
        int result = 0;
        for (Node node : list) {
            if (node instanceof JFXTextField && !((JFXTextField) node).getText().isEmpty()) {
                result++;
            } else if (node instanceof JFXDatePicker && ((JFXDatePicker) node).getValue() != null) {
                result++;
            } else if (node instanceof JFXTextArea && !((JFXTextArea) node).getText().isEmpty()) {
                result++;
            }
        }
        return result;
    }

    public Optional<ExpertConsultation> createExpertConsultation(Pane primaryStage, ExpertConsultation expertConsultation) throws IOException {
        helper = context.getBean(Helper.class);
        Dialog<ExpertConsultation> dialog = new Dialog<>();
        dialog.setTitle("Вікно створення");
        dialog.setHeaderText("Заповніть всі поля щоб створити новий запис");
        dialog.initOwner(primaryStage.getScene().getWindow());
        Image img = new Image(getClass().getResource("/img/icon_create.png").toString());
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        dialog.setGraphic(imageView);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/fxml/expertConsultationsListCell.fxml"));
        Pane pane = fxmlLoader.load();
        ButtonType buttonType;
        if (expertConsultation == null) {
            buttonType = new ButtonType("Створити", ButtonBar.ButtonData.OK_DONE);
        } else {
            buttonType = new ButtonType("Оновити", ButtonBar.ButtonData.OK_DONE);
        }

        dialog.getDialogPane().getButtonTypes().addAll(buttonType, ButtonType.CANCEL);
        Button createButton = (Button) dialog.getDialogPane().lookupButton(buttonType);
        createButton.setDisable(true);

        JFXDatePicker date = (JFXDatePicker) ((HBox) pane.getChildren().get(0)).getChildren().get(0);
        JFXTextField doctor = (JFXTextField) ((HBox) pane.getChildren().get(0)).getChildren().get(1);
        JFXTextArea conclusion = (JFXTextArea) pane.getChildren().get(2);
        date.setConverter(getStringConverter());

        if (expertConsultation != null) {
            if (expertConsultation.getDate() != null) {
                date.setValue(expertConsultation.getDate().toLocalDate());
            }
            doctor.setText(expertConsultation.getDoctor());
            conclusion.setText(expertConsultation.getConclusion());
        }

        helper.addRequiredValidator(date);
        helper.addRequiredValidator(doctor);
        helper.addRequiredValidator(conclusion);

        List<Node> list = new ArrayList<>();
        list.add(date);
        list.add(doctor);
        list.add(conclusion);

        doctor.textProperty().addListener(getStringListener(doctor, list, createButton, 3));
        conclusion.textProperty().addListener(getStringListener(conclusion, list, createButton, 3));
        date.valueProperty().addListener(getDateListener(date, list, createButton, 3));

        dialog.setResultConverter(new Callback<ButtonType, ExpertConsultation>() {
            @Override
            public ExpertConsultation call(ButtonType param) {
                if (param == buttonType && expertConsultation == null) {
                    return new ExpertConsultation(helper.getUniqueID(), Date.valueOf(date.getValue()), doctor.getText(), conclusion.getText());
                } else if (param == buttonType) {
                    expertConsultation.setDate(Date.valueOf(date.getValue()));
                    expertConsultation.setDoctor(doctor.getText());
                    expertConsultation.setConclusion(conclusion.getText());
                    return expertConsultation;
                }
                return null;
            }
        });

        dialog.getDialogPane().setContent(pane);
        return dialog.showAndWait();
    }
}
