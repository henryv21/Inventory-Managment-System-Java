package Model;

/**
 * @author Henry Ventura
 */

public class OutSourced extends Part {

    private String companyName;

    /**
     * Constructor for Outsourced
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Set company name
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Return company name
     * @return
     */
    public String getCompanyName() {
        return companyName;
    }
}
