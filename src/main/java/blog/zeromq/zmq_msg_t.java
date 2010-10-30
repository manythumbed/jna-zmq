package blog.zeromq;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class zmq_msg_t extends Structure {
	public Pointer content;
	public byte flags;
	public byte vsm_size;
	public byte[] vsm_data = new byte[30];

	public zmq_msg_t() {
		super();
	}

	public static class ByReference extends zmq_msg_t implements Structure.ByReference {
	}

	public static class ByValue extends zmq_msg_t implements Structure.ByValue {
	}

}

