package klein.firstscreen.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import klein.firstscreen.main.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 * @author Colby Klein
 */
public class MainScreen implements Initializable {

    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableColumn<Part, Integer> partIDColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInvColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;
    @FXML
    private TableView<Product> prodTableView;
    @FXML
    private TableColumn<Product, Integer> prodIDColumn;
    @FXML
    private TableColumn<Product, String> prodNameColumn;
    @FXML
    private TableColumn<Product, Integer> prodInvColumn;
    @FXML
    private TableColumn<Product, Double> prodPriceColumn;
    private static Part selectedPart;
    private static Product selectedProduct;
    public TextField productSearch;
    public TextField partSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Filling Part Table View
        partTableView.setItems(Inventory.getAllParts());

        //Associating Part Table Columns w/ Part Attributes
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Filling Product Table View
        prodTableView.setItems(Inventory.getAllProducts());

        //Associating Product Table Columns w/ Product Attributes
        prodIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    public void addPart(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/klein/firstscreen/view/addPart.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    public void updatePart(ActionEvent actionEvent) throws IOException{
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/klein/firstscreen/view/modifyPart.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setTitle("Modify Part: " + selectedPart.getName());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("No part selected");
            alert.setContentText("You must first select a part in order to modify it");
            alert.showAndWait();
        }
    }

    public void deletePart(ActionEvent actionEvent) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setContentText("Are you sure you want to delete: " + selectedPart.getName());

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.getAllParts().remove(selectedPart);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("No part selected");
            alert.setContentText("You must first select a part in order to delete it");
            alert.showAndWait();
        }
    }

    public void addProduct(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/klein/firstscreen/view/addProduct.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    public void updateProduct(ActionEvent actionEvent) throws IOException{
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/klein/firstscreen/view/modifyProduct.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setTitle("Modify Product: " + selectedProduct.getName());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("No product selected");
            alert.setContentText("You must first select a product in order to modify it");
            alert.showAndWait();
        }
    }

    public void deleteProduct(ActionEvent actionEvent) throws IOException {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setContentText("Are you sure you want to delete: " + selectedProduct.getName());

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.getAllProducts().remove(selectedProduct);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("No product selected");
            alert.setContentText("You must first select a product in order to delete it");
            alert.showAndWait();
        }
    }

    public void closeProgram(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void selectPart(MouseEvent mouseEvent) {
        selectedPart = partTableView.getSelectionModel().getSelectedItem();
    }

    public static Part getSelectedPart() {
        return selectedPart;
    }

    public void selectProduct(MouseEvent mouseEvent) {
        selectedProduct = prodTableView.getSelectionModel().getSelectedItem();
    }

    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    public void lookupProduct(ActionEvent actionEvent) {
        try{String searchName = productSearch.getText();

            ObservableList<Product> returnedProducts = searchByProductName(searchName);

            if (returnedProducts.size() == 0) {
                int searchID = Integer.parseInt(searchName);
                returnedProducts.add(searchByProductID(searchID));
            }

            prodTableView.setItems(returnedProducts);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("No search results");
            alert.setContentText("No products match your current search criteria");
            alert.showAndWait();
        }
    }

    private ObservableList<Product> searchByProductName(String partialName) {
        ObservableList<Product> possibleProducts = FXCollections.observableArrayList();

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for(Product prod : allProducts) {
            if(prod.getName().toLowerCase().contains(partialName.toLowerCase())){
                possibleProducts.add(prod);
            }
        }

        return possibleProducts;
    }

    private Product searchByProductID(int partialID) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for(Product prod : allProducts) {
            if(prod.getId() == partialID){
                return prod;
            }
        }
        return null;
    }

    public void lookupPart(ActionEvent actionEvent) {
        try {
            String searchName = partSearch.getText();

            ObservableList<Part> returnedParts = searchByPartName(searchName);

            if (returnedParts.size() == 0) {
                int searchID = Integer.parseInt(searchName);
                returnedParts.add(searchByPartID(searchID));
            }

            partTableView.setItems(returnedParts);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("No search results");
            alert.setContentText("No parts match your current search criteria");
            alert.showAndWait();
        }
    }

    private ObservableList<Part> searchByPartName(String partialName) {
        ObservableList<Part> possibleParts = FXCollections.observableArrayList();

        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part part : allParts) {
            if(part.getName().toLowerCase().contains(partialName.toLowerCase())){
                possibleParts.add(part);
            }
        }

        return possibleParts;
    }

    private Part searchByPartID(int partialID) {
        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part part : allParts) {
            if(part.getId() == partialID){
                return part;
            }
        }
        return null;
    }
}