import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class CallHandler implements Runnable{

    Object key = new Object();

    private static final Logger logger = LoggerFactory.getLogger(CallHandler.class);

    private volatile List<Representative> repsOnDuty;
    private volatile ConcurrentLinkedDeque<Call> receivedCalls;


    public CallHandler(ConcurrentLinkedDeque<Call> receivedCalls, List<Representative>repsOnDuty ) {
        this.repsOnDuty = repsOnDuty;
        this.receivedCalls = receivedCalls;
    }

    public CallHandler() {
    }
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
                if(rep.isPresent()){
                    System.out.println("Connecting Call# " + receivedCalls.peek().getCallId() + " with the representative of rank: " + rep.get().getCurrentRank() + " Id:" + rep.get().getEmployeeId() + " Complexity of the problem is " + receivedCalls.peek().getCallComplexity() + " points");
                    rep.get().setBusy();
                    EasyCall easyCall = new EasyCall(rep.get(), receivedCalls.pollFirst());
                    new Thread(easyCall).start();
                        if(receivedCalls.size()!=0){ findCallHandler(); }
                        else{ break; }
                }
                else{
                    System.out.println("No Employees available...Looking for Supervisor");
                    rep = availableEmployees.stream().filter(representative -> representative.getCurrentRank() == Representative.Rank.SUPERVISOR.toString()).findAny();
                    if(rep.isPresent()){
                        System.out.println("Supervisor found");
                        rep.get().setBusy();
                        EasyCall easyCall = new EasyCall(rep.get(), receivedCalls.pollFirst());
                        new Thread(easyCall).start();
                        if(receivedCalls.size()!=0){ findCallHandler(); }
                        else{ break; }
                    }
                    else{
                        System.out.println("No Supervisor available...Looking for Supervisor");
                        rep = availableEmployees.stream().filter(representative -> representative.getCurrentRank() == Representative.Rank.MANAGER.toString()).findAny();
                        if (rep.isPresent()){
                            System.out.println("Manager found");
                            rep.get().setBusy();
                            EasyCall easyCall = new EasyCall(rep.get(), receivedCalls.pollFirst());
                            new Thread(easyCall).start();
                            if(receivedCalls.size()!=0){ findCallHandler(); }
                            else{ break; }
                        }
                        else{
                            System.out.println("Everyone is busy");
                            Thread.sleep(10000);
                            findCallHandler();
                        }
                    }
                }
           //System.out.println(receivedCalls.pollFirst());
        }
//        for(int i = 0; i < 4; i++){
//            for (Representative repOnDuty : repsOnDuty){

//                if(repOnDuty.getCurrentRank() == Representative.Rank.EMPLOYEE.toString() ){
//                    if(repOnDuty.isAvailable()) {
//                        repOnDuty.setBusy();
//                        if(receivedCalls.peekFirst().getCallComplexity()>3){
//                            HardCall hardCall = new HardCall(repOnDuty, receivedCalls.pollFirst());
//                            new Thread(hardCall).start();
//                            if(receivedCalls.size()!=0){
//                                findCallHandler();
//                            }
//                            else{
//                                break;
//                            }
//                        }
//                        else{
//                            EasyCall easyCall = new EasyCall(repOnDuty, receivedCalls.pollFirst());
//                            new Thread(easyCall).start();
//                            if(receivedCalls.size()!=0){
//                                findCallHandler();
//                            }
//                            else{
//                                break;
//                            }
//                        }
//
//
//                    }
//                }
//            }
//        }

        logger.info("AFTER" + repsOnDuty.toString());
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