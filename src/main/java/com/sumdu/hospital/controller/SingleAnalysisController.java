package com.sumdu.hospital.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sumdu.hospital.constants.Constants;
import com.sumdu.hospital.constants.WindowType;
import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Analysis;
import com.sumdu.hospital.model.AnalysisParameter;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.service.Helper;
import com.sumdu.hospital.service.ShowDialog;
import com.sumdu.hospital.views.CreateExpertConsultationDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;

@Controller
public class SingleAnalysisController {
    private Stage stage;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXButton save;
    @FXML
    private JFXTreeTableView<AnalysisParameter> analysisTableView;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton deleteButton;

    private ApplicationContext context;
    private ShowDialog showDialog;
    private Card card;
    private DAO dao;
    private Analysis analysis;
    private WindowType windowType;
    private ObservableList<AnalysisParameter> params;

    @Autowired
    public SingleAnalysisController(ShowDialog showDialog, DAO dao, ApplicationContext context) {
        this.showDialog = showDialog;
        this.dao = dao;
        this.context = context;
    }

    public void init(Window ownerStage, Card card, Analysis analysis, WindowType windowType) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CreateExpertConsultationDialog.class
                    .getResource("/fxml/singleAnalysis.fxml"));
            fxmlLoader.setController(this);
            stackPane = fxmlLoader.load();
            stage = new Stage();
            stage.initOwner(ownerStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(stackPane));
            stage.setTitle("");

            this.card = card;
            this.analysis = analysis;
            this.windowType = windowType;

            if (windowType == WindowType.EDIT) {
                date.setValue(analysis.getDate().toLocalDate());
                analysis.setParameters(dao.getAnalysisParams(analysis.getAnalysisId()));
                params = FXCollections.observableArrayList(analysis.getParameters());
            } else {
                params = FXCollections.observableArrayList();
                for (Map.Entry<String, String> entry : Constants.analyzesTypesToFields.get(analysis.getType()).entrySet()) {
                    AnalysisParameter parameter = new AnalysisParameter();
                    parameter.setAttr(entry.getValue());
                    params.add(parameter);
                }
            }


            TreeTableColumn<AnalysisParameter, String> paramName = new TreeTableColumn<>();
            TreeTableColumn<AnalysisParameter, String> paramValue = new TreeTableColumn<>();
            paramName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getAttr()));
            paramValue.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getValue()));

            paramValue.setCellFactory((TreeTableColumn<AnalysisParameter, String> param) -> new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder()));
            paramValue.setOnEditCommit((TreeTableColumn.CellEditEvent<AnalysisParameter, String> t)->{
                ((AnalysisParameter) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue())
                    .setValue(t.getNewValue());
                analysisTableView.getSelectionModel().selectBelowCell();
            });

            paramName.setText("Найменування");
            paramValue.setText("Результат");
            analysisTableView.getColumns().add(paramName);
            analysisTableView.getColumns().add(paramValue);
            paramName.prefWidthProperty().bind(analysisTableView.widthProperty().multiply(0.6));
            paramValue.prefWidthProperty().bind(analysisTableView.widthProperty().multiply(0.4));
            paramValue.setEditable(true);

            TreeItem<AnalysisParameter> root = new RecursiveTreeItem<AnalysisParameter>(params, RecursiveTreeObject::getChildren);
            analysisTableView.setRoot(root);
            analysisTableView.setShowRoot(false);
            analysisTableView.setEditable(true);
            analysisTableView.getColumns().setAll(paramName, paramValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        stage.showAndWait();
    }

    @FXML
    void onSaveClick() {
        analysis.setParameters(params);
        if (windowType == WindowType.CREATE) {
            Helper helper = context.getBean(Helper.class);
            analysis.setAnalysisId(helper.getUniqueID());
            if (date.getValue() != null) {
                analysis.setDate(Date.valueOf(date.getValue()));
            }
            for (AnalysisParameter param : analysis.getParameters()) {
                param.setId(helper.getUniqueID());
            }
            if (dao.createAnalysis(analysis)) {
                showDialog.showInformationDialog("Аналіз збережено", stackPane);
            } else {
                showDialog.showErrorDialog("Не вдається збеоегти аналіз", null,  stackPane);
            }
        } else {
            if (dao.updateAnalysis(analysis)) {
                showDialog.showInformationDialog("Аналіз збережено", stackPane);
            } else {
                showDialog.showErrorDialog("Не вдається збеоегти аналіз", null,  stackPane);
            }
        }
        stage.close();
    }
}
