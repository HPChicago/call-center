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

    public void setBusy(){ this.isAvailable = false; }

    public int getEmployeeId() { return employeeId; }

    public void setCanHandle(boolean canHandle) {
        this.isAvailable = canHandle;
    }

    public void setProblemSolvingSklls(String rank){
        if(currentRank == Rank.EMPLOYEE.toString()){
            this.problemSolvingSkills = generateRandomNumber(85, 95);
        }
        else if(currentRank == Rank.SUPERVISOR.toString()){
            this.problemSolvingSkills = generateRandomNumber(93, 98);
        }
        else if(currentRank == Rank.MANAGER.toString()){
            this.problemSolvingSkills = 100;
        }
    }

    public int getProblemSolvingSkills() {
        return problemSolvingSkills;
    }

    public static int generateRandomNumber (int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
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
