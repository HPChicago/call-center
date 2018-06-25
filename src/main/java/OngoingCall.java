/**
 * OngoingCall Class controls flow of the current call.
 * @author Gregory Povorozniuk
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

class OngoingCall extends CallHandler implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(OngoingCall.class);

    /** FIELDS */

    volatile Representative representative;
    volatile Call call;
    private static final int MINUTE = 60000;
    private int callLength;

    /** CONSTRUCTORS */

    public OngoingCall(Representative representative, Call call) {
        this.representative = representative;
        this.call = call;
        this.setLengthOfTheCall();
    }

    /** SETTERS */

    private void setLengthOfTheCall(){
        Random rand = new Random();
        switch (call.getCallComplexity()){
            case 1:
                this.callLength = rand.nextInt(MINUTE/3 + 10000);
                break;
            case 2:
                this.callLength = rand.nextInt(MINUTE/2 + 10000);
                break;
            case 3:
                this.callLength = rand.nextInt(MINUTE + 10000);
                break;
            case 5:
                this.callLength = rand.nextInt(MINUTE*2 + 10000);
                break;
            case 8:
                this.callLength = rand.nextInt(MINUTE*3 + 10000);
                break;
            default:
                this.callLength = rand.nextInt(MINUTE + 10000);
        }
    }

    /** METHODS */

    @Override
    public void run() {
        try {
            logger.info(representative.getCurrentRank() + " ID: " + representative.getEmployeeId()  +" is picking up call# " + call.getCallId());
            Thread.sleep(callLength);
            logger.info(representative.getCurrentRank() + " ID: " + representative.getEmployeeId() + " hanging up after call# " + call.getCallId());
            representative.setFree();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}