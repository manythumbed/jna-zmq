package blog.zeromq;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import junit.framework.TestCase;

/**
 * These are smoke tests to verify the JNA mapping of the ZMQ library, no actual messaging is performed.
 */
public class ZmqLibraryTest extends TestCase {

	public void testVersion() {
		assertNotNull(zmqLibrary);
		int[] major = new int[1];
		int[] minor = new int[1];
		int[] patch = new int[1];
		zmqLibrary.zmq_version(major, minor, patch);
		assertEquals(2, major[0]);
		assertEquals(0, minor[0]);
		assertEquals(8, patch[0]);
	}

	public void testError() {
		assertEquals(2, zmqLibrary.zmq_errno());
		assertEquals("Operation not permitted", zmqLibrary.zmq_strerror(ZmqLibrary.ENOTSUP));
		assertEquals("Input/output error", zmqLibrary.zmq_strerror(ZmqLibrary.EADDRINUSE));
		assertEquals("Invalid request descriptor", zmqLibrary.zmq_strerror(ZmqLibrary.ETERM));
	}

	public void testMsgInit() {
		assertEquals(0, zmqLibrary.zmq_msg_init(new zmq_msg_t()));
		assertEquals(0, zmqLibrary.zmq_msg_init_size(new zmq_msg_t(), new NativeLong(10)));

		String string = "data";
		zmq_msg_t message = new zmq_msg_t();
		Memory data = asMemory(string);
		assertEquals(0, zmqLibrary.zmq_msg_init_data(message, data, new NativeLong(data.size()), null, asMemory("hint")));

		String content = zmqLibrary.zmq_msg_data(message).getString(0);
		assertEquals(string, asJavaString(content));
	}

	private Memory asMemory(String data) {
		byte[] terminated = Native.toByteArray(data);
		Memory memory = new Memory(terminated.length);
		memory.write(0l, terminated, 0, terminated.length);

		return memory;
	}

	private String asJavaString(String data) {
		return Native.toString(data.getBytes());
	}

	public void testMsgClose() {
		zmq_msg_t message = new zmq_msg_t();
		zmqLibrary.zmq_msg_init(message);
		assertEquals(0, zmqLibrary.zmq_msg_close(message));
	}

