/**
 * OngoingCall Class controls flow of the current call.
 *
 * @author Gregory Povorozniuk
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

class OngoingCall extends CallHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OngoingCall.class);

    /** FIELDS */

    volatile Representative representative;
    volatile Call call;
    private static final int MINUTE = 60000;
    private int callLength;
    public ConcurrentLinkedDeque<Call> receivedCalls;

    /** CONSTRUCTORS */

    public OngoingCall(Representative representative, Call call, ConcurrentLinkedDeque<Call> receivedCalls) {
        this.receivedCalls = receivedCalls;
        this.representative = representative;
        this.call = call;
        this.setLengthOfTheCall();

    }

    /** SETTERS */

    private void setLengthOfTheCall() {
        Random rand = new Random();
        switch (call.getCallComplexity()) {
            case 0:
                this.callLength = 0; //for testing purposes
            case 1:
                this.callLength = rand.nextInt(MINUTE / 3 + 10000);
                logger.info("Expected length of a call Id: " + call.getCallId() + " is: " + getExpectedLengthOfTheCall(callLength));
                break;
            case 2:
                this.callLength = rand.nextInt(MINUTE / 2 + 10000);
                logger.info("Expected length of a call Id: " + call.getCallId() + " is: " + getExpectedLengthOfTheCall(callLength));
                break;
            case 3:
                this.callLength = rand.nextInt(MINUTE + 10000);
                logger.info("Expected length of a call Id: " + call.getCallId() + " is: " + getExpectedLengthOfTheCall(callLength));
                break;
            case 5:
                this.callLength = rand.nextInt(MINUTE * 2 + 10000);
                logger.info("Expected length of a call Id: " + call.getCallId() + " is: " + getExpectedLengthOfTheCall(callLength));
                break;
            case 8:
                this.callLength = rand.nextInt(MINUTE * 3 + 10000);
                logger.info("Expected length of a call Id: " + call.getCallId() + " is: " + getExpectedLengthOfTheCall(callLength));
                break;
            default:
                this.callLength = rand.nextInt(MINUTE + 10000);
                logger.info("Expected length of a call Id: " + call.getCallId() + " is: " + getExpectedLengthOfTheCall(callLength));
        }
    }

    /** METHODS */

    public String getExpectedLengthOfTheCall(int ms) {
        int time = ms / 60000;
        if (time == 0) {
            return "less than a minute";
        } else if (time == 1) {
            return "1 minute";
        } else {
            String mn = String.valueOf(time);
            return mn + " minutes";
        }
    }

    @Override
    public void run() {
        try {
            logger.info("Call Id: " + call.getCallId() + " current call level: " + call.getCallLevel());
            logger.info(representative.getCurrentRank() + " ID: " + representative.getEmployeeId() + " is picking up call# " + call.getCallId());
            Thread.sleep(callLength/2); //decided to divide wait in 2 to save time
            logger.info(representative.getCurrentRank() + " ID: " + representative.getEmployeeId() + " hanging up after call# " + call.getCallId());
            representative.setFree();
            logger.info(representative.getCurrentRank()+ " ID: " + representative.getEmployeeId() + " is free to pick up other calls");
            call.setHandled(didRepresentativeHandleTheCall(representative));
                if (call.isHandled()) {
                    representative.increaseCountOfHandledCalls();
                    logger.info("Call Id: " + call.getCallId() + " Problem Solved: > " + call.isHandled());
                } else {
                    logger.info("Call Id: " + call.getCallId() + " Problem Solved: < " + call.isHandled() + " >");
                    call.elevateCallLevel();
                    logger.info("Call Id: " + call.getCallId() + " current call level: " + call.getCallLevel());
                    logger.info("Call Id: " + call.getCallId() + " is elevated to level: " + call.callElevatedTo());
                    receivedCalls.addFirst(call);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean didRepresentativeHandleTheCall(Representative representative) {
        int currentCallProblemSolvingSkills = generateRandomNumber(30, 100);
        if (currentCallProblemSolvingSkills <= representative.getProblemSolvingSkills()) {
            return true;
        } else {
            return false;
        }
    }


}