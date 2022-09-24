package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static Model.Inventory.getPartList;
import static Model.Inventory.lookupPart;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controls all functions of adding a product scene.
 */
public class AddProductController implements Initializable {
    public Product newProduct = new Product();

    @FXML public TextField partSearchInput;

    @FXML public TableView<Part> associatedPartsTable;
    @FXML public TableView<Part> availablePartsTable;
    @FXML public TableColumn<Part, Integer> partId;
    @FXML public TableColumn<Part, String> partName;
    @FXML public TableColumn<Part, Integer> partStock;
    @FXML public TableColumn<Part, Double> partPrice;
    @FXML public TableColumn<Part, Integer> availablePartId;
    @FXML public TableColumn<Part, String> availablePartName;
    @FXML public TableColumn<Part, Integer> availablePartStock;
    @FXML public TableColumn<Part, Double> availablePartPrice;

    @FXML public TextField newProductId;
    @FXML public TextField newProductName;
    @FXML public TextField newProductStock;
    @FXML public TextField newProductPrice;
    @FXML public TextField newProductMin;
    @FXML public TextField newProductMax;

    /**
     * Populates available parts table.
     * Ensures that data can be written in the add products table.
     *
     * I was having a problem adding a product to the table. I could go in and type the information.
     * However, on saving the product, nothing would be saved.
     * I fixed this issue by adding this initialize method.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newProductId.setText(String.valueOf(Inventory.newProdId));

        partSearchInput.setFocusTraversable(false);
        availablePartId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        availablePartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        availablePartStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        availablePartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        availablePartsTable.getColumns().setAll(availablePartId,availablePartName,availablePartStock,availablePartPrice);
        availablePartsTable.setItems(Inventory.getPartList());

        partId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        associatedPartsTable.getColumns().setAll(partId,partName,partStock,partPrice);
        associatedPartsTable.setItems(newProduct.getAssociatedParts());
    }

    /**
     * Adds the associated part to the product.
     * An error message is displayed if no part was selected.
     */
    public void AddAssociatedPartBtn(){
        Part currentPart = availablePartsTable.getSelectionModel().getSelectedItem();
        if(currentPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Part not selected");
            alert.setContentText("To continue, select a part.");
            alert.show();
        } else {
            newProduct.addAssociatedPart(currentPart);
        }
    }

    /**
     * Removes the associated part from product.
     * Will display message when part is removed.
     */
    public void RemoveAssociatedPart() {
        Part currentPart = associatedPartsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to remove\n" + "'" + currentPart.getName() + "'"
                + " from being associated with this product?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            newProduct.deleteAssociatedPart(currentPart);
            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
            confirm.setHeaderText("Removed");
            confirm.setContentText(currentPart.getName() + " has been removed.");
            confirm.show();
        }
    }

    /**
     * Cancel button takes the user back to the main menu
     * @throws Exception
     */
    public void CancelBtn() throws Exception {
        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainMenu.fxml"));
        Main.stage.getScene().setRoot(backHome);
        Main.stage.setTitle("Inventory Management System");
    }

    /**
     * Ensures that the inventory values are within minimum and maximum constraints
     * Validates minimum is less than maximum.
     * @param stock
     * @param min
     * @param max
     * @return
     */
    public Boolean CheckValues(int stock, int min, int max) {
        if (min > max){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Minimum must be less than maximum.");
            alert.show();
            return false;
        } else if(min > stock || stock > max){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Inventory must be between minimum and maximum values.");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * Validate fields to ensure they have values.
     */
    public void ValidateField(){
        if (newProductName.getText().isEmpty() ||
                newProductStock.getText().isEmpty() ||
                newProductPrice.getText().isEmpty() ||
                newProductMin.getText().isEmpty() ||
                newProductMax.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Missing Information");
            alert.setContentText("All fields are required.");
            alert.show();
            return;
        }
    }

    /**
     * Creates a new product with input data.
     * @throws Exception
     */
    public void SaveBtn() throws Exception {
        ValidateField();

        int id = Inventory.newProdId;
        String name = newProductName.getText();
        int stock = Integer.parseInt(newProductStock.getText());
        double price = Double.parseDouble(newProductPrice.getText());
        int min = Integer.parseInt(newProductMin.getText());
        int max = Integer.parseInt(newProductMax.getText());
        ObservableList<Part> newProdAssocParts = newProduct.getAssociatedParts();

        if(!CheckValues(stock, min, max)){
            return;
        }

        Inventory.addProduct(new Product(id, name, price, stock, min, max, newProdAssocParts));

        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainMenu.fxml"));
        Main.stage.getScene().setRoot(backHome);
        Main.stage.setTitle("Inventory Management System");
    }

    /**
     * Find parts based off their name or Id.
     * @param keyEvent
     */
    public void SearchPart(KeyEvent keyEvent){
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (partSearchInput.getText() == "") {
                availablePartsTable.setItems(getPartList());
                availablePartsTable.getSelectionModel().clearSelection();
                return;
            }

            int partId;
            String partName;
            Part foundPart = null;
            ObservableList<Part> filteredParts = FXCollections.observableArrayList();
            try {
                partId = Integer.parseInt(partSearchInput.getText());
                foundPart = lookupPart(partId);
                filteredParts.add(foundPart);
            } catch(Exception e) {
                partName = partSearchInput.getText();
                filteredParts = Inventory.lookupPart(partName);
            }

            if (partSearchInput != null) {
                availablePartsTable.setItems(filteredParts);
                availablePartsTable.getSelectionModel().select(foundPart);
            }
        }
    }
}
