/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import javafx.scene.input.KeyCode;
import utils.*;

/**
 *
 * @author thanv
 */
public class LoginController implements Initializable {
    
    @FXML
    private AnchorPane cardPane;
    
    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnLogin;
    
    @FXML
    private Label lblErrors;
    
    //----
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    void handleClose(MouseEvent event) {
        Platform.exit();
    }
    
    @FXML
    void handleKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (logIn().equals("Success")) {
                try {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxmls/MainForm.fxml")));
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

    @FXML
    void handleLogin(MouseEvent event) {
        if (logIn().equals("Success")) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxmls/MainForm.fxml")));
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        JFXDepthManager.setDepth(cardPane, 2);
        if (con == null) {
            setLblError(Color.TOMATO, "Kết nối đến máy chủ thất bại");
        } else {
            setLblError(Color.GREEN, "Kết nối đến máy chủ thành công");
        }
        StageDraggable.makeDraggable(cardPane);
    }    
    
    public LoginController() {
        con = ConnectionUtil.conDB();
    }
    
    private String logIn() {
        String status = "Success";
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        if(username.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Tên đăng nhập hoặc Mật khẩu không hợp lệ");
            status = "Error";
        } else {
            //query
            String sql = "SELECT * FROM tbl_login Where USERNAME=? and PASSWORD=?";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    setLblError(Color.TOMATO, "Tên đăng nhập hoặc Mật khẩu không đúng");
                    status = "Error";
                } else {
                    Name.setName(resultSet.getString("username"));
                    setLblError(Color.GREEN, "Đăng nhập thành công");
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }
        return status;
    }
    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        lblErrors.setAlignment(Pos.CENTER);
        System.out.println(text);
    }
}
