import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Call {

    /** FIELDS */

    private String callId;
    private int callComplexity;

    /** CONSTRUCTORS */

    public Call(){
        this.setCallId();
        this.setCallComplexity();
    }

    /** GETTERS & SETTERS */

    private void setCallComplexity(){
        List<Integer> fibonacciSequence = new ArrayList<>();
        int n1=0,n2=1,n3,i,count=7;
        for(i=2;i<count;++i)//loop starts from 2 because 0 and 1 are already printed
        {
            n3=n1+n2;
            n1=n2;
            n2=n3;
            fibonacciSequence.add(n3);
        }
        Random rand = new Random();
        int index = rand.nextInt(fibonacciSequence.size() + 0);
        this.callComplexity = fibonacciSequence.get(index);
    }

    private void setCallId(){
        Random rand = new Random();
        this.callId = String.valueOf(rand.nextInt(99999) + 1111);
    }

    public int getCallComplexity() {
        return callComplexity;
    }

    public String getCallId() {
        return callId;
    }

    /** METHODS */

    @Override
    public String toString() {
        return "Call{" +
                "callId='" + callId + '\'' +
                ", callComplexity=" + callComplexity +
                '}';
    }
}
