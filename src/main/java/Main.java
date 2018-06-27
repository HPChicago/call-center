public class Main {

    public static void main (String...args){

        /** numberOfCalls = Number of calls to be generated
         *numberOfRepresentatives = Number of EMPLOYEES available to handle the call. Excluding SUPERVISOR and MANAGER.
         *Example: numberOfRepresentatives = 2; meaning 2 EMPLOYEES, 1 SUPERVISOR, and 1 MANAGER can handle the calls
         */

        CallCenter callCenter = new CallCenter(6,3);
        callCenter.processCalls();
    }
}
