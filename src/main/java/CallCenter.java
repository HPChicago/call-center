import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CallCenter {

    private static final Logger logger = LoggerFactory.getLogger(CallCenter.class);

    /** FIELDS */

//    private int numberOfCalls;
//    private int numberOfRepresentatives;
    private List<Representative> repsOnDuty;
    private ConcurrentLinkedDeque<Call> receivedCalls;

    /** CONSTRUCTORS */

    public CallCenter(int numberOfCalls, int numberOfRepresentatives) {
//        this.numberOfCalls = numberOfCalls;
//        this.numberOfRepresentatives = numberOfRepresentatives;
        this.repsOnDuty(numberOfRepresentatives);
        //this.receivedCalls(numberOfCalls);
        this.setNumberOfCalls(numberOfCalls);
    }

    public void setNumberOfCalls(int numberOfCalls) {
        if(numberOfCalls < 1){
            throw new IllegalArgumentException("We are going to need more calls than that to get busy :)");
        }
        receivedCalls(numberOfCalls);
    }

    /** GETTERS & SETTERS */



    public List<Representative> repsOnDuty(int numberOfRepresentatives){
        List<Representative> representatives = new ArrayList<>();
        for(int r = 1; r <= numberOfRepresentatives; r++){
            Representative employee = new Representative(Representative.Rank.EMPLOYEE, r + 2);
            representatives.add(employee);
        }
        Representative supervisor = new Representative(Representative.Rank.SUPERVISOR, 2);
        Representative manager = new Representative(Representative.Rank.MANAGER, 1);
        representatives.add(supervisor);
        representatives.add(manager);
        logger.info(representatives.toString());
        this.repsOnDuty = representatives;
        return representatives;
    }

    public ConcurrentLinkedDeque<Call> receivedCalls(int numberOfCalls){
        ConcurrentLinkedDeque<Call> calls = new ConcurrentLinkedDeque<>();
        for(int c = 0; c < numberOfCalls; c++){
            Call call = new Call();
            calls.add(call);
        }
        this.receivedCalls = calls;
        logger.info(calls.toString());
        return calls;
    }

    /** METHODS */

    public void processCalls() {
        CallHandler callHandler = new CallHandler(receivedCalls, repsOnDuty);
        Thread t1 = new Thread(callHandler);
        t1.setDaemon(false);
        t1.start();
        //new Thread(callHandler).start();

    }
}
