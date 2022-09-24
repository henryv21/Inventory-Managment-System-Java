package Controller;

import Model.*;

import static Model.Inventory.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls all of the functions of the Main Menu.
 */
public class MainMenuController implements Initializable {
    @FXML public TextField searchPart;
    @FXML public TextField searchProduct;

    @FXML public TableView<Part> partsTable;
    @FXML public TableColumn<Part, Integer> partId;
    @FXML public TableColumn<Part, String> partName;
    @FXML public TableColumn<Part, Double> partPrice;
    @FXML public TableColumn<Part, Integer> partStock;

    @FXML public TableView<Product> productsTable;
    @FXML public TableColumn<Product, Integer> prodId;
    @FXML public TableColumn<Product, String> prodName;
    @FXML public TableColumn<Product, Double> prodPrice;
    @FXML public TableColumn<Product, Integer> prodStock;

    /**
     * Allows the table to be populated
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        productsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable.getColumns().setAll(partId, partName, partStock, partPrice);
        partsTable.setItems(getPartList());

        prodId.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.getColumns().setAll(prodId, prodName, prodStock, prodPrice);
        productsTable.setItems(getProductList());
    }

    /**
     * The selected part is retrieved from partList.
     * @return
     */
    public Part currentPart() {
        Part currentPart = partsTable.getSelectionModel().getSelectedItem();
        if (currentPart == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "A part must be selected.");
            alert.show();
        }
        return currentPart;
    }

    /**
     * When the Add button is pressed the scene changes to the add part scene.
     * @throws Exception
     */
    public void AddPartBtn() throws Exception{
        Parent addPart = FXMLLoader.load(getClass().getResource("../Views/AddPart.fxml"));
        Main.stage.setScene(new Scene(addPart));
        Main.stage.setTitle("Add a Part");
    }

    /**
     * The selected part from partList is passed to the ModifyPartController.
     * The scene changes to the modify part scene.
     * @throws Exception
     */
    public void ModPartBtn() throws Exception{
        Part selectedPart = currentPart();
        ModifyPartController.partIndex = getPartList().indexOf(selectedPart);
        ModifyPartController.partToModify = selectedPart;

        Parent modifyPart = FXMLLoader.load(getClass().getResource("../Views/ModifyPart.fxml"));
        Main.stage.setScene(new Scene(modifyPart));
        Main.stage.setTitle("Modify a Part");
    }

    /**
     * Calls the delete part from the inventory class.
     * The selected part is deleted from partList.
     */
    public void DeletePart(){
        Inventory.DeletePart(currentPart());
    }

    /**
     * The selected product is retrieved from partList.
     * @return
     */
    public Product currentProduct() {
        Product currentProduct = productsTable.getSelectionModel().getSelectedItem();
        if (currentProduct == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must select a product to continue.");
            alert.show();
        }
        return currentProduct;
    }

    /**
     * When the Add button is pressed the scene changes to the add product scene.
     * @throws Exception
     */
    public void AddProductBtn() throws Exception{
        Parent addProduct = FXMLLoader.load(getClass().getResource("../Views/AddProduct.fxml"));
        Main.stage.setScene(new Scene(addProduct));
        Main.stage.setTitle("Add a Product");
    }

    /**
     * The selected part from partList is passed to the ModifyProductController.
     * The scene changes to the modify product scene.
     * @throws Exception
     */
    public void ModProductBtn() throws Exception{
        Product selectedProduct = currentProduct();
        ModifyProductController.prodIndex = getProductList().indexOf(selectedProduct);
        ModifyProductController.prodToModify = selectedProduct;

        Pane modifyProduct = FXMLLoader.load(getClass().getResource("../Views/ModifyProduct.fxml"));
        Main.stage.setScene(new Scene(modifyProduct));
        Main.stage.setTitle("Modify a Product");
    }

    /**
     * Calls the delete part from the inventory class.
     * Deletes selected part from productList
     */
    public void DeleteProduct(){
        Inventory.DeleteProduct(currentProduct());
    }

    /**
     * Exits the program
     */
    public void Exit(){
        System.exit(0);
    }

    /**
     * Searches within the part table.
     * Keycode enter is used to search
     * @param keyEvent
     */
    public void searchPart(KeyEvent keyEvent){
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (searchPart.getText() == "") {
                partsTable.setItems(getPartList());
                partsTable.getSelectionModel().clearSelection();
                return;
            }

            int partId;
            String partName;
            Part foundPart = null;
            ObservableList<Part> filteredParts = FXCollections.observableArrayList();
            try {
                partId = Integer.parseInt(searchPart.getText());
                foundPart = lookupPart(partId);
                filteredParts.add(foundPart);
            } catch(Exception e) {
                partName = searchPart.getText();
                filteredParts = lookupPart(partName);
            }

            if (searchPart != null) {
                partsTable.setItems(filteredParts);
                partsTable.getSelectionModel().select(foundPart);
            }
        }
    }

    /**
     * Searches within the product table.
     * Keycode 'ENTER' is used to search
     * @param keyEvent
     */
    public void searchProduct(KeyEvent keyEvent){
        if ( keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (searchProduct.getText() == "") {
                productsTable.setItems(getProductList());
                productsTable.getSelectionModel().clearSelection();
                return;
            }

            int prodId;
            String prodName;
            Product foundProduct = null;
            ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
            try {
                prodId = Integer.parseInt(searchProduct.getText());
                foundProduct = LookupProduct(prodId);
                filteredProducts.add(foundProduct);
            } catch(Exception e) {
                prodName = searchProduct.getText();
                filteredProducts = LookupProduct(prodName);
            }

            if (searchProduct != null) {
                productsTable.setItems(filteredProducts);
                productsTable.getSelectionModel().select(foundProduct);
            }
        }
    }
}
