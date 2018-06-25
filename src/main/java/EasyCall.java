import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EasyCall extends CallHandler implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(EasyCall.class);

    volatile Representative representative;
    volatile Call call;

    public EasyCall(Representative representative, Call call) {
        this.representative = representative;
        this.call = call;
    }

    @Override
    public void run() {
        logger.info("Rep: " + representative.getEmployeeId() + " Rank: "+ representative.getCurrentRank() + "  is on a call# " + call.getCallId());
        try {
            Thread.sleep(10000);
            representative.setFree();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}