	public void testMove() {
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

	public void testCopy() {
		String content = "content";
		Memory payload = asMemory(content);

		zmq_msg_t original = new zmq_msg_t();
		zmq_msg_t copy = new zmq_msg_t();
		assertEquals(0, zmqLibrary.zmq_msg_init_data(original, payload, new NativeLong(payload.size()), null, null));
		assertEquals(0, zmqLibrary.zmq_msg_init(copy));
		assertEquals(0, zmqLibrary.zmq_msg_copy(copy, original));

		assertEquals(content, asJavaString(zmqLibrary.zmq_msg_data(copy).getString(0)));

	}

	public void testData() {
		String content = "content";

		zmq_msg_t message = new zmq_msg_t();
		Memory payload = asMemory(content);
		zmqLibrary.zmq_msg_init_data(message, payload, new NativeLong(payload.size()), null, null);

		Pointer pointer = zmqLibrary.zmq_msg_data(message);
		assertNotNull(pointer);
		assertEquals(content, asJavaString(pointer.getString(0)));
	}

	public void testSize() {
		String content = "content";

		zmq_msg_t message = new zmq_msg_t();
		Memory payload = asMemory(content);
		zmqLibrary.zmq_msg_init_data(message, payload, new NativeLong(payload.size()), null, null);

		assertEquals(content.length() + 1, zmqLibrary.zmq_msg_size(message).intValue()); // +1 for nul termination
	}

	public void testInit()	{
		assertNotNull(zmqLibrary.zmq_init(1));
	}

	public void testTerm()	{
		Pointer context = zmqLibrary.zmq_init(1);
		assertEquals(0, zmqLibrary.zmq_term(context));
	}

	public void testSocket()	{
		Pointer context = zmqLibrary.zmq_init(1);
		assertNotNull(context);

		Pointer socket = zmqLibrary.zmq_socket(context, ZmqLibrary.ZMQ_REP);
		assertNotNull(socket);
	}

	public void testClose()	{
		Pointer context = zmqLibrary.zmq_init(1);
		assertNotNull(context);

		Pointer socket = zmqLibrary.zmq_socket(context, ZmqLibrary.ZMQ_REP);
		assertNotNull(socket);
		assertEquals(0, zmqLibrary.zmq_close(socket));
	}

	public void testGetsockopt()	{
		Pointer context = zmqLibrary.zmq_init(1);
		assertNotNull(context);

		Pointer socket = zmqLibrary.zmq_socket(context, ZmqLibrary.ZMQ_REP);
		assertNotNull(socket);

		assertEquals(0, getSocketOption(socket, ZmqLibrary.ZMQ_RCVMORE, 8).getInt(0));
		assertEquals(0, getSocketOption(socket, ZmqLibrary.ZMQ_HWM, 8).getInt(0));
		assertEquals(0, getSocketOption(socket, ZmqLibrary.ZMQ_SWAP, 8).getInt(0));
		assertEquals(0, getSocketOption(socket, ZmqLibrary.ZMQ_AFFINITY, 8).getInt(0));
		assertEquals(0, getSocketOption(socket, ZmqLibrary.ZMQ_IDENTITY, 255).getInt(0));
		assertEquals(100, getSocketOption(socket, ZmqLibrary.ZMQ_RATE, 255).getInt(0));
		assertEquals(10, getSocketOption(socket, ZmqLibrary.ZMQ_RECOVERY_IVL, 255).getInt(0));
		assertEquals(1, getSocketOption(socket, ZmqLibrary.ZMQ_MCAST_LOOP, 255).getInt(0));
		assertEquals(0, getSocketOption(socket, ZmqLibrary.ZMQ_SNDBUF, 255).getInt(0));
		assertEquals(0, getSocketOption(socket, ZmqLibrary.ZMQ_RCVBUF, 255).getInt(0));
	}

	public void testSetsockopt()	{
		Pointer context = zmqLibrary.zmq_init(1);
		assertNotNull(context);

		Pointer socket = zmqLibrary.zmq_socket(context, ZmqLibrary.ZMQ_SUB);
		assertNotNull(socket);

		checkSocketOption(socket, ZmqLibrary.ZMQ_HWM, 8, 1);
		checkSocketOption(socket, ZmqLibrary.ZMQ_SWAP, 8, 123);
		checkSocketOption(socket, ZmqLibrary.ZMQ_AFFINITY, 8, 0);
		// TO DO test identity with binary data

		String prefix = "prefix";
		checkSocketOption(socket, ZmqLibrary.ZMQ_SUBSCRIBE, prefix); // Can't read this back
		checkSocketOption(socket, ZmqLibrary.ZMQ_UNSUBSCRIBE, prefix);

		checkSocketOption(socket, ZmqLibrary.ZMQ_RATE, 8, 200);
		checkSocketOption(socket, ZmqLibrary.ZMQ_RECOVERY_IVL, 8, 200);
//		checkSocketOption(socket, ZmqLibrary.ZMQ_MCAST_LOOP, 8, 0);

		checkSocketOption(socket, ZmqLibrary.ZMQ_SNDBUF, 8, 10);
		checkSocketOption(socket, ZmqLibrary.ZMQ_RCVBUF, 8, 10);

	}

	private void checkSocketOption(Pointer socket, int name, String expected) {
		Memory data = asMemory(expected);
		assertEquals(0, zmqLibrary.zmq_setsockopt(socket, name, data, new NativeLong(data.size())));
	}

	private void checkSocketOption(Pointer socket, int name, int capacity, int expected)	{
		Memory value = new Memory(capacity);
		value.setInt(0, expected);
		assertEquals(0, zmqLibrary.zmq_setsockopt(socket, name, value, new NativeLong(capacity)));
		assertEquals(expected, getSocketOption(socket, name, capacity).getInt(0));
	}

	private Pointer getSocketOption(Pointer socket, int name, int capacity)	{
		Memory value = new Memory(capacity);
		value.clear(capacity);
		LongByReference length = new LongByReference(capacity);

		assertEquals(0, zmqLibrary.zmq_getsockopt(socket, name, value, length));
		assertNotNull(value);

		return value;
	}

	public void setUp() {
		zmqLibrary = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);
	}

	public void tearDown() {
		zmqLibrary = null;
	}

	private ZmqLibrary zmqLibrary;
}
