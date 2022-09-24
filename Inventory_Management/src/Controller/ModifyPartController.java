package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls all functions for modifying a part.
 */
public class ModifyPartController implements Initializable {
    public static Part partToModify;

    public static int partIndex;

    private boolean InHouseBool;

    @FXML final ToggleGroup partType = new ToggleGroup();

    @FXML private RadioButton InHouseRb;
    @FXML private RadioButton OutsourcedRb;

    @FXML private TextField modifyPartIdInput;
    @FXML private TextField modifyPartNameInput;
    @FXML private TextField modifyPartPriceInput;
    @FXML private TextField modifyPartStockInput;
    @FXML private TextField modifyPartMinInput;
    @FXML private TextField modifyPartMaxInput;
    @FXML private TextField modifyPartInput;

    @FXML private Label modifyPartLbl;

    /**
     * Allows the table to be populated.
     * Adds radio buttons to toggle group.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InHouseRb.setToggleGroup(partType);
        OutsourcedRb.setToggleGroup(partType);

        modifyPartIdInput.setText(String.valueOf(partToModify.getId()));
        modifyPartNameInput.setText(partToModify.getName());
        modifyPartPriceInput.setText(String.valueOf(partToModify.getPrice()));
        modifyPartStockInput.setText(String.valueOf(partToModify.getStock()));
        modifyPartMinInput.setText(String.valueOf(partToModify.getMin()));
        modifyPartMaxInput.setText(String.valueOf(partToModify.getMax()));

        if (partToModify instanceof InHouse){
            modifyPartInput.setText(String.valueOf(((InHouse) partToModify).getMachineId()));
            InHouseRb.setSelected(true);
        } else {
            modifyPartInput.setText(((OutSourced) partToModify).getCompanyName());
            OutsourcedRb.setSelected(true);
        }
        ToggleType();
    }

    /**
     * Either MachineId or Company name is shown in the text field
     * This is based on which radio button is selected
     */
    public void ToggleType() {
        if (InHouseRb.isSelected()){
            modifyPartLbl.setText("Machine ID");
        } else if (OutsourcedRb.isSelected()){
            modifyPartLbl.setText("Company Name");
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
     * Updates part data with new user input data.
     * @throws Exception
     */
    public void ModifySaveBtn() throws Exception {
        if (InHouseRb.isSelected()){
            InHouseBool = true;
        } else if (OutsourcedRb.isSelected()){
            InHouseBool = false;
        }

        int newId = partToModify.getId();
        String newName = modifyPartNameInput.getText();
        double newPrice = Double.parseDouble(modifyPartPriceInput.getText());
        int newStock = Integer.parseInt(modifyPartStockInput.getText());
        int newMin = Integer.parseInt(modifyPartMinInput.getText());
        int newMax = Integer.parseInt(modifyPartMaxInput.getText());

        if(!CheckValues(newStock, newMin, newMax)){
            return;
        }

        Part newPart;
        if (InHouseBool) {
            int newMachineId = Integer.parseInt(modifyPartInput.getText());
            newPart = new InHouse(newId, newName, newPrice, newStock, newMin, newMax, newMachineId);
        } else {
            String newCompanyName = modifyPartInput.getText();
            newPart = new OutSourced(newId, newName, newPrice, newStock, newMin, newMax, newCompanyName);
        }
        Inventory.updatePart(partIndex, newPart);
        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainMenu.fxml"));
        Main.stage.getScene().setRoot(backHome);
    }

    /**
     * Cancel button takes the user back to the main menu
     * @param actionEvent
     * @throws Exception
     */
    public void ModifyCancelBtn(ActionEvent actionEvent) throws Exception{
        //Go back to main screen.
        Parent mainMenu = FXMLLoader.load(getClass().getResource("../Views/MainMenu.fxml"));
        Main.stage.setTitle("Inventory Management System");
        Main.stage.getScene().setRoot(mainMenu);
    }
}
