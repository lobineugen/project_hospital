package com.sumdu.hospital.service;

import com.sumdu.hospital.model.Card;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.sumdu.hospital.constants.Constants.INFORMATION_DIALOG;
import static com.sumdu.hospital.constants.Constants.WARNING_DIALOG;
import static com.sumdu.hospital.constants.Constants.getStringConverter;

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

    public Optional<Pair<String, Card>> createCard(Pane primaryStage) {
        Dialog<Pair<String, Card>> dialog = new Dialog<>();
        dialog.setTitle("Создать новую карту");
        dialog.setHeaderText("Заполните необходимие поля что би создать");
        dialog.initOwner(primaryStage.getScene().getWindow());
        TextField number = new TextField();
        TextField week = new TextField();
        DatePicker dateIn = new DatePicker();
        DatePicker dateOut = new DatePicker();
        dateIn.setConverter(getStringConverter());
        dateOut.setConverter(getStringConverter());

        ButtonType createButton = new ButtonType("Створити", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);
        dialog.setResultConverter(new Callback<ButtonType, Pair<String, Card>>() {
            @Override
            public Pair<String, Card> call(ButtonType param) {
                if (param == createButton) {
                    helper = context.getBean(Helper.class);
                    Card card = new Card(helper.getUniqueID(), number.getText(), Date.valueOf(dateIn.getValue()), Date.valueOf(dateOut.getValue()), week.getText());
                    return new Pair<>("newCard", card);
                }
                return null;
            }
        });
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
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
}
