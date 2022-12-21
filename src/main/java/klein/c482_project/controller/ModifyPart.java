package klein.c482_project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import klein.c482_project.model.InHouse;
import klein.c482_project.model.Inventory;
import klein.c482_project.model.Outsourced;
import klein.c482_project.model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for modifyPart View.
 * @author Colby Klein
 */
public class ModifyPart implements Initializable {
    public TextField partID;
    public TextField partName;
    public TextField partStock;
    public TextField partPrice;
    public TextField partMax;
    public TextField partMin;
    private Part selectedPart;
    public Label partSourceLabel;
    public TextField partSource;
    public RadioButton outsourced;
    public RadioButton inHouse;
    public Boolean isOutsourced = false;

    /**
     * Initializes the textFields with the current values of selectedPart.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedPart = MainScreen.getSelectedPart();

        partID.setText(String.valueOf(selectedPart.getId()));
        partName.setText(String.valueOf(selectedPart.getName()));
        partStock.setText(String.valueOf(selectedPart.getStock()));
        partPrice.setText(String.valueOf(selectedPart.getPrice()));
        partMax.setText(String.valueOf(selectedPart.getMax()));
        partMin.setText(String.valueOf(selectedPart.getMin()));

        if(selectedPart instanceof Outsourced) {
            isOutsourced = true;
            outsourced.setSelected(true);
            partSourceLabel.setText("Company Name");
            partSource.setText(String.valueOf(((Outsourced) MainScreen.getSelectedPart()).getCompanyName()));
        }
        if(selectedPart instanceof InHouse) {
            isOutsourced = false;
            inHouse.setSelected(true);
            partSourceLabel.setText("Machine ID");
            partSource.setText(String.valueOf(((InHouse) MainScreen.getSelectedPart()).getMachineID()));
        }
    }

    /**
     * The onSave method reads through the textFields, validates the user's input and if valid changes the allParts static observableList within Inventory class to reflect any changes made to the Part.
     * @param actionEvent is related to clicking the "Save" button in the window.
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        try {
            int id = Integer.parseInt(partID.getText());
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

                        Outsourced newPart = new Outsourced(id, name, price, stock, min, max, company);
                        Inventory.addPart(newPart);
                        Inventory.getAllParts().remove(selectedPart);
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

                        InHouse newPart = new InHouse(id, name, price, stock, min, max, machID);
                        Inventory.addPart(newPart);
                        Inventory.getAllParts().remove(selectedPart);
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
     * The returnToMain method disregards any inputs within the textFields and returns the user to the main screen.
     * @param actionEvent  is related to clicking the "Cancel" button in the window.
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
        if(selectedPart instanceof Outsourced){
            partSource.setText(String.valueOf(((Outsourced) selectedPart).getCompanyName()));
        }
        else {
            partSource.setText("");
        }
    }

    /**
     * Changes window to allow for completion of InHouse part. Adds a "Machine ID" field in place of "Company Name".
     * @param actionEvent is related to clicking the "InHouse" radio button.
     */
    public void toInHouse(ActionEvent actionEvent) {
        partSourceLabel.setText("Machine ID");
        isOutsourced = false;
        if(selectedPart instanceof InHouse){
            partSource.setText(String.valueOf(((InHouse) selectedPart).getMachineID()));
        }
        else {
            partSource.setText("");
        }
    }

}
