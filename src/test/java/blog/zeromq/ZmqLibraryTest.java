package blog.zeromq;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import junit.framework.TestCase;

public class ZmqLibraryTest extends TestCase {

	public void testVersion()	{
		assertNotNull(zmqLibrary);
		int[] major = new int[1];
		int[] minor = new int[1];
		int[] patch = new int[1];
		zmqLibrary.zmq_version(major, minor, patch);
		assertEquals(2, major[0]);
		assertEquals(0, minor[0]);
		assertEquals(8, patch[0]);
	}

	public void testError()	{
		assertEquals(2, zmqLibrary.zmq_errno());
		assertEquals("Operation not permitted", zmqLibrary.zmq_strerror(ZmqLibrary.ENOTSUP));
		assertEquals("Input/output error", zmqLibrary.zmq_strerror(ZmqLibrary.EADDRINUSE));
		assertEquals("Invalid request descriptor", zmqLibrary.zmq_strerror(ZmqLibrary.ETERM));
	}

	public void testMsgInit()	{
		assertEquals(0, zmqLibrary.zmq_msg_init(new zmq_msg_t()));
		assertEquals(0, zmqLibrary.zmq_msg_init_size(new zmq_msg_t(), new NativeLong(10)));
	}

	public void testMsgClose()	{
		zmq_msg_t message = new zmq_msg_t();
		zmqLibrary.zmq_msg_init(message);
		assertEquals(0, zmqLibrary.zmq_msg_close(message));
	}

	public void testMove()	{
		zmq_msg_t source = new zmq_msg_t();
		zmq_msg_t destination = new zmq_msg_t();

		//assertEquals(1, zmqLibrary.zmq_msg_move(source, destination));
	}

	public void testZmqData()	{
		Pointer data = zmqLibrary.zmq_msg_data(new zmq_msg_t());
		assertNotNull(data);
	}

	public void setUp()	{
		zmqLibrary = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);
	}

	public void tearDown()	{
		zmqLibrary = null;
	}

	private ZmqLibrary zmqLibrary;
}
