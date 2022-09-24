package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Henry Ventura
 */
public class Product {
    private String name;

    private int id;
    private int stock;
    private int min;
    private int max;

    private double price;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * This product constructor does not have associated parts
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     */
    public Product(int id, String name, double price, int stock, int min, int max)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * This product constructor has a list of associated parts
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param associatedParts
     */
    public Product(int id, String name, double price, int stock, int min, int max, ObservableList<Part> associatedParts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = associatedParts;
    }

    /**
     * Default constructor
     */
    public Product() {

    }

    /**
     * Set id
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Set name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set price
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Set stock
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Set minimum
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Set maximum
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Return ID
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Return name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Return price
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     * Return stock
     * @return
     */
    public int getStock() {
        return stock;
    }

    /**
     * Return minimum
     * @return
     */
    public int getMin() {
        return min;
    }

    /**
     * Return maximum
     * @return
     */
    public int getMax() {
        return max;
    }

    /**
     * Adds associated part to the associated parts list.
     * @param part
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Removes selected associated part from the associated parts list.
     * @param selectedAssociatedPart
     */
    public void deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Return associated parts list
     * @return
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }
}
