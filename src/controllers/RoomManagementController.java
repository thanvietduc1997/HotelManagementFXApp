/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import utils.Datetime;
import utils.StageDraggable;

/**
 * FXML Controller class
 *
 * @author Duc
 */
public class RoomManagementController implements Initializable {

    @FXML
    private AnchorPane roomAnchor;
    @FXML
    private JFXTextField txtRoomId;
    @FXML
    private JFXTextField txtRoomType;
    @FXML
    private JFXTextField txtRoomNoBed;
    @FXML
    private JFXTextField txtRoomPrice;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnDel;
    @FXML
    private TableView<RoomManagementModelTable> tbl_room;
    @FXML
    private TableColumn<RoomManagementModelTable, String> tbl_room_id;
    @FXML
    private TableColumn<RoomManagementModelTable, String> tbl_room_type;
    @FXML
    private TableColumn<RoomManagementModelTable, String> tbl_room_nobed;
    @FXML
    private TableColumn<RoomManagementModelTable, String> tbl_room_price;

    ObservableList<RoomManagementModelTable> oblist = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            btnAdd.setDisable(false);
            btnSave.setDisable(true);
            btnDel.setDisable(true);
            tbl_room_id.setCellValueFactory(new PropertyValueFactory<>("room_no"));
            tbl_room_type.setCellValueFactory(new PropertyValueFactory<>("room_type"));
            tbl_room_nobed.setCellValueFactory(new PropertyValueFactory<>("no_bed"));
            tbl_room_price.setCellValueFactory(new PropertyValueFactory<>("price"));

            Connection con = ConnectionUtil.conDB();
            ResultSet rs = con.createStatement().executeQuery("select * from "
                    + ConnectionUtil.getDBName()
                    + ".room_details");
            while (rs.next()) {
                oblist.add(new RoomManagementModelTable(rs.getString("room_no"), rs.getString("room_type"), rs.getString("no_bed"), rs.getString("price")));
            }
            tbl_room.setItems(oblist);
        } catch (SQLException ex) {
            Logger.getLogger(RoomManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
        StageDraggable.makeDraggable(roomAnchor);
    }

    @FXML
    private void handleAdd(MouseEvent event) {
        // TODO add your handling code here:
        String idroom = txtRoomId.getText();
        String type = txtRoomType.getText();
        String no_bed = txtRoomNoBed.getText();
        String price = txtRoomPrice.getText();
        String[] data = { idroom, type, no_bed, price };
        
        oblist.add(new RoomManagementModelTable(idroom, type, no_bed, price));
        tbl_room.setItems(oblist);
        
        String addQuery = "INSERT INTO "
                + ConnectionUtil.getDBName() +
                ".`room_details` (`room_no`, `room_type`, `no_bed`, `price`)"
                + " VALUES (?, ?, ?, ?)";
        ConnectionUtil.updateQueryWithPreparedStatment(addQuery, data);
        txtRoomId.clear();
        txtRoomType.clear();
        txtRoomNoBed.clear();
        txtRoomPrice.clear();
    }

    @FXML
    private void handleSave(MouseEvent event) {
        int id = tbl_room.getSelectionModel().getSelectedIndex();
        RoomManagementModelTable modeltable = oblist.get(id);
        modeltable.setRoom_no(txtRoomId.getText());
        modeltable.setRoom_type(txtRoomType.getText());
        modeltable.setNo_bed(txtRoomNoBed.getText());
        modeltable.setPrice(txtRoomPrice.getText());
        oblist.set(id, modeltable);
        String updateQuery = "UPDATE  "
                + ConnectionUtil.getDBName()
                +".`room_details` SET "
                + "`room_type` = ?, "
                + "`no_bed` = ?, "
                + "`price` = ? "
                + "WHERE (`room_no` = ?);";
        String[] data = {
            txtRoomType.getText(),
            txtRoomNoBed.getText(),
            txtRoomPrice.getText(),
            modeltable.getRoom_no()
        };
        ConnectionUtil.updateQueryWithPreparedStatment(updateQuery, data);
    }

    @FXML
    private void handleDel(MouseEvent event) {
        int id = tbl_room.getSelectionModel().getSelectedIndex();
        RoomManagementModelTable modeltable = oblist.get(id);
        oblist.remove(id);
        tbl_room.setItems(oblist);
        
        String[] data = { modeltable.getRoom_no() };
        String delQuery = "DELETE FROM "
                + ConnectionUtil.getDBName()
                +".`tbl_register` WHERE (`customer_id` = ?)";
       
        ConnectionUtil.updateQueryWithPreparedStatment(delQuery, data);
    }

    @FXML
    private void handleTableClicked(MouseEvent event) {
        if(tbl_room.getSelectionModel().getSelectedItem() != null) {
            btnSave.setDisable(false);
            btnDel.setDisable(false);
            try {
                String id = tbl_room.getSelectionModel().getSelectedItem().getRoom_no();
                Connection con = ConnectionUtil.conDB();
                ResultSet rs = con.createStatement().executeQuery("select * from "
                        + ConnectionUtil.getDBName()
                        + ".room_details where room_no="
                        + id);
                while (rs.next()) {
                    txtRoomId.setText(rs.getString("room_no"));
                    txtRoomType.setText(rs.getString("room_type"));
                    txtRoomNoBed.setText(rs.getString("no_bed"));
                    txtRoomPrice.setText(rs.getString("price"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerInfoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handleClose(MouseEvent event) {
        Platform.exit();
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

 

}
