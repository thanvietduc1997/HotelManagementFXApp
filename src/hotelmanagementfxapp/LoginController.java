/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementfxapp;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.*   ;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.ConnectionUtil;

/**
 *
 * @author thanv
 */
public class LoginController implements Initializable {
    
    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;
    
    @FXML
    private Label lblErrors;
    
    //----
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;


    @FXML
    void handleLogin(MouseEvent event) {
        if (logIn().equals("Success")) {
            try {

                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
//                Label status_lbl = new Label("Chuyển hướng");
//                    Timeline timeline = new Timeline(
//                    new KeyFrame(Duration.ZERO, new EventHandler() {
//                      @Override public void handle(Event event) {
//                        String statusText = status_lbl.getText();
//                        status_lbl.setText(
//                          ("Chuyển hướng . . .".equals(statusText))
//                            ? "Chuyển hướng ." 
//                            : statusText + " ."
//                        );
//                      }
//                    }),  
//                    new KeyFrame(Duration.millis(200))
//                  );
//                  timeline.setCycleCount(Timeline.INDEFINITE);
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
        if (con == null) {
            setLblError(Color.TOMATO, "Kết nối đến máy chủ thất bại");
        } else {
            setLblError(Color.GREEN, "Kết nối đến máy chủ thành công");
        }
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
