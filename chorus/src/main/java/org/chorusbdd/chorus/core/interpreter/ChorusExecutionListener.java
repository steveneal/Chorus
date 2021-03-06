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
package org.chorusbdd.chorus.core.interpreter;

import org.chorusbdd.chorus.core.interpreter.results.*;

import java.util.List;

/**
 * Implementors can register with a ChorusInterpreter to recieve callbacks during test execution.
 *
 * Created by: Steve Neal
 * Date: 11/01/12
 */
public interface ChorusExecutionListener {

    /**
     * @param testExecutionToken, a token representing the current suite of tests starting execution
     */
    public void testsStarted(TestExecutionToken testExecutionToken);

    /**
     * @param testExecutionToken, a token representing the current suite of tests
     * @param features a List of features executed
     */
    public void testsCompleted(TestExecutionToken testExecutionToken, List<FeatureToken> features);

    /**
     * @param testExecutionToken, a token representing the current suite of tests running
     * @param feature, a token representing the feature which is starting
     */
    public void featureStarted(TestExecutionToken testExecutionToken, FeatureToken feature);

    /**
     * @param testExecutionToken, a token representing the current suite of tests running
     * @param feature, a token representing the feature which has just completed
     */
    public void featureCompleted(TestExecutionToken testExecutionToken, FeatureToken feature);

    /**
     * @param testExecutionToken, a token representing the current suite of tests running
     * @param scenario, a token representing the scenario which is starting
     */
    public void scenarioStarted(TestExecutionToken testExecutionToken, ScenarioToken scenario);

    /**
     * @param testExecutionToken, a token representing the current suite of tests running
     * @param scenario, a token representing the scenario which has just completed
     */
    public void scenarioCompleted(TestExecutionToken testExecutionToken, ScenarioToken scenario);

    /**
     * @param testExecutionToken, a token representing the current suite of tests running
     * @param step, a token representing the test stop which has just started execution
     */
    public void stepStarted(TestExecutionToken testExecutionToken, StepToken step);

    /**
     * @param testExecutionToken, a token representing the current suite of tests running
     * @param step, a token representing the test stop which has just completed execution
     */
    public void stepCompleted(TestExecutionToken testExecutionToken, StepToken step);

}
