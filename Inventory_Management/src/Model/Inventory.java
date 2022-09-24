package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * @author Henry Ventura
 */
public class Inventory {
    final private static ObservableList<Part> partList = FXCollections.observableArrayList();
    final private static ObservableList<Product> productList = FXCollections.observableArrayList();

    public static int newPartId = 1;
    public static int newProdId = 1;

    /**
     * Default constructor
     */
    public Inventory(){
    }

    /**
     * Adds a part to partList
     * Automatically increments a unique Id to partId
     * @param newPart
     */
    public static void addPart(Part newPart){
        partList.add(newPart);
        newPartId++;
    }

    /**
     * A loop that looks through partList for a matching partID
     * Error if no match found
     * @param partId
     * @return
     */
    public static Part lookupPart(int partId){
        Part foundPart = null;
        for(Part a: partList){
            if (a.getId() == partId) {
                foundPart = a;
            }
        }
        if(foundPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Part not found.");
            alert.show();
        }
        return foundPart;
    }

    /**
     * A loop that looks through partList for a matching name
     * Error if no match found
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        for(Part b: partList){
            if (b.getName().toLowerCase().contains(partName.toLowerCase())){
                filteredParts.add(b);
            }
        }
        if(filteredParts.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Part not found.");
            alert.show();
        }
        return filteredParts;
    }

    /**
     * The selected part is updated.
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        partList.set(index, selectedPart);
    }

    /**
     * Deletes the highlighted part from partList.
     * A confirm dialogue is displayed and asks
     * the user to confirm deletion.
     * @param selectedPart
     */
    public static void DeletePart(Part selectedPart) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to delete\n" + "'" + selectedPart.getName() + "'" +
                " from the parts table?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            partList.remove(selectedPart);
            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
            confirm.setHeaderText("Deleted");
            confirm.setContentText(selectedPart.getName() + " has been deleted.");
            confirm.show();
        }
    }

    /**
     * Returns the ObservableList of all of the parts.
     * @return
     */
    public static ObservableList<Part> getPartList() {
        return partList;
    }

    /**
     * Adds a part to productList
     * Automatically increments a unique Id to productId
     * @param newProd
     */
    public static void addProduct(Product newProd) {
        productList.add(newProd);
        newProdId++;
    }

    /**
     * A loop that looks through productList for a matching Id
     * Error if no match found
     * @param productId
     * @return
     */
    public static Product LookupProduct(int productId) {
        Product foundProd = null;
        for(Product a: productList){
            if (a.getId() == productId){
                foundProd = a;
            }
        }
        if(foundProd == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Product not found.");
            alert.show();
        }
        return foundProd;
    }

    /**
     * A loop that looks through partList for a matching name.
     * An error pops up, if not match was found.
     * @param productName
     * @return
     */
    public static ObservableList<Product> LookupProduct(String productName) {
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        for(Product b: productList){
            if(b.getName().toLowerCase().contains(productName.toLowerCase())){
                filteredProducts.add(b);
            }
        }
        if(filteredProducts.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Product not found.");
            alert.show();
        }
        return filteredProducts;
    }

    /**
     * The selected product is updated.
     * @param index
     * @param selectedProduct
     */
    public static void updateProduct(int index, Product selectedProduct){
        productList.set(index, selectedProduct);
    }

    /**
     * Deletes the highlighted part from productList.
     * An error occurs if product has associated part.
     * A confirm dialogue is displayed and asks
     * the user to confirm deletion.
     *
     * An error I encountered was using JOptionPane instead of Alert.
     * JOptionPane was not showing the confirm delete dialogue sometimes.
     * This was fixed by using an alert dialogue.
     * @param selectedProduct
     */
    public static void DeleteProduct(Product selectedProduct) {
        int currentAssociation = selectedProduct.getAssociatedParts().size();
        if (currentAssociation <= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to delete\n" + "'" + selectedProduct.getName() + "'" +
                    " from the products table?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                productList.remove(selectedProduct);
                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                confirm.setHeaderText("Deleted");
                confirm.setContentText(selectedProduct.getName() + " has been deleted.");
                confirm.show();
            }
        }else {
            Alert delete = new Alert(Alert.AlertType.ERROR);
            delete.setHeaderText("Product not deleted");
            delete.setContentText("This product has associated parts.\n" +
                    "Remove all associated parts from this product and try again.\n");
            delete.show();
        }
    }

    /**
     * Returns the ObservableList of all of the products.
     * @return
     */
    public static ObservableList<Product> getProductList() {
        return productList;
    }
}
