package uk.co.smartkey.chorus.remoting;

import uk.co.smartkey.chorus.remoting.jmx.DynamicJmxProxy;
import org.junit.Test;

/**
 * Created by: Steve Neal
 * Date: 12/10/11
 */
public class DynamicMBeanProxyTest {

    @Test(expected = ChorusRemotingException.class)
    public void exceptionForIllegalHost() throws Exception {
        new DynamicJmxProxy("NO-SUCH-HOST", -1, "");
    }

}