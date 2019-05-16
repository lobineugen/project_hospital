package com.sumdu.hospital.service;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.sumdu.hospital.model.Card;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static com.sumdu.hospital.constants.Constants.*;

@Service
public class ShowDialog {
    private Helper helper;
    private ApplicationContext context;
    private Button createButton;
    private JFXTextField number;
    private JFXTextField week;
    private JFXDatePicker dateIn;
    private JFXDatePicker dateOut;

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
        number = new JFXTextField();
        week = new JFXTextField();
        dateIn = new JFXDatePicker();
        dateOut = new JFXDatePicker();
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

        number.textProperty().addListener(getStringListener(number));
        week.textProperty().addListener(getStringListener(week));
        dateIn.valueProperty().addListener(getDateListener(dateIn));
        dateOut.valueProperty().addListener(getDateListener(dateOut));

        dialog.setResultConverter(new Callback<ButtonType, Card>() {
            @Override
            public Card call(ButtonType param) {
                if (param.equals(createButtonType)) {
                    return new Card(helper.getUniqueID(), number.getText(), Date.valueOf(dateIn.getValue()), Date.valueOf(dateOut.getValue()), week.getText());
                }
                return null;
            }
        });
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

        dialog.getDialogPane().setContent(grid);
        return dialog.showAndWait();
    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    private ChangeListener<String> getStringListener(JFXTextField textField) {
        return new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!number.getText().isEmpty()
                        && !week.getText().isEmpty()
                        && dateIn.getValue() != null
                        && dateOut.getValue() != null) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
                textField.validate();
            }
        };
    }

    private ChangeListener<LocalDate> getDateListener(JFXDatePicker datePicker) {
        return new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (!number.getText().isEmpty()
                        && !week.getText().isEmpty()
                        && dateIn.getValue() != null
                        && dateOut.getValue() != null) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
                datePicker.validate();
            }
        };
    }

    public Optional createExpertConsultation(Pane primaryStage) throws IOException {
        Dialog<Card> dialog = new Dialog<>();
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

        dialog.getDialogPane().setContent(pane);
        return dialog.showAndWait();
    }
}
