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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import klein.firstscreen.main.Inventory;
import klein.firstscreen.main.Part;
import klein.firstscreen.main.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
    private static final ObservableList<Part> tempParts = FXCollections.observableArrayList();
    public TableView<Part> assocPartTableView;
    public TableColumn<Part, Integer> assocPartIDColumn;
    public TableColumn<Part, String> assocPartNameColumn;
    public TableColumn<Part, Integer> assocPartStockColumn;
    public TableColumn<Part, Double> assocPartPriceColumn;
    private Product selectedProduct;
    private Part selectedPartToAdd;
    private Part selectedPartToRemove;
    @FXML
    private TableView<Part> mainPartTableView;
    @FXML
    private TableColumn<Part, Integer> mainPartIDColumn;
    @FXML
    private TableColumn<Part, String> mainPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> mainPartStockColumn;
    @FXML
    private TableColumn<Part, Double> mainPartPriceColumn;
    public TextField partSearch;

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
        mainPartStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //
        tempParts.addAll(selectedProduct.getAllParts());

        //Filling Associated Part Table View
        assocPartTableView.setItems(tempParts);

        //Associating Associated Part Table Columns w/ Part Attributes
        assocPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void onSave(ActionEvent actionEvent) throws IOException {
        // ADD - Create relationship between 'associated parts' and 'products'
        try {
            int tempMin = Integer.parseInt(prodMin.getText());
            int tempMax = Integer.parseInt(prodMax.getText());
            int tempStock = Integer.parseInt(prodStock.getText());

            if (tempMin > tempMax || tempStock < tempMin || tempStock > tempMax) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setHeaderText("Invalid Entry");
                alert.setContentText("Part Inventory must between \"Min\" and \"Max\"\n" +
                        "\"Min\" must be less than or equal to \"Max\"");
                alert.showAndWait();
            } else {
                selectedProduct.setName(prodName.getText());
                selectedProduct.setPrice(Double.parseDouble(prodPrice.getText()));
                selectedProduct.setStock(Integer.parseInt(prodStock.getText()));
                selectedProduct.setMin(Integer.parseInt(prodMin.getText()));
                selectedProduct.setMax(Integer.parseInt(prodMax.getText()));

                selectedProduct.getAllParts().clear();

                if(tempParts.size() > 0) {
                    for (Part part : tempParts) {
                        selectedProduct.addPart(part);
                    }
                }

                returnToMain(actionEvent);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("Invalid Entry");
            alert.setContentText("At least one attribute is formatted incorrectly or missing");
            alert.showAndWait();
        }
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        tempParts.clear();
        Parent parent = FXMLLoader.load(getClass().getResource("/klein/firstscreen/view/mainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }


    public void lookupPart(ActionEvent actionEvent) {
        try {
            String searchName = partSearch.getText();

            ObservableList<Part> returnedParts = searchByPartName(searchName);

            if (returnedParts.size() == 0) {
                int searchID = Integer.parseInt(searchName);
                returnedParts.add(searchByPartID(searchID));
            }

            mainPartTableView.setItems(returnedParts);
            selectedPartToAdd = returnedParts.get(0);

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

    public void selectPartToAdd(MouseEvent mouseEvent) {
        selectedPartToAdd = mainPartTableView.getSelectionModel().getSelectedItem();
    }

    public void selectPartToRemove(MouseEvent mouseEvent) {
        selectedPartToRemove = assocPartTableView.getSelectionModel().getSelectedItem();
    }

    public void addAssocPart(ActionEvent actionEvent) {
        if(tempParts.contains(selectedPartToAdd)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("Part already added");
            alert.setContentText("You have already added this part to the associated list");
            alert.showAndWait();
        } else {
            try {
                selectedPartToAdd.getName();
                tempParts.add(selectedPartToAdd);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setHeaderText("No part selected");
                alert.setContentText("You must first select an associated part in order to add it");
                alert.showAndWait();
            }
        }
    }

    public void removeAssocPart(ActionEvent actionEvent) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setContentText("Are you sure you want to remove: " + selectedPartToRemove.getName());

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                tempParts.remove(selectedPartToRemove);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("No part selected");
            alert.setContentText("You must first select an associated part in order to remove it");
            alert.showAndWait();
        }
    }
}
