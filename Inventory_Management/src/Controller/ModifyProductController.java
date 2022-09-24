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

import javax.swing.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Inventory.getPartList;

/**
 * Handles all functions for modifying a part
 */
public class ModifyProductController implements Initializable{
    public static int prodIndex;
    public static Product prodToModify;

    @FXML public TextField partSearchInput;

    @FXML public TableView<Part> associatedPartsTable;
    @FXML public TableView<Part> availablePartsTable;
    @FXML public TableColumn<Part, Integer> partId;
    @FXML public TableColumn<Part, String> partName;
    @FXML public TableColumn<Part, Integer> partStock;
    @FXML public TableColumn<Part, Double> partPrice;
    @FXML public TableColumn<Part, Integer> availPartId;
    @FXML public TableColumn<Part, String> availPartName;
    @FXML public TableColumn<Part, Integer> availPartStock;
    @FXML public TableColumn<Part, Double> availPartPrice;

    @FXML public TextField modifyProductId;
    @FXML public TextField modifyProductStock;
    @FXML public TextField modifyProductName;
    @FXML public TextField modifyProductPrice;
    @FXML public TextField modifyProductMin;
    @FXML public TextField modifyProductMax;

    /**
     * Initialize allows the product table to be populated.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partSearchInput.setFocusTraversable(false);

        modifyProductId.setText(String.valueOf(prodToModify.getId()));
        modifyProductName.setText(prodToModify.getName());
        modifyProductStock.setText(String.valueOf(prodToModify.getStock()));
        modifyProductPrice.setText(String.valueOf(prodToModify.getPrice()));
        modifyProductMax.setText(String.valueOf(prodToModify.getMax()));
        modifyProductMin.setText(String.valueOf(prodToModify.getMin()));

        availPartId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        availPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        availPartStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        availPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        availablePartsTable.getColumns().setAll(availPartId, availPartName, availPartStock, availPartPrice);
        availablePartsTable.setItems(Inventory.getPartList());

        partId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        associatedPartsTable.getColumns().setAll(partId, partName, partStock, partPrice);
        associatedPartsTable.setItems(prodToModify.getAssociatedParts());
    }

    /**
     * Adds part to the associated part array
     */
    public void AddAssociatedPartBtn(){
        Part currentPart = availablePartsTable.getSelectionModel().getSelectedItem();

        if(currentPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Part not selected.");
            alert.setContentText("To continue, select a part.");
            alert.show();
        } else {
            prodToModify.addAssociatedPart(currentPart);
        }
    }

    /**
     * Removes associated part from the array.
     * A confirm dialogue is displayed and asks
     * the user to confirm removal.
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
            prodToModify.deleteAssociatedPart(currentPart);
            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
            confirm.setHeaderText("Removed");
            confirm.setContentText(currentPart.getName() + " has been removed.");
            confirm.show();
        }
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
     * Updates product data with new user input data.
     * @throws Exception
     */
    public void SaveBtn() throws Exception {
        int id = Integer.parseInt(modifyProductId.getText());
        String name = modifyProductName.getText();
        int stock = Integer.parseInt(modifyProductStock.getText());
        double price = Double.parseDouble(modifyProductPrice.getText());
        int min = Integer.parseInt(modifyProductMin.getText());
        int max = Integer.parseInt(modifyProductMax.getText());
        ObservableList<Part> modifyProdAssociatedParts = prodToModify.getAssociatedParts();

        if(!CheckValues(stock, min, max)){
            return;
        }

        Inventory.updateProduct(prodIndex, new Product(id, name, price, stock, min, max, modifyProdAssociatedParts));

        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainMenu.fxml"));
        Main.stage.getScene().setRoot(backHome);
        Main.stage.setTitle("Inventory Management System");
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
     * Searches for parts in the table.
     * @param keyEvent
     */
    public void searchPart(KeyEvent keyEvent){
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
                foundPart = Inventory.lookupPart(partId);
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
