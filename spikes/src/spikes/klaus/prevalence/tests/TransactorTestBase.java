// Author: Klaus Wuestefeld
// I hereby place the contents of this file in the public domain.

package spikes.klaus.prevalence.tests;

import java.io.Serializable;

import junit.framework.TestCase;
import spikes.klaus.prevalence.Transaction;
import spikes.klaus.prevalence.Transactor;


public abstract class TransactorTestBase extends TestCase {

	private static class ExecutionProbe implements Transaction, Serializable {
		private boolean wasExecuted = false;

		public void executeOn(Object businessSystem) {
			assertTrue(!wasExecuted);
			wasExecuted = true;
			
			recordExecution(businessSystem);
		}

		private static final long serialVersionUID = 1L;
	}

	private static class IsolationProbe implements Transaction, Serializable {

		public void executeOn(Object businessSystem) {
			try {
				businessSystem.notify();
			} catch (IllegalMonitorStateException e) {
				fail("Transaction execution must be synchronized on the business system.");
			}
		}

		private static final long serialVersionUID = 1L;
	}

	private static Object EXPECTED_BUSINESS_SYSTEM;
	static private int EXECUTIONS;

	protected Transactor _subject;


	protected abstract Transactor subject();
	
	@Override
	protected void setUp() {
		_subject = subject();
	}
	

	public void testTransactionExecution() {
		synchronized (TransactorTestBase.class) {
			EXPECTED_BUSINESS_SYSTEM = _subject.businessSystem();
			EXECUTIONS = 0;
	
			_subject.execute(new ExecutionProbe());
			_subject.execute(new ExecutionProbe());
			_subject.execute(new ExecutionProbe());
	
			assertEquals(EXECUTIONS, 3);
		}
	}

	static private void recordExecution(Object businessSystem) {
		if (businessSystem == EXPECTED_BUSINESS_SYSTEM) EXECUTIONS++;
	}

	public void testTransactionIsolation() {
		_subject.execute(new IsolationProbe());
	}

}