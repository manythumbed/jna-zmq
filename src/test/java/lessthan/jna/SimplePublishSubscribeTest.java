package lessthan.jna;

import static lessthan.jna.JnaUtilities.*;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import junit.framework.TestCase;

import java.util.concurrent.*;

public class SimplePublishSubscribeTest extends TestCase {

	private static ZmqLibrary ZMQ_LIBRARY = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);

	public void testPublishSubscribe() throws ExecutionException, InterruptedException {
		ExecutorCompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(Executors.newFixedThreadPool(2));
		Future<Boolean> subscriberResult = completionService.submit((new Subscriber()));
		Future<Boolean> publishResult = completionService.submit(new Publisher());

		assertTrue(publishResult.get());
		assertTrue(subscriberResult.get());
	}

	private class Subscriber implements Callable<Boolean> {

		public Boolean call() throws Exception {
			Pointer context = ZMQ_LIBRARY.zmq_init(1);
			Pointer subscriber = ZMQ_LIBRARY.zmq_socket(context, ZmqLibrary.ZMQ_SUB);
			ZMQ_LIBRARY.zmq_setsockopt(subscriber, ZmqLibrary.ZMQ_SUBSCRIBE, asMemory(""), new NativeLong(0));
			ZMQ_LIBRARY.zmq_connect(subscriber, "tcp:localhost:5678");

			zmq_msg_t message = new zmq_msg_t();
			ZMQ_LIBRARY.zmq_msg_init(message);
			while (true) {
				int result = ZMQ_LIBRARY.zmq_recv(subscriber, message, ZmqLibrary.ZMQ_NOBLOCK);
				if(result == 0) break;
			}
			
			String data = asJavaString(ZMQ_LIBRARY.zmq_msg_data(message).getString(0));

			ZMQ_LIBRARY.zmq_close(subscriber);
			ZMQ_LIBRARY.zmq_term(context);
			return "Message".equals(data);
		}
	}

	private class Publisher implements Callable<Boolean> {

		public Boolean call() throws Exception {
			Pointer context = ZMQ_LIBRARY.zmq_init(1);
			Pointer publisher = ZMQ_LIBRARY.zmq_socket(context, ZmqLibrary.ZMQ_PUB);
			ZMQ_LIBRARY.zmq_bind(publisher, "tcp:localhost:5678");

			Memory data = asMemory("Message");
			zmq_msg_t message = new zmq_msg_t();
			ZMQ_LIBRARY.zmq_msg_init_data(message, data, new NativeLong(data.size()), null, null);
			boolean result = 0 == ZMQ_LIBRARY.zmq_send(publisher, message, 0);

			ZMQ_LIBRARY.zmq_close(publisher);
			ZMQ_LIBRARY.zmq_term(context);
			return result;
		}
	}
}
