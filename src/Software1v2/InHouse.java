/**
 * @author Jessie
 */

package Software1v2;

public class InHouse extends Part {
    // stores the parts machine ID
    private int machineId;

    // Constructor for a Part that includes a machine ID
    public InHouse(int id, String name, double price, int stock, int min, int max , int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return Software1.InHouse instance machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId set the instances machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
