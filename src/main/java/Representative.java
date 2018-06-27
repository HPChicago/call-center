import java.util.Random;

public class Representative {

    /** FIELDS */

    private boolean isAvailable;
    private int employeeId;
    private String currentRank;
    private int problemSolvingSkills;

    /** CONSTRUCTORS */

    public Representative(Representative.Rank rank, int employeeId) {
        this.currentRank = rank.toString();
        this.employeeId = employeeId;
        this.setFree();
        this.setProblemSolvingSklls(currentRank);
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

    public void setProblemSolvingSklls(String rank){
        Random random = new Random();
        if(currentRank == Rank.EMPLOYEE.toString()){
            this.problemSolvingSkills = (random.nextInt(92) + 85);
        }
        else if(currentRank == Rank.SUPERVISOR.toString()){
            this.problemSolvingSkills = (random.nextInt(97) + 92);
        }
        else if(currentRank == Rank.MANAGER.toString()){
            this.problemSolvingSkills = 100;
        }
    }

    /** METHODS */

    @Override
    public String toString() {
        return "Representative{" +
                "isAvailable=" + isAvailable +
                ", employeeId=" + employeeId +
                ", currentRank='" + currentRank + '\'' +
                ", problemSolvingSkills=" + problemSolvingSkills +
                '}';
    }

    /** ENUMS */

    public enum Rank {
        EMPLOYEE,
        SUPERVISOR,
        MANAGER
    }

}
