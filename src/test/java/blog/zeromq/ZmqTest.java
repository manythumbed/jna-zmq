package blog.zeromq;

import junit.framework.TestCase;

public class ZmqTest extends TestCase {

	public void testVersion()	{
		assertEquals("2.0.8", zmq.getVersion());
		assertEquals(2, zmq.getMajorVersion());
		assertEquals(0, zmq.getMinorVersion());
		assertEquals(8, zmq.getPatch());
	}

	public void setUp()	{
		zmq = new Zmq();
	}

	public void tearDown()	{
		zmq = null;
	}

	private Zmq zmq;
}
