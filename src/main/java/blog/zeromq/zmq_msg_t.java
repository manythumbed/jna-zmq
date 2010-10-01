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

		public zmq_msg_t(Pointer content, byte flags, byte vsm_size, byte vsm_data[]) {
			super();
			this.content = content;
			this.flags = flags;
			this.vsm_size = vsm_size;
			if (vsm_data.length != this.vsm_data.length) {
				throw new java.lang.IllegalArgumentException("Incorrect array size");
			}
			this.vsm_data = vsm_data;
		}

		public static class ByReference extends zmq_msg_t implements Structure.ByReference {
		}

		public static class ByValue extends zmq_msg_t implements Structure.ByValue {
		}

	}

