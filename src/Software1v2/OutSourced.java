/**
 * @author Jessie OutSourced type of Part
 */

package Software1v2;

public class OutSourced extends Part {
    // store the company name of the OutSourced part
    private String companyName;

    // Constructor for OutSourced part with added company name
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return the instance companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName set the instances companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
