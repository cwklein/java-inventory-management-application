package klein.c482_project.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// JavaDoc Files are located in the JavaDoc folder, zipped and attached separately.

/*Part B Responses:
1) I encountered many bugs throughout this program. One that sticks out as having gave me a particularly hard
time is when I was trying to modify the associated parts list for products. The bug was that, even when I would
cancel out of the modify product list, rather than save changes, the associated parts were still affected.
The way that I got around this was by filling the assocPartTableView with a local, static array based on
the selectedProduct from main screen that was cleared upon exit. This way I found that I was able to modify
it in any way that I liked but the changes to the Product weren't finalized until I clicked "Save".
Essentially kept from modifying the product itself until the save action rather than modifying it
with "Add" and "Remove".

2) Something that I think would extend functionality of this program would be to add another view in which
a user can see a list of all parts currently associated with a product as well as a separate list for those
parts which are un-associated. There could be a second field in which the associated products can be listed
for each part. This would make inventory management easier because management would know which parts are
superfluous and which parts are integral to company performance/ production.
*/


/**
 * The C482_Project class is the main application. It populates the tables with test data, loads the inital scene and launches the application.
 * @author Colby Klein
 */
public class Main extends Application  {

    /**
     * Start loads the initial screen.
     * @param stage the stage to set
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/klein/c482_project/view/mainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * addTestData populates the Inventory with test data to illustrate the table and method functionality.
     */
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

    /**
     * main runs first in the application. First adds test data and then launches the program.
     */
    public static void main(String[] args) {
        addTestData();
        launch();
    }
}