package klein.c482_project.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Maintains the static inventory lists (allParts & allProducts), part and product ID's and related methods.
 * @author Colby Klein
 */
public class Inventory {

    private static int partID = 1;
    private static int prodID = 1001;
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param part the part to add to partList Array
     */
    public static void addPart(Part part){
        allParts.add(part);
    }

    /**
     * @param product the product to add to productList Array
     */
    public static void addProduct(Product product){
        allProducts.add(product);
    }

    /**
     * @return the partList Array
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return the productList Array
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * @return the partID and increment by 1
     */
    public static int getPartID() {
        return partID++;
    }

    /**
     * @return the prodID and increment by 1
     */
    public static int getProdID() {
        return prodID++;
    }
}
