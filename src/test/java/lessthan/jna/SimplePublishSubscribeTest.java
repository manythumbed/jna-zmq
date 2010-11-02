package lessthan.jna;

import static lessthan.jna.JnaHelpers.*;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import junit.framework.TestCase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class SimplePublishSubscribeTest extends TestCase {

	private static ZmqLibrary ZMQ_LIBARARY = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);

	public void testPublishSubscribe() {
		Executor executor = Executors.newFixedThreadPool(2);

		Publisher publisher = new Publisher();
		executor.execute(publisher);

		assertTrue(publisher.messageSent);
	}

	private class Publisher implements Runnable {

		public void run() {
			Pointer context = ZMQ_LIBARARY.zmq_init(1);
			Pointer subscriber = ZMQ_LIBARARY.zmq_socket(context, ZmqLibrary.ZMQ_PUB);
			ZMQ_LIBARARY.zmq_bind(subscriber, "tcp:localhost:5678");

			Memory data = asMemory("Message");
			zmq_msg_t message = new zmq_msg_t();
			ZMQ_LIBARARY.zmq_msg_init_data(message, data, new NativeLong(data.size()), null, null);
			int rc = ZMQ_LIBARARY.zmq_send(subscriber, message, 0);
			if(rc == 0)	{
				messageSent = true;
			}
		}

		public boolean messageSent;
	}
}
