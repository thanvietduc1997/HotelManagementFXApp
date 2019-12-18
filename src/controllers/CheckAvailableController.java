/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.ConnectionUtil;
import utils.Datetime;
import utils.StageDraggable;

/**
 *
 * @author Duc
 */
public class CheckAvailableController implements Initializable {

    @FXML
    private TableView<CheckAvailableModelTable> tblSearch;
    @FXML
    private JFXDatePicker dpArrival;
    @FXML
    private JFXDatePicker dpDepart;
    @FXML
    private JFXComboBox<String> cbRoomType;
    @FXML
    private TableColumn<CheckAvailableModelTable, String> col_id;
    @FXML
    private TableColumn<CheckAvailableModelTable, String> col_type;
    @FXML
    private TableColumn<CheckAvailableModelTable, String> col_beds;
    @FXML
    private TableColumn<CheckAvailableModelTable, String> col_price;

    ObservableList<CheckAvailableModelTable> oblist = FXCollections.observableArrayList();

    ObservableList<String> roomtype = FXCollections.observableArrayList("1", "2", "3", "4");
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnRequest;
    @FXML
    private AnchorPane checkAvailableAnchor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnRequest.setDisable(true);
        btnSearch.setDisable(false);
        cbRoomType.setItems(roomtype);
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("room_type"));
        col_beds.setCellValueFactory(new PropertyValueFactory<>("no_bed"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        StageDraggable.makeDraggable(checkAvailableAnchor);
    }

    @FXML
    private void handleGoBack(MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxmls/MainForm.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    private void handleClose(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleShow(MouseEvent event) {
        try {
            Stage source_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxmls/RoomType.fxml")));
            stage.setX(source_stage.getX() + source_stage.getWidth());
            stage.setY(source_stage.getY());
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
//            StageDraggable.makeDraggable(stage.AnchorPaneRoomType);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    private void handleSearch(MouseEvent event) {
        try {
            oblist.clear();
            String checkInDateString = dpArrival.getValue().format(Datetime.getFormatter());
            String checkOutDateString = dpDepart.getValue().format(Datetime.getFormatter());
            String roomType = cbRoomType.getValue();
            String[] inputString = {
                checkInDateString,
                checkInDateString,
                checkOutDateString,
                checkOutDateString,
                checkInDateString,
                checkOutDateString,
                roomType
            };

            String prepareQuery = "SELECT * "
                    + "FROM "
                    + ConnectionUtil.getDBName()
                    + ".room_details "
                    + "WHERE room_no NOT IN "
                    + "("
                    + "SELECT room_no "
                    + "FROM "
                    + ConnectionUtil.getDBName() + ".tbl_reserve "
                    + "where (? < departure_date and ? >= arrival_date) "
                    + "or (? > arrival_date and ? <= departure_date) "
                    + "or (? <= arrival_date and ? >= departure_date)"
                    + ") and room_type = ?;";
            ResultSet rs = ConnectionUtil.executeQueryWithPreparedStatement(prepareQuery, inputString);
            while (rs.next()) {
                oblist.add(new CheckAvailableModelTable(rs.getString("room_no"), rs.getString("room_type"), rs.getString("no_bed"), rs.getString("price")));
            }
            tblSearch.setItems(oblist);
        } catch (SQLException ex) {
            Logger.getLogger(CheckAvailableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleRedirecting(MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            int id = tblSearch.getSelectionModel().getSelectedIndex();
            CheckAvailableModelTable modeltable = oblist.get(id);
            String roomId = modeltable.getId();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/MakeReservation.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            MakeReservationController controller = fxmlLoader.<MakeReservationController>getController();
            controller.setRoomId(roomId);
            controller.setArrivalDate(dpArrival.getValue());
            controller.setDepartureDate(dpDepart.getValue());
            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(CheckAvailableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleTableClicked(MouseEvent event) {
        if (tblSearch.getSelectionModel().getSelectedItem() == null) {
        } else {
            btnRequest.setDisable(false);
            btnSearch.setDisable(false);
        }
    }

}
