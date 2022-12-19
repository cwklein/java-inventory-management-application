package klein.firstscreen.controller;

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
import klein.firstscreen.main.InHouse;
import klein.firstscreen.main.Inventory;
import klein.firstscreen.main.Outsourced;
import klein.firstscreen.main.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
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

    public void onSave(ActionEvent actionEvent) {
        // ADD - Check that fields are valid before adding to table

        selectedPart.setName(partName.getText());
        selectedPart.setPrice(Double.parseDouble(partPrice.getText()));
        selectedPart.setStock(Integer.parseInt(partStock.getText()));
        selectedPart.setMin(Integer.parseInt(partMin.getText()));
        selectedPart.setMax(Integer.parseInt(partMax.getText()));

        if(isOutsourced){
            ((Outsourced)selectedPart).setCompanyName(partSource.getText());
        }
        else {
            ((InHouse)selectedPart).setMachineID(Integer.parseInt(partSource.getText()));
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
        if(selectedPart instanceof Outsourced){
            partSource.setText(String.valueOf(((Outsourced) selectedPart).getCompanyName()));
        }
        else {
            partSource.setText("");
        }
    }

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
