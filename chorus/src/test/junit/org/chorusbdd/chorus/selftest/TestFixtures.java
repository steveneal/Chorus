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
package org.chorusbdd.chorus.selftest;

import junit.framework.TestCase;
import org.chorusbdd.chorus.Main;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 11/05/12
 * Time: 22:27
 *
 * A unit test which kicks off the Chorus interpreter and runs the spring-specific
 * fixtures from chorus-spring
 *
 * Run this in forking mode
 *
 * At present I can't find a way to create an idea run config which executes these directly, using the maven test
 * classpath - the only way I can find to make it work is to wrap these in a junit test.
 */
public class TestFixtures extends TestCase {

    @Test
    public void testFixtures() {
        String[] args = new String[] {
            "-verbose", "-showsummary", "-trace", "-f", "src/test/features", "-h", "org.chorusbdd.chorus.selftest.handlers"
        };

        try {
            boolean success = Main.run(args);
            if ( ! success ) {
                fail("Some chorus tests failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed during test execution");
        }
    }
}
