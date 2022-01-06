package Controllers;

import Tm.ToDoTm;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ToDoFOrmController {
    public Label lblTitle;
    public Label lblUID;
    public AnchorPane root;
    public Pane subRoot;
    public TextField txtDiscription;
    public ListView<ToDoTm> lstToDo;
    public TextField txtSelectedToDo;
    public Button btnDelete;
    public Button btnUpdate;
    public String selectedID= null;

    public void initialize(){

        lblTitle.setText("Hi " + LoginPageController.loginUserName + " Welcome to To Do List");
        lblUID.setText(LoginPageController.loginUserID);
        subRoot.setVisible(false);
        loadList();

       setDisableCommon(true);

        lstToDo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoTm>() {
            @Override
            public void changed(ObservableValue<? extends ToDoTm> observable, ToDoTm oldValue, ToDoTm newValue) {
                setDisableCommon(false);
                subRoot.setVisible(false);
                ToDoTm selectedItem = lstToDo.getSelectionModel().getSelectedItem();
              if (selectedItem == null){
                  return;
              }
                txtSelectedToDo.setText(selectedItem.getDiscription());
                txtSelectedToDo.requestFocus();
                selectedID = selectedItem.getId();

            }

        });
    }
    public void setDisableCommon(boolean isDisable){
        txtSelectedToDo.setDisable(isDisable);
        btnDelete.setDisable(isDisable);
        btnUpdate.setDisable(isDisable);
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to Log Out..?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();


        if(buttonType.get().equals(ButtonType.YES)){
            Parent parent = FXMLLoader.load(this.getClass().getResource("../View/LoginPageForm.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.centerOnScreen();
        }
    }

    public void btnddNewToDoOnAction(ActionEvent actionEvent) {
        txtDiscription.requestFocus();
        lstToDo.getSelectionModel().clearSelection();
        subRoot.setVisible(true);
        setDisableCommon(true);
        txtSelectedToDo.clear();

    }
    public void btnAddtoListOnAction(ActionEvent actionEvent) {
        String id= autogenareteID();
        String description = txtDiscription.getText();
        String UID = lblUID.getText();


        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into todo values (?,?,?)");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,description);
            preparedStatement.setObject(3,UID);

            int i = preparedStatement.executeUpdate();
            System.out.println(i);
            txtDiscription.clear();
            subRoot.setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

      loadList();

    }
    public String autogenareteID() {

        Connection connection = DBConnection.getInstance().getConnection();

        String id = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from todo  order by id desc limit 1");
            boolean isExist = resultSet.next();

            if (isExist) {

                String todoID = resultSet.getString(1);
                todoID = todoID.substring(1, todoID.length());
                int intid = Integer.parseInt(todoID);
                intid++;
                if (intid < 10) {
                    id = "T00" + intid;
                } else if (intid < 100) {
                    id = "T0" + intid;
                } else {
                    id = "T" + intid;
                }
            } else {
               id="T001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return id;
    }

    public void loadList(){
        ObservableList<ToDoTm> items = lstToDo.getItems();
        items.clear();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
           PreparedStatement prepareStatement = connection.prepareStatement("SELECT * from todo where user_id = ?");
           prepareStatement.setObject(1,lblUID.getText());

           ResultSet resultSet = prepareStatement.executeQuery();
           while (resultSet.next()){
               String id = resultSet.getString(1);
               String description = resultSet.getString(2);
               String user_id = resultSet.getString(3);
               items.add(new ToDoTm(id,description,user_id));

           }
           lstToDo.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnOnActionDelete(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this todo..? ", ButtonType.YES,ButtonType.NO);

        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)){
            Connection connection =DBConnection.getInstance().getConnection();
            try {
                PreparedStatement prepareStatement = connection.prepareStatement("Delete from todo where id = ?");
                prepareStatement.setObject(1,selectedID);
                prepareStatement.executeUpdate();
                txtSelectedToDo.clear();
                loadList();
                setDisableCommon(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

    }

    public void btnOnActionupdate(ActionEvent actionEvent) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update todo set description = ? where id = ?");
            preparedStatement.setObject(1,txtSelectedToDo.getText());
            preparedStatement.setObject(2,selectedID);
            preparedStatement.executeUpdate();
            txtSelectedToDo.clear();
            loadList();
            setDisableCommon(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
