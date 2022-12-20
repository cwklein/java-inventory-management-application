package klein.firstscreen.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import klein.firstscreen.main.InHouse;
import klein.firstscreen.main.Inventory;
import klein.firstscreen.main.Outsourced;

import java.io.IOException;

/**
 *
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

    public void onSave(ActionEvent actionEvent) throws IOException {
        // ADD - Check that fields are valid before adding to table
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

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/klein/firstscreen/view/mainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void toOutsourced(ActionEvent actionEvent) {
        partSourceLabel.setText("Company Name");
        isOutsourced = true;
    }

    public void toInHouse(ActionEvent actionEvent) {
        partSourceLabel.setText("Machine ID");
        isOutsourced = false;
    }
}
