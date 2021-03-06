/**
 *  Copyright (C) 2000-2012 The Software Conservancy as Trustee.
 *  All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to
 *  deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *  IN THE SOFTWARE.
 *
 *  Nothing in this notice shall be deemed to grant any rights to trademarks,
 *  copyrights, patents, trade secrets or any other intellectual property of the
 *  licensor or any contributor except as expressly stated herein. No patent
 *  license is granted separate from the Software, for code that you delete from
 *  the Software, or for combinations of the Software with other software or
 *  hardware.
 */
package org.chorusbdd.chorus;

import org.chorusbdd.chorus.core.interpreter.ChorusExecutionListener;
import org.chorusbdd.chorus.executionlistener.SystemOutExecutionListener;
import org.chorusbdd.chorus.remoting.jmx.DynamicProxyMBeanCreator;
import org.chorusbdd.chorus.remoting.jmx.RemoteExecutionListenerMBean;
import org.chorusbdd.chorus.util.logging.ChorusLog;
import org.chorusbdd.chorus.util.logging.ChorusLogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 16/05/12
 * Time: 20:33
 *
 * Create the appropriate execution listeners, based on system paramters and switches passed
 * to the interpreter
 */
public class ExecutionListenerFactory {

    private static ChorusLog log = ChorusLogFactory.getLog(Main.class);

    public List<ChorusExecutionListener> createExecutionListener(Map<String, List<String>> parsedArgs) {
        List<ChorusExecutionListener> result = new ArrayList<ChorusExecutionListener>();
        if ( parsedArgs.containsKey("jmxListener")) {
            //we can have zero to many remote jmx execution listeners available
            addProxyForRemoteJmxListener(parsedArgs, result);
        }

        addSystemOutExecutionListener(parsedArgs, result);
        return result;
    }

    private void addSystemOutExecutionListener(Map<String, List<String>> parsedArgs, List<ChorusExecutionListener> result) {
        boolean verbose = parsedArgs.containsKey("verbose");
        boolean showSummary = parsedArgs.containsKey("showsummary");
        result.add(new SystemOutExecutionListener(showSummary, verbose));
    }

    private void addProxyForRemoteJmxListener(Map<String, List<String>> parsedArgs, List<ChorusExecutionListener> result) {
        List<String> remoteListenerHostAndPorts = parsedArgs.get("jmxListener");
        for ( String hostAndPort : remoteListenerHostAndPorts ) {
            addRemoteListener(result, hostAndPort);
        }
    }

    private void addRemoteListener(List<ChorusExecutionListener> result, String hostAndPort) {
        try {
            StringTokenizer t = new StringTokenizer(hostAndPort, ":");
            String host = t.nextToken();
            int port = Integer.valueOf(t.nextToken());
            DynamicProxyMBeanCreator h = new DynamicProxyMBeanCreator(host, port);
            h.connect();
            result.add(h.createMBeanProxy(RemoteExecutionListenerMBean.JMX_EXECUTION_LISTENER_NAME, RemoteExecutionListenerMBean.class));
        } catch (Throwable t) {
            if ( ! log.isDebugEnabled() ) {
                log.warn("Failed to create proxy for jmx execution listener at " + hostAndPort);
            } else {
                log.debug("Failed to create proxy for jmx execution listener at " + hostAndPort, t);
            }
        }
    }

}
