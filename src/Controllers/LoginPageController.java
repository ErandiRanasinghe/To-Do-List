package Controllers;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPageController {
    public AnchorPane root;
    public TextField txtUsername;
    public PasswordField txtPassword;
    public static String loginUserName;
    public static String loginUserID;

    public void lblCreateNewAccountOnMouseClicked(MouseEvent mouseEvent) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("../View/CreateNewAccountForm.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Create New Account");
        stage.show();
        stage.centerOnScreen();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        login();

    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        login();
    }

    public void login(){
        String userName = txtUsername.getText();
        String password = txtPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
          PreparedStatement  preparedStatement = connection.prepareStatement("select * from user where name = ? and password = ?");
            preparedStatement.setObject(1, userName);
            preparedStatement.setObject(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){

                loginUserName = resultSet.getString(2);
                loginUserID = resultSet.getString(1);
                Parent parent=FXMLLoader.load(this.getClass().getResource("../View/ToDoForm.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.setTitle("To Do form");
                stage.show();

            }
              else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid User name or Password");
            alert.showAndWait();

            txtUsername.clear();
            txtPassword.clear();

            txtUsername.requestFocus();

              }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
