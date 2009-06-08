//package spikes.klaus;
//
//import java.io.EOFException;
//import java.io.IOException;
//
//public class PrevaylerJr {
//	private final Object _system;
//	private final AcidOutputStream _journal;
//
//	public PrevaylerJr(Object initialState, String storageFile)
//			throws Exception {
//		AcidInputStream input = new AcidInputStream(storageFile);
//		try {
//			initialState = input.readObject();
//
//			while (true)
//				((Command) input.readObject()).executeOn(initialState);
//
//		} catch (EOFException expected) {
//		}
//
//		_system = initialState;
//		_journal = new AcidOutputStream(storageFile, _system);
//	}
//
//	synchronized public Object executeTransaction(Command transaction)
//			throws Exception {
//		checkSystemConsistency();
//		_journal.append(transaction);
//		return tryToExecute(transaction);
//	}
//
//	synchronized public Object executeQuery(Command query) {
//		checkSystemConsistency();
//		return tryToExecute(query);
//	}
//
//	private Object tryToExecute(Command command) {
//		try {
//			return command.executeOn(_system);
//		} catch (Error err) {
//			_journal.close();
//			throw err;
//		}
//	}
//
//	private void checkSystemConsistency() {
//		if (_journal.isClosed())
//			throw new IllegalStateException(
//					"An Error such as OutOfMemoryError or StackOverflowError was thrown while executing some previous transaction or query. The system might be in an inconsistent state.");
//	}
//
//	public static interface Command {
//		Object executeOn(Object system);
//	}
//}
//
//class AcidInputStream {
//
//	public AcidInputStream(String fileName) throws IOException {
//	}
//
//	public Object readObject() throws IOException {
//		return null;
//	}
//
//}
//
//class AcidOutputStream {
//
//	/**
//	 * Atomically and durably replaces the previous journal file with the new
//	 * entry using file rename manoeuvres.
//	 */
//	public AcidOutputStream(String fileName, Object firstEntry)
//			throws IOException {
//	}
//
//	public void append(Object entry) throws IOException {
//	}
//
//	public void close() {
//	}
//
//	public boolean isClosed() {
//		return false;
//	}
//
//}