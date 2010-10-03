package blog.zeromq;

import com.sun.jna.Memory;
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

		String string = "data";
		zmq_msg_t message = new zmq_msg_t();
		Memory data = asMemory(string);
		assertEquals(0, zmqLibrary.zmq_msg_init_data(message, data, new NativeLong(data.size()), null, asMemory("hint")));

		String content = zmqLibrary.zmq_msg_data(message).getString(0);
		assertEquals(string, asJavaString(content));
	}

	private Memory asMemory(String data)	{
		byte[] terminated = Native.toByteArray(data);
		Memory memory = new Memory(terminated.length);
		memory.write(0l, terminated, 0, terminated.length);

		return memory;
	}

	private String asJavaString(String data)	{
		return Native.toString(data.getBytes());
	}

	public void testMsgClose()	{
		zmq_msg_t message = new zmq_msg_t();
		zmqLibrary.zmq_msg_init(message);
		assertEquals(0, zmqLibrary.zmq_msg_close(message));
	}

	public void testMove()	{
		zmq_msg_t source = new zmq_msg_t();
		zmq_msg_t destination = new zmq_msg_t();

		Memory srcContent = asMemory("source");
		Memory dstContent = asMemory("destination");
		assertEquals(0, zmqLibrary.zmq_msg_init_data(source, srcContent, new NativeLong(srcContent.size()), null, null));
		assertEquals(0, zmqLibrary.zmq_msg_init_data(destination, dstContent, new NativeLong(dstContent.size()), null, null));

		assertEquals(0, zmqLibrary.zmq_msg_move(source, destination));

		assertEquals("destination", asJavaString(zmqLibrary.zmq_msg_data(source).getString(0)));
		assertEquals("", asJavaString(zmqLibrary.zmq_msg_data(destination).getString(0)));
	}

	public void testData()	{
		String content = "content";

		zmq_msg_t message = new zmq_msg_t();
		Memory payload = asMemory(content);
		zmqLibrary.zmq_msg_init_data(message, payload, new NativeLong(payload.size()), null, null);

		Pointer pointer = zmqLibrary.zmq_msg_data(message);
		assertNotNull(pointer);
		assertEquals(content, asJavaString(pointer.getString(0)));
	}

	public void setUp()	{
		zmqLibrary = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);
	}

	public void tearDown()	{
		zmqLibrary = null;
	}

	private ZmqLibrary zmqLibrary;
}
