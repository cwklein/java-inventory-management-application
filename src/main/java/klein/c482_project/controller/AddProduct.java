package klein.c482_project.controller;

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
import klein.c482_project.model.Inventory;
import klein.c482_project.model.Part;
import klein.c482_project.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for addProduct View.
 * @author Colby Klein
 */
public class AddProduct implements Initializable {
    public TextField prodID;
    public TextField prodName;
    public TextField prodPrice;
    public TextField prodStock;
    public TextField prodMax;
    public TextField prodMin;
    private Part selectedPartToAdd;
    private Part selectedPartToRemove;
    public TableView<Part> assocPartTableView;
    public TableColumn<Part, Integer> assocPartIDColumn;
    public TableColumn<Part, String> assocPartNameColumn;
    public TableColumn<Part, Integer> assocPartStockColumn;
    public TableColumn<Part, Double> assocPartPriceColumn;
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
    public TextField partSearch;
    private static final ObservableList<Part> newProdParts = FXCollections.observableArrayList();

    /**
     * Initializes the mainPartTableView and assocPartTableViews with corresponding columns.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Filling Main Part Table View
        mainPartTableView.setItems(Inventory.getAllParts());

        //Associating Main Part Table Columns w/ Part Attributes
        mainPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Filling Associated Part Table View
        assocPartTableView.setItems(newProdParts);

        //Associating Associated Part Table Columns w/ Part Attributes
        assocPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Reads through the textFields, validates the user's input and if valid adds the new Product to allProducts.
     * @param actionEvent is related to clicking the "Save" button.
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        // ADD - Create relationship between 'associated parts' and 'products'
        try {
            String name = prodName.getText();
            double price = Double.parseDouble(prodPrice.getText());
            int stock = Integer.parseInt(prodStock.getText());
            int min = Integer.parseInt(prodMin.getText());
            int max = Integer.parseInt(prodMax.getText());

            if (min > max || stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setHeaderText("Invalid Entry");
                alert.setContentText("Product Inventory must between \"Min\" and \"Max\"\n" +
                        "\"Min\" must be less than or equal to \"Max\"");
                alert.showAndWait();
            } else {
                Product newProd = new Product(Inventory.getProdID(), name, price, stock, min, max);
                Inventory.addProduct(newProd);
                for (Part part : newProdParts) {
                    newProd.addPart(part);
                }
                returnToMain(actionEvent);
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("Invalid Entry");
            alert.setContentText("At least one attribute is formatted incorrectly or missing");
            alert.showAndWait();
        }
    }

    /**
     * Returns the user to the main screen.
     * @param actionEvent  is related to clicking the "Cancel" button or following the onSave method.
     */
    public void returnToMain(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/klein/c482_project/view/mainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Changes the mainPartTableView to reflect only those parts whose ID or name match the search criteria.
     * @param actionEvent is related to pressing enter on the part search bar.
     */
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

    /**
     * defines selectedPartToAdd as whichever part has been clicked by the user.
     * @param mouseEvent is related to whatever part is clicked by the user in mainPartTableView.
     */
    public void selectPartToAdd(MouseEvent mouseEvent) {
        selectedPartToAdd = mainPartTableView.getSelectionModel().getSelectedItem();
    }

    /**
     * defines selectedPartToRemove as whichever part has been clicked by the user.
     * @param mouseEvent is related to whatever part is clicked by the user in assocPartTableView.
     */
    public void selectPartToRemove(MouseEvent mouseEvent) {
        selectedPartToRemove = assocPartTableView.getSelectionModel().getSelectedItem();
    }

    /**
     * Adds selectedPartToAdd to the assocPartTableView.
     * @param actionEvent is related to clicking the "Add" button.
     */
    public void addAssocPart(ActionEvent actionEvent) {
        if(newProdParts.contains(selectedPartToAdd)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setHeaderText("Part already added");
            alert.setContentText("You have already added this part to the associated list");
            alert.showAndWait();
        } else {
            try {
                selectedPartToAdd.getName();
                newProdParts.add(selectedPartToAdd);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setHeaderText("No part selected");
                alert.setContentText("You must first select an associated part in order to add it");
                alert.showAndWait();
            }
        }
    }

    /**
     * Removes selectedPartToRemove from the assocPartTableView.
     * @param actionEvent is related to clicking the "Remove" button.
     */
    public void removeAssocPart(ActionEvent actionEvent) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setContentText("Are you sure you want to remove: " + selectedPartToRemove.getName());

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                newProdParts.remove(selectedPartToRemove);
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
