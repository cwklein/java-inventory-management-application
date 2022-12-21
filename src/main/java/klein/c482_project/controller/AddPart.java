package klein.c482_project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import klein.c482_project.model.InHouse;
import klein.c482_project.model.Inventory;
import klein.c482_project.model.Outsourced;

import java.io.IOException;

/**
 * Controller for addPart View.
 * @author Colby Klein
 */
public class AddPart {
    public Label partSourceLabel;
    public TextField partID;
    public TextField partName;
    public TextField partPrice;
    public TextField partStock;
    public TextField partMax;
    public TextField partMin;
    public TextField partSource;
    public Boolean isOutsourced = false;

    /**
     * Reads through the textFields, validates the user's input and if valid adds the new Part to allParts.
     * @param actionEvent is related to clicking the "Save" button.
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        try {
            String name = partName.getText();
            double price = Double.parseDouble(partPrice.getText());
            int min = Integer.parseInt(partMin.getText());
            int max = Integer.parseInt(partMax.getText());
            int stock = Integer.parseInt(partStock.getText());

            if (stock > max || stock < min || min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setHeaderText("Invalid Entry");
                alert.setContentText("Part Inventory must between \"Min\" and \"Max\"\n" +
                        "\"Min\" must be less than or equal to \"Max\"");
                alert.showAndWait();
            } else {
                if (isOutsourced) {
                    try {
                        String company = partSource.getText();

                        Outsourced newPart = new Outsourced(Inventory.getPartID(), name, price, stock, min, max, company);
                        Inventory.addPart(newPart);
                        returnToMain(actionEvent);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialogue");
                        alert.setHeaderText("Invalid Entry");
                        alert.setContentText("Company Name is not a valid format");
                        alert.showAndWait();
                    }
                } else {
                    try {
                        int machID = Integer.parseInt(partSource.getText());

                        InHouse newPart = new InHouse(Inventory.getPartID(), name, price, stock, min, max, machID);
                        Inventory.addPart(newPart);
                        returnToMain(actionEvent);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialogue");
                        alert.setHeaderText("Invalid Entry");
                        alert.setContentText("Part ID is not a valid format");
                        alert.showAndWait();
                    }
                }
            }
        } catch(Exception e) {
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
     * Changes window to allow for completion of Outsourced part. Adds a "Company Name" field in place of "Machine ID".
     * @param actionEvent is related to clicking the "Outsourced" radio button.
     */
    public void toOutsourced(ActionEvent actionEvent) {
        partSourceLabel.setText("Company Name");
        isOutsourced = true;
    }

    /**
     * Changes window to allow for completion of InHouse part. Adds a "Machine ID" field in place of "Company Name".
     * @param actionEvent is related to clicking the "InHouse" radio button.
     */
    public void toInHouse(ActionEvent actionEvent) {
        partSourceLabel.setText("Machine ID");
        isOutsourced = false;
    }
}
