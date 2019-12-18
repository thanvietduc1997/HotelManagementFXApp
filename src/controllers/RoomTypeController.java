/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import utils.StageDraggable;

/**
 * FXML Controller class
 *
 * @author Duc
 */
public class RoomTypeController implements Initializable {

    @FXML
    private TableView<RoomTypeModelTable> tbl_roomtype;
    @FXML
    private TableColumn<RoomTypeModelTable, String> col_type;
    @FXML
    private TableColumn<RoomTypeModelTable, String> col_name;

    ObservableList<RoomTypeModelTable> oblist = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorPaneRoomType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Connection con = ConnectionUtil.conDB();
            ResultSet rs = con.createStatement().executeQuery("select * from "
                    + ConnectionUtil.getDBName()
                    + ".room_type");
            while (rs.next()) {                
                oblist.add(new RoomTypeModelTable(rs.getString("type_id"), rs.getString("type_name")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoomTypeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        tbl_roomtype.setItems(oblist);
        StageDraggable.makeDraggable(AnchorPaneRoomType);
    }

    @FXML
    private void handleClose(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
