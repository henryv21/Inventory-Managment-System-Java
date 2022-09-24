package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import static Model.Inventory.newPartId;

/**
 * TODO:
 *  * The ability to add a part from the modify product form
 *  * would prove useful. It would be a quicker process to add
 *  * an associated part if you're not forced to go back to the main menu
 *  * and then the add part form.
 */
public class AddPartController{

    @FXML final ToggleGroup partType = new ToggleGroup();

    @FXML public RadioButton InHouseRb = new RadioButton();

    @FXML public RadioButton OutsourcedRb = new RadioButton();

    @FXML public Label newPartIdLbl;
    @FXML public Label newPartNameLbl;
    @FXML public Label newPartPriceLbl;
    @FXML public Label newPartStockLbl;
    @FXML public Label newPartMinLbl;
    @FXML public Label newPartMaxLbl;
    @FXML public Label newPartVariantLbl;

    @FXML public TextField newPartIdInput;
    @FXML public TextField newPartNameInput;
    @FXML public TextField newPartPriceInput;
    @FXML public TextField newPartStockInput;
    @FXML public TextField newPartMinInput;
    @FXML public TextField newPartMaxInput;
    @FXML public TextField newPartVariantInput;

    private boolean InHouseBool;

    /**
     * Buttons are added to the toggle group when the scene initializes
     */
    public void initialize(){
        InHouseRb.setToggleGroup(partType);
        OutsourcedRb.setToggleGroup(partType);
        newPartIdInput.setText(String.valueOf(newPartId));
    }

    /**
     * Either MachineId or Company name is shown in the text field
     * This is based on which radio button is selected
     */
    public void ToggleType() {
        if (InHouseRb.isSelected()){
            newPartVariantLbl.setText("MachineId");
        } else if (OutsourcedRb.isSelected()){
            newPartVariantLbl.setText("Company Name");
        }
    }

    /**
     * Returns back to the main menu.
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
     * @return boolean
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
            alert.setContentText("Inventory must be between Minimum and Maximum");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * Validate fields to ensure they have values.
     */

    public void ValidateField(){
        if (newPartNameInput.getText().isEmpty() ||
                newPartStockInput.getText().isEmpty() ||
                newPartPriceInput.getText().isEmpty() ||
                newPartMinInput.getText().isEmpty() ||
                newPartMaxInput.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Missing Information");
            alert.setContentText("All fields are required.");
            alert.show();
            return;
        }
    }

    /**
     * Creates the part if no errors were found
     * @throws Exception
     */
    public void SaveBtn() throws Exception{
        ValidateField();

        if (InHouseRb.isSelected()){
            InHouseBool = true;
        } else if (OutsourcedRb.isSelected()){
            InHouseBool = false;
        }


        String name = newPartNameInput.getText();
        double price = Double.parseDouble(newPartPriceInput.getText());
        int stock = Integer.parseInt(newPartStockInput.getText());
        int min = Integer.parseInt(newPartMinInput.getText());
        int max = Integer.parseInt(newPartMaxInput.getText());

        if(!CheckValues(stock, min, max)){
            return;
        }

        if (InHouseBool){
            int machineId = Integer.parseInt(newPartVariantInput.getText());
            Inventory.addPart(new InHouse(newPartId, name, price, stock, min, max, machineId));
        } else {
            String companyName = newPartVariantInput.getText();
            Inventory.addPart(new OutSourced(newPartId, name, price, stock, min, max, companyName));
        }

        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainMenu.fxml"));
        Main.stage.getScene().setRoot(backHome);
        Main.stage.setTitle("Inventory Management System");
    }

}
