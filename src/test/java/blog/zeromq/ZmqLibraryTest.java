package blog.zeromq;

import com.sun.jna.Native;
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

	public void setUp()	{
		zmqLibrary = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);
	}

	public void tearDown()	{
		zmqLibrary = null;
	}

	private ZmqLibrary zmqLibrary;
}
