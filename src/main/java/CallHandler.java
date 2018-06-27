import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class CallHandler implements Runnable{

    //Object key = new Object();

    private static final Logger logger = LoggerFactory.getLogger(CallHandler.class);

    /** FIELDS */

    private volatile List<Representative> repsOnDuty;
    public volatile ConcurrentLinkedDeque<Call> receivedCalls;

    /** CONSTRUCTORS */

    public CallHandler(ConcurrentLinkedDeque<Call> receivedCalls, List<Representative>repsOnDuty ) {
        this.repsOnDuty = repsOnDuty;
        this.receivedCalls = receivedCalls;
    }

    public CallHandler(){}

    /** METHODS */

    @Override
    public void run() {
        try {
            findCallHandler();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void findCallHandler() throws InterruptedException {
        while(receivedCalls.size()!= 0){
            List<Representative> availableEmployees = repsOnDuty.stream().filter(representative -> representative.isAvailable() == true).collect(Collectors.toList());
            logger.debug("Available employees : " + availableEmployees);
            Optional<Representative> rep = availableEmployees.stream().filter(representative -> representative.getCurrentRank() == Representative.Rank.EMPLOYEE.toString()).findAny();
                if(rep.isPresent() && receivedCalls.peekFirst().getCallLevel() != 3 && receivedCalls.peekFirst().getCallLevel() != 2){
                  logger.info("Connecting Call# " + receivedCalls.peek().getCallId() + " with: " + rep.get().getCurrentRank() + " ID:" + rep.get().getEmployeeId() + " Complexity of the problem is " + receivedCalls.peek().getCallComplexity() + " points");
                    rep.get().setBusy();
                    //System.out.println("2" + receivedCalls.peek().getCallId() + " < callId - handled? > " +  receivedCalls.peek().isHandled());
                    //System.out.println("My thread is + " + Thread.currentThread().getName());
                    OngoingCall ongoingCall = new OngoingCall(rep.get(), receivedCalls.pollFirst(), receivedCalls);
                    new Thread(ongoingCall).start();
                        if(receivedCalls.size()!=0){ findCallHandler(); }
                        else{ break; }
                }
                else{
                    logger.debug("No Employees available...Looking for Supervisor");
                    rep = availableEmployees.stream().filter(representative -> representative.getCurrentRank() == Representative.Rank.SUPERVISOR.toString()).findAny();
                    if(rep.isPresent() && receivedCalls.peekFirst().getCallLevel() != 3){
                        logger.info("Connecting Call# " + receivedCalls.peek().getCallId() + " with: " + rep.get().getCurrentRank() + " ID:" + rep.get().getEmployeeId() + " Complexity of the problem is " + receivedCalls.peek().getCallComplexity() + " points");
                        logger.debug("Supervisor found");
                        rep.get().setBusy();
                        OngoingCall ongoingCall = new OngoingCall(rep.get(), receivedCalls.pollFirst(), receivedCalls);
                        new Thread(ongoingCall).start();
                        if(receivedCalls.size()!=0){ findCallHandler(); }
                        else{ break; }
                    }
                    else{
                        logger.debug("No Supervisor available...Looking for Supervisor");
                        rep = availableEmployees.stream().filter(representative -> representative.getCurrentRank() == Representative.Rank.MANAGER.toString()).findAny();
                        if (rep.isPresent()){
                            logger.info("Connecting Call# " + receivedCalls.peek().getCallId() + " with: " + rep.get().getCurrentRank() + " ID:" + rep.get().getEmployeeId() + " Complexity of the problem is " + receivedCalls.peek().getCallComplexity() + " points");
                            logger.debug("Manager found");
                            rep.get().setBusy();
                            OngoingCall ongoingCall = new OngoingCall(rep.get(), receivedCalls.pollFirst(), receivedCalls);
                            new Thread(ongoingCall).start();
                            if(receivedCalls.size()!=0){ findCallHandler(); }
                            else{ break; }
                        }
                        else{
                            //logger.info("Please wait for the next available representative...");
                            Thread.sleep(10000);
                            findCallHandler();
                        }
                    }
                }
           //System.out.println(receivedCalls.pollFirst());
        }
    }
//    public void handleCall(Representative representative, Call call) throws InterruptedException {
//        synchronized (key){
//            System.out.println("1" + repsOnDuty);
//            System.out.println(Thread.currentThread().getName());
//            logger.info("Representative with id: " + representative.getEmployeeId() + " and rank " + representative.getCurrentRank() + " is connected to Call# " + call.getCallId() + "...");
//            representative.setBusy();
//            //List<Representative> availableEmployees = repsOnDuty.stream().filter(e -> e.isAvailable() == true).collect(Collectors.toList());
//            //logger.info("Available operators: " + availableEmployees.size());
//            System.out.println(representative);
//            representative.setFree();
//            System.out.println(repsOnDuty);
//            key.notify();
//        }

//    }
}