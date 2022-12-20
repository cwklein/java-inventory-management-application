package klein.firstscreen.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author Colby Klein
 */
public class C482_Project extends Application  {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(C482_Project.class.getResource("/klein/firstscreen/view/mainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public static void addTestData() {
        //Populating the Part and Product tables with sample information
        InHouse samplePart1 = new InHouse(Inventory.getPartID(), "Brakes",15.00, 20,10, 20, 101);
        Outsourced samplePart2 = new Outsourced(Inventory.getPartID(), "Wheel",11.00, 15,10, 20, "ABC Company");
        InHouse samplePart3 = new InHouse(Inventory.getPartID(), "Seat",15.00, 10,10, 20, 102);

        Product sampleProduct1 = new Product(Inventory.getProdID(),"Giant Bike", 299.99, 10, 2, 10);
        Product sampleProduct2 = new Product(Inventory.getProdID(), "Tricycle", 99.99, 2, 2, 10);

        Inventory.addPart(samplePart1);
        Inventory.addPart(samplePart2);
        Inventory.addPart(samplePart3);

        Inventory.addProduct(sampleProduct1);
        Inventory.addProduct(sampleProduct2);

        sampleProduct1.addPart(samplePart1);
        sampleProduct2.addPart(samplePart2);
    }

    public static void main(String[] args) {
        addTestData();
        launch();
    }
}