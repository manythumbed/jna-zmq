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
		Executor executor = Executors.newFixedThreadPool(2);
		ExecutorCompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(executor);
		executor.execute(new Publisher());
		Future<Boolean> subscriberResult = completionService.submit((new Subscriber()));

		assertTrue(subscriberResult.get());
	}

	private class Subscriber implements Callable<Boolean> {

		public Boolean call() throws Exception {
			int rc;
			Pointer context = ZMQ_LIBRARY.zmq_init(1);
			Pointer subscriber = ZMQ_LIBRARY.zmq_socket(context, ZmqLibrary.ZMQ_SUB);
			rc = ZMQ_LIBRARY.zmq_connect(subscriber, "tcp://localhost:5556");
			assert(rc == 0);
			Memory filter = asMemory("");
			rc =ZMQ_LIBRARY.zmq_setsockopt(subscriber, ZmqLibrary.ZMQ_SUBSCRIBE, filter, new NativeLong(0));
			assert(rc == 0);

			Pointer control = ZMQ_LIBRARY.zmq_socket(context, ZmqLibrary.ZMQ_REQ);
			ZMQ_LIBRARY.zmq_connect(control, "tcp://*:5555");

			zmq_msg_t controlMessage = new zmq_msg_t();
			Memory controlPayload = asMemory("GO!");
			ZMQ_LIBRARY.zmq_msg_init_data(controlMessage, controlPayload, new NativeLong(controlPayload.size()), null, null);
			System.out.println("Sending control message");
			ZMQ_LIBRARY.zmq_send(control, controlMessage, 0);
			System.out.println("Control message sent");

			zmq_msg_t message = new zmq_msg_t();
			ZMQ_LIBRARY.zmq_msg_init(message);

			System.out.println("Subscribed and listening");
			ZMQ_LIBRARY.zmq_recv(subscriber, message, 0);
			System.out.println("Done receiving");

			String data = asJavaString(ZMQ_LIBRARY.zmq_msg_data(message));

			ZMQ_LIBRARY.zmq_close(subscriber);
			ZMQ_LIBRARY.zmq_term(context);
			return "Message".equals(data);
		}
	}

	private class Publisher implements Runnable {

		public void run() {
			int rc;
			Pointer context = ZMQ_LIBRARY.zmq_init(1);
			Pointer publisher = ZMQ_LIBRARY.zmq_socket(context, ZmqLibrary.ZMQ_PUB);
			rc = ZMQ_LIBRARY.zmq_bind(publisher, "tcp://*:5556");
			ZMQ_LIBRARY.zmq_bind(publisher, "ipc://weather.ipc");
			assert(rc == 0);

			Pointer control = ZMQ_LIBRARY.zmq_socket(context, ZmqLibrary.ZMQ_REP);
			ZMQ_LIBRARY.zmq_bind(control, "tcp://*:5555");

			zmq_msg_t controlMessage = new zmq_msg_t();
			ZMQ_LIBRARY.zmq_msg_init(controlMessage);
			System.out.println("Waiting for control message");
			rc = ZMQ_LIBRARY.zmq_recv(control, controlMessage, 0);

			System.out.println("Sending messages");
			while(true)	{
//			for(int i = 0; i < 10000; i++)	{
//				if(i % 100 == 0)	{
//					System.out.println("Sent " + i);
//				}
				Memory data = asMemory("Message");
				zmq_msg_t message = new zmq_msg_t();
				rc = ZMQ_LIBRARY.zmq_msg_init_data(message, data, new NativeLong(data.size()), null, null);
				assert(rc == 0);
				rc = ZMQ_LIBRARY.zmq_send(publisher, message, 0);
				assert(rc == 0);
				rc = ZMQ_LIBRARY.zmq_msg_close(message);
				assert(rc == 0);
			}

//			ZMQ_LIBRARY.zmq_close(publisher);
//			ZMQ_LIBRARY.zmq_term(context);
		}
	}
}

/*
//  Prepare our context and publisher
    void *context = zmq_init (1);
    void *publisher = zmq_socket (context, ZMQ_PUB);
    zmq_bind (publisher, "tcp://*:5556");
    zmq_bind (publisher, "ipc://weather.ipc");

    //  Initialize random number generator
    srandom ((unsigned) time (NULL));
    while (1) {
        //  Get values that will fool the boss
        int zipcode, temperature, relhumidity;
        zipcode     = within (100000);
        temperature = within (215) - 80;
        relhumidity = within (50) + 10;

        //  Send message to all subscribers
        char update [20];
        sprintf (update, "%05d %d %d", zipcode, temperature, relhumidity);
        s_send (publisher, update);
    }        rt
    zmq_close (publisher);
    zmq_term (context);
    return 0;
 */

/*
  void *context = zmq_init (1);

    //  Socket to talk to server
    printf ("Collecting updates from weather server...\n");
    void *subscriber = zmq_socket (context, ZMQ_SUB);
    zmq_connect (subscriber, "tcp://localhost:5556");

    //  Subscribe to zipcode, default is NYC, 10001
    char *filter = (argc > 1)? argv [1]: "10001 ";
    zmq_setsockopt (subscriber, ZMQ_SUBSCRIBE, filter, strlen (filter));

    //  Process 100 updates
    int update_nbr;
    long total_temp = 0;
    for (update_nbr = 0; update_nbr < 100; update_nbr++) {
        char *string = s_recv (subscriber);
        int zipcode, temperature, relhumidity;
        sscanf (string, "%d %d %d",
            &zipcode, &temperature, &relhumidity);
        total_temp += temperature;
        free (string);
    }
    printf ("Average temperature for zipcode '%s' was %dF\n",
        filter, (int) (total_temp / update_nbr));

    zmq_close (subscriber);
    zmq_term (context);
    return 0;
*/