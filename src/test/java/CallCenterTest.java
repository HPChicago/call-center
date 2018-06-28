
import org.junit.Test;

public class CallCenterTest {

    @Test(expected = IllegalArgumentException.class)
    public void requiresMoreThanOneCall(){
        CallCenter callCenter = new CallCenter(0,1);
    }
    @Test(expected = IllegalArgumentException.class)
    public void requiresMoreThanOneEmployee(){
        CallCenter callCenter = new CallCenter(10,0);
    }
}
