package sneer.apps.transferqueue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Hashtable;
import java.util.Map;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class TransferQueue {
	private static final int FILEPART_CHUNK_SIZE = 5000;
	Map<TransferKey, FileSchedule> _receiverSchedule = new Hashtable<TransferKey, FileSchedule>();
	private final Channel _channel;
	
	public TransferQueue(Channel channel){
		_channel = channel;
		_channel.input().addReceiver(transferQueueReceiver());
	}
	
	public void sendFile(final ContactId contactId, final File file, final String transferId, final Omnivore<Long> progressCallback){
		Threads.startDaemon(new Runnable(){ public void run() {
			try {
				tryToSendFile(contactId, file, transferId, progressCallback);
			} catch(IOException ioe) {
				ioe.printStackTrace(); //Fix: Treat properly.
			}
		}});
	}
	
	public void receiveFile(ContactId contactId, File file, long size, String transferId, Omnivore<Long> progressCallback){
		_receiverSchedule.put(new TransferKey(transferId, contactId), new FileSchedule(contactId, file, size, progressCallback));
	}
	
	private Omnivore<Packet> transferQueueReceiver() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {
			TransferPacket transferPacket = (TransferPacket) packet._contents;
			FileSchedule schedule = _receiverSchedule.get(new TransferKey(transferPacket._transferId, packet._contactId));
			if (schedule == null) return; //someone sending a file I dont want to receive.
			writeFilePart(transferPacket, schedule);
		}};
	}
		
	private void writeFilePart(TransferPacket transferPacket, FileSchedule schedule) {
		if ((transferPacket._contents.length+transferPacket._offset)>schedule._size) return;
		try {
			RandomAccessFile raf = new RandomAccessFile(schedule._file,"rws");
			raf.seek(transferPacket._offset);
			raf.write(transferPacket._contents,0,transferPacket._contents.length);
			raf.close();
			if (schedule._progressCallback!=null)
				schedule._progressCallback.consume(transferPacket._offset+transferPacket._contents.length);
		} catch (FileNotFoundException e) {
			e.printStackTrace(); //Fix: Treat properly
		} catch (IOException e) {
			e.printStackTrace(); //Fix: Treat properly
		}
	}

	public class FileSchedule{
		public final File _file;
		public final long _size;
		public final ContactId _contactId;
		public final Omnivore<Long> _progressCallback;

		public FileSchedule(ContactId contactId, File file, long size, Omnivore<Long> progressCallback){
			_contactId = contactId;
			_file = file;
			_size = size;
			_progressCallback = progressCallback;
		}
	}
	
	public class TransferKey{
		public final String _transferId;
		public final ContactId _contactId;

		public TransferKey(String transferId, ContactId contactId){
			_transferId = transferId;
			_contactId = contactId;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj==null) return false;
			if (!(obj instanceof TransferKey)) return false;
			TransferKey temp = (TransferKey)obj;
			return _transferId.equals(temp._transferId)&&_contactId.equals(temp._contactId);
		}
		
	}

	private void tryToSendFile(final ContactId contactId, File file, final String transferId, final Omnivore<Long> progressCallback) throws IOException {
		byte[] buffer = new byte[FILEPART_CHUNK_SIZE];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		int read;
		long offset = 0;
		while ((read = in.read(buffer)) != -1) {
			byte[] contents = new byte[read];
			System.arraycopy(buffer,0,contents,0,read);
			final TransferPacket filePart = new TransferPacket(transferId, contents, offset);
			sendPart(contactId, filePart);
			offset += read;
			if (progressCallback!=null)
				progressCallback.consume(offset);
		}
	}

	private void sendPart(ContactId contactId, TransferPacket filePart) {
		_channel.output().consume(new Packet(contactId, filePart));
	}
	
}
