import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class CallHandler implements Runnable {

    Object key = new Object();

    private static final Logger logger = LoggerFactory.getLogger(CallHandler.class);

    /**
     * FIELDS
     */

    private volatile List<Representative> repsOnDuty;
    public volatile ConcurrentLinkedDeque<Call> receivedCalls;
    public ArrayList<Thread> arrThreads = new ArrayList<>();

    /**
     * CONSTRUCTORS
     */

    public CallHandler(ConcurrentLinkedDeque<Call> receivedCalls, List<Representative> repsOnDuty) {
        this.repsOnDuty = repsOnDuty;
        this.receivedCalls = receivedCalls;
    }

    public CallHandler() {
    }

    /**
     * METHODS
     */

    @Override
    public void run() {
        try {
            findCallHandler();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void findCallHandler() throws InterruptedException {
        while (receivedCalls.size() != 0) {
            List<Representative> availableEmployees = repsOnDuty.stream().filter(representative -> representative.isAvailable() == true).collect(Collectors.toList());
            logger.debug("Available employees : " + availableEmployees);
            Optional<Representative> rep = availableEmployees.stream().filter(representative -> representative.getCurrentRank() == Representative.Rank.EMPLOYEE.toString()).findAny();
            if (rep.isPresent() && receivedCalls.peekFirst().getCallLevel() != 3 && receivedCalls.peekFirst().getCallLevel() != 2) {
                logger.info("Connecting Call# " + receivedCalls.peek().getCallId() + " with: " + rep.get().getCurrentRank() + " ID:" + rep.get().getEmployeeId() + " Complexity of the problem is " + receivedCalls.peek().getCallComplexity() + " points");
                rep.get().setBusy();
                OngoingCall ongoingCall = new OngoingCall(rep.get(), receivedCalls.pollFirst(), receivedCalls);
                Thread thread = new Thread(ongoingCall);
                arrThreads.add(thread);
                thread.start();
            } else {
                logger.debug("No Employees available...Looking for Supervisor");
                rep = availableEmployees.stream().filter(representative -> representative.getCurrentRank() == Representative.Rank.SUPERVISOR.toString()).findAny();
                if (rep.isPresent() && receivedCalls.peekFirst().getCallLevel() != 3) {
                    logger.info("Connecting Call# " + receivedCalls.peek().getCallId() + " with: " + rep.get().getCurrentRank() + " ID:" + rep.get().getEmployeeId() + " Complexity of the problem is " + receivedCalls.peek().getCallComplexity() + " points");
                    logger.debug("Supervisor found");
                    rep.get().setBusy();
                    OngoingCall ongoingCall = new OngoingCall(rep.get(), receivedCalls.pollFirst(), receivedCalls);
                    Thread thread = new Thread(ongoingCall);
                    arrThreads.add(thread);
                    thread.start();
                } else {
                    logger.debug("No Supervisor available...Looking for Manager");
                    rep = availableEmployees.stream().filter(representative -> representative.getCurrentRank() == Representative.Rank.MANAGER.toString()).findAny();
                    if (rep.isPresent()) {
                        logger.info("Connecting Call# " + receivedCalls.peek().getCallId() + " with: " + rep.get().getCurrentRank() + " ID:" + rep.get().getEmployeeId() + " Complexity of the problem is " + receivedCalls.peek().getCallComplexity() + " points");
                        logger.debug("Manager found");
                        rep.get().setBusy();
                        OngoingCall ongoingCall = new OngoingCall(rep.get(), receivedCalls.pollFirst(), receivedCalls);
                        Thread thread = new Thread(ongoingCall);
                        arrThreads.add(thread);
                        thread.start();
                    } else {
                        //logger.info("Please wait for the next available representative...");
                        Thread.sleep(10000);
                        findCallHandler();
                    }
                }
            }
        }
        List<Representative> availableEmployees = repsOnDuty.stream().filter(representative -> representative.isAvailable() == true).collect(Collectors.toList());
        if (availableEmployees.size() != repsOnDuty.size()) {
            Thread.sleep(10000);
            if (receivedCalls.size() != 0) {
                System.out.println("Calls in the queue: " + receivedCalls.size() + "...");
                findCallHandler();
            } else {
                for (int i = 0; i < arrThreads.size(); i++) {
                    arrThreads.get(i).join();
                }
                logger.info("The size of the queue is: " + receivedCalls.size());
                logger.info("All calls have been processed successfully");
            }
        }
    }

    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}