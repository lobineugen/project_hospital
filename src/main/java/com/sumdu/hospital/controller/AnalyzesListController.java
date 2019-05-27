package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.sumdu.hospital.constants.WindowType;
import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Analysis;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.service.ShowDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class AnalyzesListController {
    @FXML
    private VBox vBox;

    @FXML
    private Label title;

    @FXML
    private JFXListView<Analysis> listView;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton deleteButton;

    @FXML
    public JFXButton editButton;

    private ObservableList<Analysis> analysesList = FXCollections.observableArrayList();
    private ApplicationContext context;
    private ShowDialog showDialog;
    private Card card;
    private DAO dao;
    private String analysisType;

    @Autowired
    public AnalyzesListController(ShowDialog showDialog, DAO dao, ApplicationContext context) {
        this.showDialog = showDialog;
        this.dao = dao;
        this.context = context;
    }

    @FXML
    public void initialize() {
        initTable();
        initializeEventHandlers();
        listView.setCellFactory(analysisListView -> new JFXListCell<Analysis>() {
            @Override
            protected void updateItem(Analysis item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getDate().toString());
                }
            }
        });
    }

    private void initTable() {
        analysesList.clear();
        analysesList.addAll(dao.getAnalyzes(analysisType, card.getCardID()));
        listView.setItems(analysesList);
    }

    private void initializeEventHandlers() {
        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            SingleAnalysisController controller = context.getBean(SingleAnalysisController.class);
            Analysis analysis = new Analysis();
            analysis.setType(analysisType);
            analysis.setCardID(card.getCardID());
            controller.init(vBox.getScene().getWindow(), card, analysis, WindowType.CREATE);
            controller.show();
            initTable();
        });
        deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (listView.getSelectionModel().isEmpty()) {
                showDialog.showInformationDialog("Нічого не вибрано. Видалення неможливо!", vBox);
                return;
            }
            Analysis analysis = listView.getSelectionModel().getSelectedItem();
            dao.deleteByID(analysis.getAnalysisId(), analysis);
            analysesList.remove(analysis);
            showDialog.showInformationDialog("Запис усішно вилучено!", vBox);
        });
        editButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (listView.getSelectionModel().isEmpty()) {
                return;
            }
            Analysis analysis = listView.getSelectionModel().getSelectedItem();
            SingleAnalysisController controller = context.getBean(SingleAnalysisController.class);
            controller.init(vBox.getScene().getWindow(), card, analysis, WindowType.EDIT);
            controller.show();
            initTable();
        });
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
}
