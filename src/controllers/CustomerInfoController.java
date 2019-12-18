/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Observable;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.*;

/**
 *
 * @author Duc
 */
public class CustomerInfoController implements Initializable {

    @FXML
    private TableView<CustomerInfoModelTable> tableCustomer;
    @FXML
    private TableColumn<CustomerInfoModelTable, String> tableCustomer_ColId;
    @FXML
    private TableColumn<CustomerInfoModelTable, String> tableCustomer_Name;
    @FXML
    private TableColumn<CustomerInfoModelTable, String> tableCustomer_Gender;
    @FXML
    private TableColumn<CustomerInfoModelTable, String> tableCustomer_DoB;
    @FXML
    private TableColumn<CustomerInfoModelTable, String> tableCustomer_Address;
    @FXML
    private TableColumn<CustomerInfoModelTable, String> tableCustomer_PhoneNo;
    @FXML
    private TableColumn<CustomerInfoModelTable, String> tableCustomer_Email;
    @FXML
    private TableColumn<CustomerInfoModelTable, String> tableCustomer_Username;
    @FXML
    private TableColumn<CustomerInfoModelTable, String> tableCustomer_Password;

    ObservableList<CustomerInfoModelTable> oblist = FXCollections.observableArrayList();

    ObservableList<String> genderlist = FXCollections.observableArrayList("Nam", "Nữ", "Khác");

    @FXML
    private JFXDatePicker jfxdatepicker;
    @FXML
    private JFXComboBox<String> jfxcombobox;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtAddress;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXTextField txtPassword;
    @FXML
    private JFXTextField txtPhoneno;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnDel;
    @FXML
    private AnchorPane customerAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StageDraggable.makeDraggable(customerAnchorPane);
        btnAdd.setDisable(false);
        btnSave.setDisable(true);
        btnDel.setDisable(true);
        jfxcombobox.setItems(genderlist);
        jfxcombobox.setValue(genderlist.get(0));
        try {
            Connection con = ConnectionUtil.conDB();

            ResultSet rs = con.createStatement().executeQuery("select * from apdb.tbl_register");

            while (rs.next()) {
                oblist.add(new CustomerInfoModelTable(
                        rs.getString("customer_id"),
                        rs.getString("fullname"),
                        rs.getString("sex"),
                        rs.getString("date_of_birth"),
                        rs.getString("home_address"),
                        rs.getString("phone_no"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableCustomer_ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableCustomer_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableCustomer_Gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tableCustomer_DoB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        tableCustomer_Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        tableCustomer_PhoneNo.setCellValueFactory(new PropertyValueFactory<>("phoneno"));
        tableCustomer_Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableCustomer_Username.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableCustomer_Password.setCellValueFactory(new PropertyValueFactory<>("password"));

        tableCustomer.setItems(oblist);
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
    private void handleAddCustomer(MouseEvent event) {
        int last_id = Integer.parseInt(oblist.get(oblist.size() - 1).getId());
        String id = Integer.toString(last_id + 1);
        String name = (String) txtName.getText();
        String gender = (String) jfxcombobox.getValue();
        String dob = (String) jfxdatepicker.getValue().format(Datetime.getFormatter());
        String address = (String) txtAddress.getText();
        String phoneno = (String) txtPhoneno.getText();
        String email = (String) txtEmail.getText();
        String username = (String) txtUsername.getText();
        String password = (String) txtPassword.getText();
        oblist.add(new CustomerInfoModelTable(
                id,
                name,
                gender,
                dob,
                address,
                phoneno,
                email,
                username,
                password
        ));
        tableCustomer.setItems(oblist);
        String[] inputString = { id, name, gender, dob, address, phoneno, email, username, password };
        String addQuery = "insert into "
                + ConnectionUtil.getDBName()
                + ".tbl_register (`customer_id`, `fullname`, `sex`, `date_of_birth`, `home_address`, `phone_no`, `email`, `username`, `password`) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        ConnectionUtil.updateQueryWithPreparedStatment(addQuery, inputString);
    }

    @FXML
    private void handleSaveChangeCustomerInfo(MouseEvent event) {
        btnAdd.setDisable(false);
        btnSave.setDisable(true);
        btnDel.setDisable(true);
        int id = tableCustomer.getSelectionModel().getSelectedIndex();
        CustomerInfoModelTable modeltable = oblist.get(id);
        modeltable.setName(txtName.getText());
        modeltable.setGender(jfxcombobox.getValue());
        modeltable.setDob(jfxdatepicker.getValue().format(Datetime.getFormatter()));
        modeltable.setAddress(txtAddress.getText());
        modeltable.setEmail(txtEmail.getText());
        modeltable.setPhoneno(txtPhoneno.getText());
        modeltable.setUsername(txtUsername.getText());
        modeltable.setPassword(txtPassword.getText());
        oblist.set(id, modeltable);
        tableCustomer.setItems(oblist);
        
        String updateQuery = "UPDATE  "
                + ConnectionUtil.getDBName()
                +".`tbl_register` SET "
                + "`fullname` = ?, "
                + "`sex` = ?, "
                + "`date_of_birth` = ?, "
                + "`home_address` = ?, "
                + "`phone_no` = ?, "
                + "`email` = ?, "
                + "`username` = ?, "
                + "`password` = ? "
                + "WHERE (`customer_id` = ?);";
        String[] data = {
            txtName.getText(),
            jfxcombobox.getValue(),
            jfxdatepicker.getValue().format(Datetime.getFormatter()),
            txtAddress.getText(),
            txtPhoneno.getText(),
            txtEmail.getText(),
            txtUsername.getText(),
            txtPassword.getText(),
            modeltable.getId()
        };
        ConnectionUtil.updateQueryWithPreparedStatment(updateQuery, data);
    }

    @FXML
    private void handleDelCustomer(MouseEvent event) {
        btnAdd.setDisable(false);
        btnSave.setDisable(true);
        btnDel.setDisable(true);
        int id = tableCustomer.getSelectionModel().getSelectedIndex();
        CustomerInfoModelTable modeltable = oblist.get(id);
        oblist.remove(id);
        tableCustomer.setItems(oblist);
        
        String[] data = { modeltable.getId() };
        String delQuery = "DELETE FROM "
                + ConnectionUtil.getDBName()
                +".`tbl_register` WHERE (`customer_id` = ?)";
       
        ConnectionUtil.updateQueryWithPreparedStatment(delQuery, data);
    }

    @FXML
    private void handleTableClicked(MouseEvent event) {
        System.out.println(tableCustomer.getSelectionModel());
        if (tableCustomer.getSelectionModel().getSelectedItem() == null) {
        } else {
            String id = tableCustomer.getSelectionModel().getSelectedItem().getId();
            btnAdd.setDisable(false);
            btnSave.setDisable(false);
            btnDel.setDisable(false);
            try {
                Connection con = ConnectionUtil.conDB();
                ResultSet rs = con.createStatement().executeQuery("select * from "
                        + ConnectionUtil.getDBName()
                        + ".tbl_register where customer_id="
                        + id);
                while (rs.next()) {
                    txtName.setText(rs.getString("fullname"));
                    jfxcombobox.setValue(rs.getString("sex"));
                    LocalDate ld = LocalDate.parse(rs.getString("date_of_birth"),
                            Datetime.getFormatter());
                    jfxdatepicker.setValue(ld);
                    txtAddress.setText(rs.getString("home_address"));
                    txtEmail.setText(rs.getString("email"));
                    txtPhoneno.setText(rs.getString("phone_no"));
                    txtUsername.setText(rs.getString("username"));
                    txtPassword.setText(rs.getString("password"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerInfoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
