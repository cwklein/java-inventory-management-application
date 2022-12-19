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

    public void onSave(ActionEvent actionEvent) {
        // ADD - Check that fields are valid before adding to table

        String name = partName.getText();
        double price = Double.parseDouble(partPrice.getText());
        int stock = Integer.parseInt(partStock.getText());
        int min = Integer.parseInt(partMin.getText());
        int max = Integer.parseInt(partMax.getText());

        if(isOutsourced){
            String company = partSource.getText();

            Outsourced newPart = new Outsourced (Inventory.getPartID(), name, price, stock, min, max, company);
            Inventory.addPart(newPart);
        }
        else {
            int machID = Integer.parseInt(partSource.getText());

            InHouse newPart = new InHouse(Inventory.getPartID(), name, price, stock, min, max, machID);
            Inventory.addPart(newPart);
        }

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

    public void toOutsourced(ActionEvent actionEvent) {
        partSourceLabel.setText("Company Name");
        isOutsourced = true;
    }

    public void toInHouse(ActionEvent actionEvent) {
        partSourceLabel.setText("Machine ID");
        isOutsourced = false;
    }
}
