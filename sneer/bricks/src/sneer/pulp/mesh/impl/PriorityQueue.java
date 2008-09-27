package sneer.pulp.mesh.impl;

import java.util.LinkedList;
import java.util.List;

import wheel.lang.Threads;

class PriorityQueue<T> {

	private final PriorityQueue<T> _nextQueue;
	private final List<T> _myElements = new LinkedList<T>();
	private long _counter;
	
	private int _totalElements;

	PriorityQueue(int minimumPriority) {
		_nextQueue = minimumPriority == 0
			? null
			: new PriorityQueue<T>(minimumPriority - 1);
	}

	synchronized public void add(T element, int priority) {
		privateAdd(element, priority);

		_totalElements++;
//		if (_totalElements > 1)
//			System.out.println("Buffer size: " + _totalElements); //Fix Use blinking lights or something instead of out.

		this.notify();
	}

	private void privateAdd(T element, int priority) {
		if (priority == 0) { 
			_myElements.add(element);
		} else {
			_nextQueue.privateAdd(element, priority - 1);
		}
	}

	synchronized public T waitForNext() {
		T result = next();
		if (result == null) {
			Threads.waitWithoutInterruptions(this);
			result = next();
		}
		_totalElements--;
		return result;
	}

	private T next() {
		if (_myElements.isEmpty()) {
			if (_nextQueue == null) return null;
			return _nextQueue.next();
		}
		
		if (_counter++ == 9) {
			_counter = 0;
			if (_nextQueue != null) {
				T result = _nextQueue.next();
				if (result != null) return result;
			}
		}
		
		return _myElements.remove(0);
	}

}
