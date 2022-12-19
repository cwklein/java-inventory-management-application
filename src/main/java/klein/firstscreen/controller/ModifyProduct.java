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
public class ModifyProduct implements Initializable {
    public TextField prodID;
    public TextField prodName;
    public TextField prodStock;
    public TextField prodPrice;
    public TextField prodMax;
    public TextField prodMin;
    private Product selectedProduct;
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
        // ADD - Create relationship between 'associated parts' and 'products'
        selectedProduct = MainScreen.getSelectedProduct();

        //Populating fields with info from selected product
        prodID.setText(String.valueOf(selectedProduct.getId()));
        prodName.setText(String.valueOf(selectedProduct.getName()));
        prodStock.setText(String.valueOf(selectedProduct.getStock()));
        prodPrice.setText(String.valueOf(selectedProduct.getPrice()));
        prodMax.setText(String.valueOf(selectedProduct.getMax()));
        prodMin.setText(String.valueOf(selectedProduct.getMin()));

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

        selectedProduct.setName(prodName.getText());
        selectedProduct.setPrice(Double.parseDouble(prodPrice.getText()));
        selectedProduct.setStock(Integer.parseInt(prodStock.getText()));
        selectedProduct.setMin(Integer.parseInt(prodMin.getText()));
        selectedProduct.setMax(Integer.parseInt(prodMax.getText()));

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
