package frozenbubble.game.net.library.jiga;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *  Provide simple layout to make an HTTP request to the server
 */
public class XmlRequest implements Runnable {
	
	/** POST Method */
	public final static String METHOD_POST = "POST";
	/** GET Method */
	public final static String METHOD_GET = "GET";
	
	/** Successfully completed */
	public final static int STATE_OK = 0;
	/** Not yet completed */
	public final static int STATE_INCOMPLETE = 1;	
	/** Request is invalid, can't execute */ 
	public final static int STATE_INVALID_REQUEST = 3;
	/** Timeout */
	public final static int STATE_TIMEOUT = 4;
	/** Completed with errors */
	public final static int STATE_ERROR = 5;
	
	/** Timeout */
	private final static int REQUEST_TIMEOUT = 30000; // 30 secondes 
	
	/** CodeBase URL */
	private URL codeBase;
	/** Method (POST / GET) */
	private String method;
	/** URL to call */
	private String url;
	/** Data */
	private String data;
	/** Should this call be synchronous */
	private boolean async;
	/** Complete state */
	private int state;
	/** Answer */
	private String answer;
	
	public XmlRequest(GameApplet applet) {
		this(applet.getCodeBase());
	}
	
	public XmlRequest(URL url1) {
		codeBase = url1;
	}
		
	/**
	 * Defines what to call
	 * @param method1 Must be POST or GET. Set to POST if not understood
	 * @param url1 url to call (Will be completed with the codebase value)
	 * @param async1 defines asynchronous call
	 */
	public void open(String method1, String url1, boolean async1) {

		if (METHOD_POST.equals(method1) || METHOD_GET.equals(method1)) {
			this.method = method1;
		}
		else {
			this.method = METHOD_POST;
		}
		
		if (url1 == null) {
			url1 = "/";
		}
		else {
			this.url = url1;
		}
		this.async = async1;
		
		this.state = STATE_INCOMPLETE;
	}
	
	/**
	 * Calls server.  
	 * @param data1 form parameters, concatened to url (GET) or added to body (POST)
	 */
	public void send(String data1) {
		
		this.data = data1;
		
		if (method == null) {
			// Do nothing if open() was not called
			state = STATE_INVALID_REQUEST;
			
			return; 
		}
		
		state = STATE_INCOMPLETE;
		
		if (async) {
			new Thread(this).start();
		}
		else {
			run();
		}
	}
	
	public void run() {
		if (method != null && "http".equalsIgnoreCase(codeBase.getProtocol())) { // Avoid direct invalid call
			try {
				state = STATE_INCOMPLETE;
				answer = null;
				
				long timeLimit = System.currentTimeMillis() + REQUEST_TIMEOUT;

				// Build URL
				String urlString = "http://";
				urlString += codeBase.getHost();
				
				if (codeBase.getPort() != -1 && codeBase.getPort() != 80) {
					urlString += ":" + String.valueOf(codeBase.getPort());
				}

				if (!url.startsWith("/")) {
					urlString += "/";
				}
				urlString += url;

				// Add GET data
				if (METHOD_GET.equals(method) 
						&& data != null 
						&& data.trim().length() > 0) {
					// Add parameters
					if (url.indexOf("?") != -1) {
						urlString += "&";
					}
					else {
						urlString += "?";
					}
					
					urlString += data;
					
					// Reset data
					data = new String();
				}
				
				if (data == null) {
					data = new String();
				}
				
				// Init HttpURLConnection
				HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();

				connection.setRequestMethod(method);
				
				if (METHOD_POST.equals(method)) {
					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");					
				}
				connection.setRequestProperty("Content-Length", String.valueOf(data.length()));
				
				connection.setInstanceFollowRedirects(true);
				connection.setUseCaches(false);
				
				connection.setDoOutput(true);
				connection.setDoInput(true);
				
				// Connect
				connection.connect();
				
				// Send request
				connection.getOutputStream().write(data.getBytes("ISO-8859-1"));
				connection.getOutputStream().flush();

				// Read answer				
				BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
				int available = bis.available();
				
				boolean dataReceived = false;
				String content = new String();
				int dataSize = connection.getContentLength();
				
				while (!dataReceived) {
					if (available != 0) {
						
						byte[] inData = new byte[available];
						bis.read(inData);
						
						content += new String(inData, "ISO-8859-1");
						
						dataReceived = (content.length() >= dataSize);
					}
					
					if (System.currentTimeMillis() > timeLimit && !dataReceived) {
						state = STATE_TIMEOUT;
						dataReceived = true; // Stop while
					}					
					
					if (!dataReceived) {
						try {
							Thread.sleep(500);
						}
						catch(Exception e) {} 
						
						available = bis.available();
					}
					else {
						state = STATE_OK;
						answer = content;
					}					
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				state = STATE_ERROR;
			}
		}
	}
	
	public int getState() {
		return state;
	}
	
	public boolean completed() {
		return state != STATE_INCOMPLETE;
	}
	
	public String getResponseText() {
		return answer;
	}
	
	public MiniDOM getResponseXML() {
		MiniDOM output = null;
		
		try {
			output = MiniDOM.getTree(answer);
		}
		catch(Exception e) {}
		
		return output;
	}
}
