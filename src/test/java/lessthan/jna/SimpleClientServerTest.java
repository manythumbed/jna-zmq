package lessthan.jna;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import junit.framework.TestCase;

import java.util.concurrent.*;

public class SimpleClientServerTest extends TestCase {

	public void testClientServer() throws ExecutionException, InterruptedException {
		ExecutorCompletionService<String> completionService = new ExecutorCompletionService<String>(Executors.newFixedThreadPool(2));

		Future<String> serverFuture = completionService.submit(new Server());
		Future<String> clientFuture = completionService.submit(new Client());

		assertEquals("request", serverFuture.get());
		assertEquals("response", clientFuture.get());
	}

	private static ZmqLibrary ZMQ = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);

	private class Server implements Callable<String> {
		public String call() throws Exception {
			Pointer context = ZMQ.zmq_init(1);
			Pointer socket = ZMQ.zmq_socket(context, ZmqLibrary.ZMQ_REP);
			ZMQ.zmq_bind(socket, "tcp://*:5555");

			zmq_msg_t message = new zmq_msg_t();
			ZMQ.zmq_msg_init(message);
			ZMQ.zmq_recv(socket, message, 0);

			String request = JnaUtilities.asJavaString(ZMQ.zmq_msg_data(message).getString(0));
			ZMQ.zmq_msg_close(message);

			Thread.sleep(1);

			zmq_msg_t response = new zmq_msg_t();
			ZMQ.zmq_msg_init(response);

			Memory payload = JnaUtilities.asMemory("response");
			ZMQ.zmq_msg_init_data(response, payload, new NativeLong(payload.size()), null, null);
			ZMQ.zmq_send(socket, response, 0);

			ZMQ.zmq_msg_close(response);

			return request;
		}
	}

	private class Client implements Callable<String> {
		public String call() throws Exception {
			Pointer context = ZMQ.zmq_init(1);
			Pointer socket = ZMQ.zmq_socket(context, ZmqLibrary.ZMQ_REQ);
			ZMQ.zmq_connect(socket, "tcp://*:5555");

			zmq_msg_t request = new zmq_msg_t();
			Memory payload = JnaUtilities.asMemory("request");
			ZMQ.zmq_msg_init_data(request, payload, new NativeLong(payload.size()), null, null);

			ZMQ.zmq_send(socket, request, 0);
			ZMQ.zmq_msg_close(request);

			zmq_msg_t response = new zmq_msg_t();
			ZMQ.zmq_msg_init(response);

			ZMQ.zmq_recv(socket, response, 0);

			return JnaUtilities.asJavaString(ZMQ.zmq_msg_data(response).getString(0));
		}
	}
}