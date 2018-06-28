import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CallHandlerTest {
    private static final Logger logger = LoggerFactory.getLogger(CallHandlerTest.class);

    @Test
    public void callForwardedToEmployee() throws InterruptedException {
        logger.info("Test: Call forwarded to employee");
        Call call = new Call();
        ConcurrentLinkedDeque<Call> calls = new ConcurrentLinkedDeque<>();
        calls.push(call);
        List<Representative> representatives = new ArrayList<>();
        Representative supervisor = new Representative(Representative.Rank.SUPERVISOR, 2);
        Representative manager = new Representative(Representative.Rank.MANAGER, 1);
        Representative employee = new Representative(Representative.Rank.EMPLOYEE, 3);
        representatives.add(supervisor);
        representatives.add(manager);
        representatives.add(employee);
        CallHandler callHandler = new CallHandler(calls, representatives);
        employee.setProblemSolvingSklls(100);
        call.setCallComplexity(0);
        callHandler.findCallHandler();
        Assert.assertEquals(1, employee.getCountOfHandledCalls());
    }
    @Test
    public void callForwardedToSupervisorWhenEmployeesAreBusy() throws InterruptedException {
        logger.info("Test: Call forwarded to supervisor when employees are busy");
        Call call = new Call();
        ConcurrentLinkedDeque<Call> calls = new ConcurrentLinkedDeque<>();
        calls.push(call);
        List<Representative> representatives = new ArrayList<>();
        Representative manager = new Representative(Representative.Rank.MANAGER, 1);
        Representative supervisor = new Representative(Representative.Rank.SUPERVISOR, 2);
        Representative employee = new Representative(Representative.Rank.EMPLOYEE, 3);
        Representative employee2 = new Representative(Representative.Rank.EMPLOYEE, 4);
        representatives.add(supervisor);
        representatives.add(manager);
        representatives.add(employee);
        representatives.add(employee2);
        CallHandler callHandler = new CallHandler(calls, representatives);
        supervisor.setProblemSolvingSklls(100);
        employee.setBusy();
        employee2.setBusy();
        call.setCallComplexity(0);
        callHandler.findCallHandler();
        Assert.assertEquals(0, employee.getCountOfHandledCalls());
        Assert.assertEquals(0, employee2.getCountOfHandledCalls());
    }
    @Test
    public void callForwardedToManagerWhenAllOtherRepsAreBusy() throws InterruptedException {
        logger.info("Test: Call forwarded to manager when all other representatives are busy");
        Call call = new Call();
        ConcurrentLinkedDeque<Call> calls = new ConcurrentLinkedDeque<>();
        calls.push(call);
        List<Representative> representatives = new ArrayList<>();
        Representative employee = new Representative(Representative.Rank.EMPLOYEE, 3);
        Representative manager = new Representative(Representative.Rank.MANAGER, 1);
        Representative supervisor = new Representative(Representative.Rank.SUPERVISOR, 2);
        representatives.add(supervisor);
        representatives.add(manager);
        representatives.add(employee);
        CallHandler callHandler = new CallHandler(calls, representatives);
        employee.setBusy();
        supervisor.setBusy();
        call.setCallComplexity(0);
        callHandler.findCallHandler();
        Assert.assertEquals(1, manager.getCountOfHandledCalls());
    }
    @Test
    public void callForwardedToSupervisorIfEmployeeCannotHandle() throws InterruptedException {
        logger.info("Test: Call forwarded to supervisor if employees cannot handle the call");
        Call call = new Call();
        ConcurrentLinkedDeque<Call> calls = new ConcurrentLinkedDeque<>();
        calls.push(call);
        List<Representative> representatives = new ArrayList<>();
        Representative supervisor = new Representative(Representative.Rank.SUPERVISOR, 2);
        Representative manager = new Representative(Representative.Rank.MANAGER, 1);
        Representative employee = new Representative(Representative.Rank.EMPLOYEE, 3);
        representatives.add(supervisor);
        representatives.add(manager);
        representatives.add(employee);
        CallHandler callHandler = new CallHandler(calls, representatives);
        employee.setProblemSolvingSklls(0);
        supervisor.setProblemSolvingSklls(100);
        call.setCallComplexity(0);
        callHandler.findCallHandler();
        Assert.assertEquals(0, employee.getCountOfHandledCalls());
    }
    @Test
    public void callForwardedToManagerIfSupervisorCannotHandle() throws InterruptedException {
        logger.info("Test: Call forwarded to manager if supervisor cannot handle");
        Call call = new Call();
        ConcurrentLinkedDeque<Call> calls = new ConcurrentLinkedDeque<>();
        calls.push(call);
        List<Representative> representatives = new ArrayList<>();
        Representative supervisor = new Representative(Representative.Rank.SUPERVISOR, 2);
        Representative manager = new Representative(Representative.Rank.MANAGER, 1);
        Representative employee = new Representative(Representative.Rank.EMPLOYEE, 3);
        representatives.add(supervisor);
        representatives.add(manager);
        representatives.add(employee);
        CallHandler callHandler = new CallHandler(calls, representatives);
        supervisor.setProblemSolvingSklls(0);
        call.setCallComplexity(0);
        call.elevateCallLevel();
        callHandler.findCallHandler();
        Assert.assertEquals(0, supervisor.getCountOfHandledCalls());
    }
}
