/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import utils.Datetime;

/**
 * FXML Controller class
 *
 * @author Duc
 */
public class MakeReservationController implements Initializable {

    @FXML
    private JFXTextField txtCustomerId;
    @FXML
    private JFXTextField txtRoomId;
    @FXML
    private JFXDatePicker dpArrival;
    @FXML
    private JFXDatePicker dpDepart;
    @FXML
    private Label txtInfo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleClose(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    private void HandleGoBack(MouseEvent event) {
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
    private void handleMakeReservation(MouseEvent event) {
        String query = "INSERT INTO "
                + ConnectionUtil.getDBName()
                + ".tbl_reserve (customer_id, room_no, arrival_date, departure_date) "
                + "VALUES (?, ?, ?, ?);";
        String[] data = {
            txtCustomerId.getText(),
            txtRoomId.getText(),
            dpArrival.getValue().format(Datetime.getFormatter()),
            dpDepart.getValue().format(Datetime.getFormatter())
        };
        if (ConnectionUtil.updateQueryWithPreparedStatment(query, data)) {
            txtInfo.setTextFill(Color.GREEN);
            txtInfo.setText("Đặt phòng thành công");
        }
    }
    
    public void setRoomId (String roomId) {
        txtRoomId.setText(roomId);
    }
    public void setArrivalDate (LocalDate arrival_date) {
        dpArrival.setValue(arrival_date);
    }
    public void setDepartureDate (LocalDate departure_date) {
        dpDepart.setValue(departure_date);
    }

}
