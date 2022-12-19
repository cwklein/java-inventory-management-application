package klein.firstscreen.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import klein.firstscreen.main.Inventory;
import klein.firstscreen.main.Part;
import klein.firstscreen.main.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Colby Klein
 */
public class AddProduct implements Initializable {
    public TextField prodID;
    public TextField prodName;
    public TextField prodPrice;
    public TextField prodStock;
    public TextField prodMax;
    public TextField prodMin;
    @FXML
    private TableView<Part> mainPartTableView;
    @FXML
    private TableColumn<Part, Integer> mainPartIDColumn;
    @FXML
    private TableColumn<Part, String> mainPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> mainPartInvColumn;
    @FXML
    private TableColumn<Part, Double> mainPartPriceColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Filling Main Part Table View
        mainPartTableView.setItems(Inventory.getAllParts());

        //Associating Main Part Table Columns w/ Part Attributes
        mainPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void onSave(ActionEvent actionEvent) {
        // ADD - Check that fields are valid before adding to table
        // ADD - Create relationship between 'associated parts' and 'products'

        String name = prodName.getText();
        double price = Double.parseDouble(prodPrice.getText());
        int stock = Integer.parseInt(prodStock.getText());
        int min = Integer.parseInt(prodMin.getText());
        int max = Integer.parseInt(prodMax.getText());

        Product newProd = new Product (Inventory.getProdID(), name, price, stock, min, max);
        Inventory.addProduct(newProd);

        try {
            returnToMain(actionEvent);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Error: Returning to Main Screen");
            e.printStackTrace();
        }
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/klein/firstscreen/view/mainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }


}