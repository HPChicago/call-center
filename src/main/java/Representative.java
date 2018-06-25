public class Representative {

    /** FIELDS */

    private boolean isAvailable;
    private int employeeId;
    private String currentRank;

    /** CONSTRUCTORS */

    public Representative(Representative.Rank rank, int employeeId) {
        this.currentRank = rank.toString();
        this.employeeId = employeeId;
        this.setFree();
    }

    /** GETTERS & SETTERS */

    public String getCurrentRank() { return currentRank; }

    public boolean isAvailable() { return isAvailable; }

    public void setFree(){ this.isAvailable = true; }

    public void setBusy(){
        this.isAvailable = false;
    }

    public int getEmployeeId() { return employeeId; }

    public void setCanHandle(boolean canHandle) {
        this.isAvailable = canHandle;
    }

    /** METHODS */

    @Override
    public String toString() {
        return "Representative{" +
                "isAvailable=" + isAvailable +
                ", employeeId='" + employeeId + '\'' +
                ", currentRank='" + currentRank + '\'' +
                '}';
    }

    /** ENUMS */

    public enum Rank {
        EMPLOYEE,
        SUPERVISOR,
        MANAGER
    }

}
