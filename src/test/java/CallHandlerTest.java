import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CallHandlerTest {

    @Test
    public void callForwardedToEmployee() throws InterruptedException {
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
        Call call = new Call();
        ConcurrentLinkedDeque<Call> calls = new ConcurrentLinkedDeque<>();
        calls.push(call);
        List<Representative> representatives = new ArrayList<>();
        Representative manager = new Representative(Representative.Rank.MANAGER, 1);
        Representative supervisor = new Representative(Representative.Rank.SUPERVISOR, 2);
        Representative employee = new Representative(Representative.Rank.EMPLOYEE, 3);
        representatives.add(supervisor);
        representatives.add(manager);
        representatives.add(employee);
        CallHandler callHandler = new CallHandler(calls, representatives);
        supervisor.setProblemSolvingSklls(100);
        employee.setBusy();
        call.setCallComplexity(0);
        callHandler.findCallHandler();
        Assert.assertEquals(0, employee.getCountOfHandledCalls());
        Assert.assertEquals(1, supervisor.getCountOfHandledCalls());
        Assert.assertEquals(0, manager.getCountOfHandledCalls());
    }
    @Test
    public void callForwardedToManagerWhenAllOtherRepsAreBusy() throws InterruptedException {
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
        Assert.assertEquals(0, employee.getCountOfHandledCalls());
        Assert.assertEquals(0, supervisor.getCountOfHandledCalls());
        Assert.assertEquals(1, manager.getCountOfHandledCalls());
    }
    @Test
    public void callForwardedToSupervisorIfEmployeeCannotHandle() throws InterruptedException {
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
        Assert.assertEquals(1, supervisor.getCountOfHandledCalls());
    }
    @Test
    public void callForwardedToManagerIfSupervisorCannotHandle() throws InterruptedException {
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
        Assert.assertEquals(0, employee.getCountOfHandledCalls());
        Assert.assertEquals(0, supervisor.getCountOfHandledCalls());
        Assert.assertEquals(1, manager.getCountOfHandledCalls());
    }
}
