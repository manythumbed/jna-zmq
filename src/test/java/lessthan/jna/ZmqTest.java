package lessthan.jna;

import blog.zeromq.Zmq;
import junit.framework.Assert;
import junit.framework.TestCase;

public class ZmqTest extends TestCase {

	public void testVersion()	{
		Assert.assertEquals("2.0.8", zmq.getVersion());
		Assert.assertEquals(2, zmq.getMajorVersion());
		Assert.assertEquals(0, zmq.getMinorVersion());
		Assert.assertEquals(8, zmq.getPatch());
	}

	public void setUp()	{
		zmq = new Zmq();
	}

	public void tearDown()	{
		zmq = null;
	}

	private Zmq zmq;
}
