package blog.zeromq;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
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

		zmq_msg_t message = new zmq_msg_t();

		Memory memory = new Memory("data".getBytes().length);
		memory.write(0l, "data".getBytes(), 0, "data".getBytes().length);
		assertEquals(0, zmqLibrary.zmq_msg_init_data(message, memory, new NativeLong("data".length()), null, "hint".getBytes()));

		String content = zmqLibrary.zmq_msg_data(message).getString(0);
		assertEquals("data", content.substring(0, content.length() - 1));
	}

	public void testMsgClose()	{
		zmq_msg_t message = new zmq_msg_t();
		zmqLibrary.zmq_msg_init(message);
		assertEquals(0, zmqLibrary.zmq_msg_close(message));
	}

//	public void testMove()	{
//		byte[] srcContent = "source".getBytes();
//		Memory srcMemory = new Memory(srcContent.length);
//		srcMemory.write(0l, srcContent, 0, srcContent.length);
//		byte[] destContent = "destination".getBytes();
//
//		zmq_msg_t source = new zmq_msg_t();
//		zmq_msg_t destination = new zmq_msg_t();
//
//		assertEquals(0, zmqLibrary.zmq_msg_init_data(source, srcContent, new NativeLong(srcContent.length), null, null));
//		assertEquals(0, zmqLibrary.zmq_msg_init_data(destination, destContent, new NativeLong(destContent.length), null, null));
//
//		assertEquals(0, zmqLibrary.zmq_msg_move(source, destination));
//
//	}

//	public void testZmqData()	{
//		byte[] content = "content".getBytes();
//
//		zmq_msg_t message = new zmq_msg_t();
//		zmqLibrary.zmq_msg_init_data(message, content, new NativeLong(content.length), null, null);
//
//		Pointer pointer = zmqLibrary.zmq_msg_data(message);
//		assertNotNull(pointer);
//		System.out.println("MSG: " + message.content);
//
//		assertEquals("X".getBytes(), pointer.getByteArray(0, 1));
//	}

	public void setUp()	{
		zmqLibrary = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);
	}

	public void tearDown()	{
		zmqLibrary = null;
	}

	private ZmqLibrary zmqLibrary;
}
