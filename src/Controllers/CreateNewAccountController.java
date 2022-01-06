package Controllers;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import db.DBConnection;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CreateNewAccountController {
    public TextField txtNewPassword;
    public TextField txtConfirmPassword;
    public Label lblpasswword1;
    public Label lblpassword2;
    public TextField txtUserName;
    public TextField txtEmail;
    public Button btnRegister;
    public Label lblid;
    public AnchorPane root;


    public void initialize() {
        lblpasswword1.setVisible(false);
        lblpassword2.setVisible(false);
       fieldDisable(true);
    }

    public void btnREgisterOnAction(ActionEvent actionEvent) {
        register();
    }

    public void boardercolor(String color) {
        txtNewPassword.setStyle("-fx-border-color:" + color);
        txtConfirmPassword.setStyle("-fx-border-color:" + color);


    }

    public void setlabelvisibility(boolean isvisible) {
        lblpasswword1.setVisible(isvisible);
        lblpassword2.setVisible(isvisible);


    }

    public void AddNewUSerOnAction(ActionEvent actionEvent) {
        fieldDisable(false);
        DBConnection object = DBConnection.getInstance();
        Connection connection = object.getConnection();


    }
    public void fieldDisable(boolean isDisable ) {
        txtUserName.setDisable(isDisable );
        txtEmail.setDisable(isDisable );
        txtNewPassword.setDisable(isDisable );
        txtConfirmPassword.setDisable(isDisable );
        btnRegister.setDisable(isDisable);

         autoGenerateID();
    }
    public void autoGenerateID(){
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet=  statement.executeQuery("select id from user order by id desc limit 1");
            boolean isExist = resultSet.next();

            if (isExist){

                String userID = resultSet.getString(1);
                userID = userID.substring(1, userID.length());
                int intid = Integer.parseInt(userID);
                intid++;
                        if (intid<10){
                            lblid.setText("U00"+ intid) ;
                        }
                        else if ( intid<100){
                            lblid.setText("U0" + intid);
                        }
                        else {
                            lblid.setText("U" + intid);
                        }

                lblid.setText( "U00" + intid);


            }
            else {
               lblid.setText("U001");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void txtConfirmPasswordOnAction(ActionEvent actionEvent) {
        register();
    }
    public void register(){
        if (txtUserName.getText().isEmpty()){
            txtUserName.requestFocus();
        }
        else if (txtEmail.getText().isEmpty()){
            txtEmail.requestFocus();
        }
        else if (txtNewPassword.getText().isEmpty()){
            txtNewPassword.requestFocus();
        }
           else if (txtConfirmPassword.getText().isEmpty()){
               txtConfirmPassword.requestFocus();
        }
           else {
            String newPassword = txtNewPassword.getText();
            String confirmPassword = txtConfirmPassword.getText();

            if (newPassword.equals(confirmPassword)) {
                boardercolor("transparent");
                setlabelvisibility(false);
                String id = lblid.getText();
                String UserName = txtUserName.getText();
                String email = txtEmail.getText();


                Connection connection = DBConnection.getInstance().getConnection();
                try {

                    PreparedStatement preparedStatement = connection.prepareStatement("insert into user values(?,?,?,?)");
                    preparedStatement.setObject(1, id);
                    preparedStatement.setObject(2, UserName);
                    preparedStatement.setObject(3, email);
                    preparedStatement.setObject(4, confirmPassword);

                    int i = preparedStatement.executeUpdate();
                    if (i>0){
//                   Alert alert= new Alert(Alert.AlertType.CONFIRMATION,"Success..");
//                   alert.showAndWait();

                        Parent parent=FXMLLoader.load(this.getClass().getResource("../View/LoginPageForm.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) root.getScene().getWindow();
                        stage.setScene(scene);
                        stage.setTitle("Login");
                        stage.centerOnScreen();
                    }



                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }

            } else {
                boardercolor("red");
                setlabelvisibility(true);
                txtNewPassword.clear();
                txtConfirmPassword.clear();
            }
        }

    }
}
