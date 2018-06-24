import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class CallHandler {

    Object key = new Object();

    private static final Logger logger = LoggerFactory.getLogger(CallHandler.class);

    Representative representative;
    private volatile List<Representative> repsOnDuty;
    private ConcurrentLinkedDeque<Call> receivedCalls;

    public CallHandler(ConcurrentLinkedDeque<Call> receivedCalls, List<Representative>repsOnDuty ) {
        this.repsOnDuty = repsOnDuty;
        this.receivedCalls = receivedCalls;
    }

    public synchronized void processCalls() throws InterruptedException {
        List<Representative> availableEmployees = repsOnDuty.stream().filter(e -> e.isAvailable() == true).collect(Collectors.toList());
        synchronized (key){
            if(availableEmployees.size() == 0){
                try {
                    key.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(!receivedCalls.isEmpty()){
                System.out.println("Next call is + " + receivedCalls.peekFirst().getCallId());
                findCallHandler();
                key.wait();
            }
            System.out.println("No more calls");

        }
    }

    public void findCallHandler(){
        for (Representative repOnDuty : repsOnDuty){
                if(repOnDuty.getCurrentRank() == Representative.Rank.EMPLOYEE.toString() ){
                    if(repOnDuty.isAvailable()){
                        Runnable handleCall = () -> {
                            try {
                                handleCall(repOnDuty, receivedCalls.pollFirst());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        };
                        Thread thread = new Thread(handleCall);
                        thread.start();


                        //System.out.println(repOnDuty.isAvailable());
                    }
                    else{
                        System.out.println("All employees are busy");
                    }
            }
        }

    }

    public void handleCall(Representative representative, Call call) throws InterruptedException {
        synchronized (key){
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(4000);
            logger.info("Representative with id: " + representative.getEmployeeId() + " and rank " + representative.getCurrentRank() + " is connected to Call# " + call.getCallId() + "...");
            representative.setBusy();
            List<Representative> availableEmployees = repsOnDuty.stream().filter(e -> e.isAvailable() == true).collect(Collectors.toList());
            logger.info("Available operators: " + availableEmployees.size());
            System.out.println(representative);
            key.notify();
        }

    }
}