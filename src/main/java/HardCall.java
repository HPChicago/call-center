import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HardCall extends CallHandler implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(HardCall.class);

    volatile Representative representative;
    volatile Call call;

    public HardCall(Representative representative, Call call) {
        this.representative = representative;
        this.call = call;
    }

    @Override
    public void run() {
        logger.info("Rep: " + representative.getEmployeeId() + " Rank: "+ representative.getCurrentRank() + "  is on a call# " + call.getCallId());
        try {
            Thread.sleep(30000);
            representative.setFree();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
