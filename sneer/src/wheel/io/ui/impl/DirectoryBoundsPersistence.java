package wheel.io.ui.impl;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import wheel.io.Log;
import wheel.io.Streams;
import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;
import wheel.lang.Threads;

public class DirectoryBoundsPersistence implements BoundsPersistence {

	//Constant indicating time in millis used to avoid multiple writes 
	//to disk caused by multiple calls to storeBounds 
	private static int DISK_WRITE_THRESHOLD = 1000;
	private Boolean _isDirty = false;
	
	private static final String BOUNDS_FILE_NAME = ".bounds";
	private final Directory _directory;
	private final Map<String, Rectangle> _storedBounds;
	private Thread _boundsThread;
	private boolean _synchronous;


	public DirectoryBoundsPersistence(Directory directory) {
		_directory = directory;
		_storedBounds = getStoredBounds();
		_boundsThread = new Thread();
	}
	
	public DirectoryBoundsPersistence(TransientDirectory directory, boolean asynchronous) {
		this(directory);		
		_synchronous = !asynchronous;
		DISK_WRITE_THRESHOLD = 0;
	}

	@Override
	public synchronized Rectangle getStoredBounds(String id) {
		return _storedBounds.get(id);
	}

	@Override
	public synchronized void storeBounds(String id, Rectangle bounds) {
		_storedBounds.put(id, bounds);
		
		synchronized (_isDirty ){ //Fix Consider using monitor object or AtomicBoolean
			_isDirty = true;
			//Fix: may lose data, isAlive isn't a good mechanism to deal with concurrency 
			if (!_boundsThread.isAlive()){
				_boundsThread = new Thread(new StoreBounds());
				_boundsThread.start();
			}
		}
		
		if (_synchronous)
			Threads.joinWithoutInterruptions(_boundsThread);
		
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Rectangle> getStoredBounds() {
		if (!_directory.fileExists(BOUNDS_FILE_NAME))
			return new HashMap<String, Rectangle>();
		
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = 
				new ObjectInputStream(_directory.openFile(BOUNDS_FILE_NAME));	
			Object savedBounds = objectInputStream.readObject();
			if (savedBounds instanceof Map)
				return (Map<String, Rectangle>)savedBounds;
			
		} catch (IOException e) {
			Log.log(e);
		} catch (ClassNotFoundException e) {
			Log.log(e);
		} finally {
			if (objectInputStream != null)
				Streams.crash(objectInputStream);
		}
		
		return new HashMap<String, Rectangle>();
	}
	
	private final class StoreBounds implements Runnable {


		@Override
		public void run() {
			waitDiskWriteThreshold();
			writeBoundsToFile();
		}

		private void waitDiskWriteThreshold() {
			Threads.sleepWithoutInterruptions(DISK_WRITE_THRESHOLD);
			synchronized (_isDirty){
				if (_isDirty){
					_isDirty = false;
				} else{
					return;
				}
			}
			
			waitDiskWriteThreshold();
		}
		
		protected void writeBoundsToFile() {
			ObjectOutputStream objectOutputStream = null;
			try {
				if (_directory.fileExists(BOUNDS_FILE_NAME))
					_directory.deleteFile(BOUNDS_FILE_NAME);
					
				objectOutputStream = 
					new ObjectOutputStream(_directory.createFile(BOUNDS_FILE_NAME));	
				objectOutputStream.writeObject(_storedBounds);
				objectOutputStream.flush();
			} catch (IOException e) {
				Log.log(e);
			} finally {
				if (objectOutputStream != null)
					Streams.crash(objectOutputStream);
			}
			
		}

	}

}
