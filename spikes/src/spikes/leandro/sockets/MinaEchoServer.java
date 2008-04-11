package spikes.leandro.sockets;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaEchoServer {

	public static final int PORT = 8080;
	
	public static void main(String[] args) throws Exception {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();

        // Prepare the configuration
        //acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        // Bind
        acceptor.setHandler(new Handler());
        acceptor.bind(new InetSocketAddress(PORT));

        //System.out.println("Listening on port " + PORT);
    }

}

class Handler implements IoHandler {

	@Override
	public void exceptionCaught(IoSession session, Throwable error) throws Exception {
		error.printStackTrace();
		//System.out.println("exceptionCaught");
	}

	@Override
	public void messageReceived(IoSession session, Object msg) throws Exception {
		//System.out.println("messageReceived");
		String str = msg.toString();
		if( str.trim().equalsIgnoreCase("quit") ) {
			session.close();
			return;
		}
		session.write(">> "+str);
	}

	@Override
	public void messageSent(IoSession session, Object msg) throws Exception {
		//System.out.println("messageSent");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		//System.out.println("sessionClosed");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		//System.out.println("sessionCreated");
		session.getConfig().setIdleTime(IdleStatus.READER_IDLE, 10);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		//System.out.println("sessionIdle: "+status);
        if (status == IdleStatus.READER_IDLE) {
        	//session.close();
        }
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		//System.out.println("sessionOpened");
	}
	
}