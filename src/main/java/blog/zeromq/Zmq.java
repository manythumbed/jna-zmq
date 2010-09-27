package blog.zeromq;

import com.sun.jna.Native;

public class Zmq {

	public Zmq()	{
		this.zmqLibrary = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);
	}

	public int getMajorVersion()	{
		return zmqVersion()[0];
	}

	public int getMinorVersion()	{
		return zmqVersion()[1];
	}

	public int getPatch()	{
		return zmqVersion()[2];
	}

	public String getVersion()	{
		int[] version = zmqVersion();
		return version[0] + "." + version[1] + "." + version[2];
	}

	private int[] zmqVersion()	{
		int[] major = new int[1];
		int[] minor = new int[1];
		int[] patch = new int[1];

		int[] result = new int[3];

		zmqLibrary.zmq_version(major, minor, patch);
		result[0] = major[0];
		result[1] = minor[0];
		result[2] = patch[0];

		return result;
	}

	private ZmqLibrary zmqLibrary;
}
