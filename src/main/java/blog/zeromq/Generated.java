package blog.zeromq;
/*
import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;

import java.nio.IntBuffer;

import com.ochafik.lang.jnaerator.runtime.LibraryExtractor;
import com.ochafik.lang.jnaerator.runtime.MangledFunctionMapper;
import com.ochafik.lang.jnaerator.runtime.NativeSize;
import com.ochafik.lang.jnaerator.runtime.NativeSizeByReference;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.IntByReference;

import java.nio.IntBuffer;


public interface Generated extends Library {

		public static final java.lang.String JNA_LIBRARY_NAME = LibraryExtractor.getLibraryPath("test", true, test.TestLibrary.class);
		public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(test.TestLibrary.JNA_LIBRARY_NAME, com.ochafik.lang.jnaerator.runtime.MangledFunctionMapper.DEFAULT_OPTIONS);
		public static final Generated INSTANCE = (Generated) Native.loadLibrary(test.TestLibrary.JNA_LIBRARY_NAME, test.TestLibrary.class, com.ochafik.lang.jnaerator.runtime.MangledFunctionMapper.DEFAULT_OPTIONS);
		public static final int ZMQ_SUB = 2;
		public static final int ZMQ_VSM = 32;
		public static final int ZMQ_FORWARDER = 2;
		public static final int ZMQ_AFFINITY = 4;
		public static final int ZMQ_NOBLOCK = 1;
		public static final int ZMQ_PAIR = 0;
		public static final int ZMQ_MSG_MORE = 1;
		public static final int ZMQ_RATE = 8;
		public static final int ZMQ_MCAST_LOOP = 10;
		public static final int ZMQ_PUB = 1;
		public static final int ZMQ_PULL = 7;
		public static final int ZMQ_REP = 4;
		public static final int ZMQ_REQ = 3;
		public static final int ZMQ_RCVMORE = 13;
		public static final int ZMQ_IDENTITY = 5;
		public static final int ZMQ_QUEUE = 1;
		public static final int ZMQ_RECOVERY_IVL = 9;
		public static final int ZMQ_HWM = 1;
		public static final int ZMQ_DELIMITER = 31;
		public static final int ZMQ_HAUSNUMERO = 156384712;
		public static final int ZMQ_XREP = 6;
		public static final int ZMQ_XREQ = 5;
		public static final int ZMQ_STREAMER = 3;
		public static final int ZMQ_POLLERR = 4;
		public static final int ZMQ_POLLIN = 1;
		public static final int ZMQ_SWAP = 3;
		public static final int ZMQ_POLLOUT = 2;
		public static final int ZMQ_SNDMORE = 2;
		public static final int ZMQ_SNDBUF = 11;
		public static final int ZMQ_UNSUBSCRIBE = 7;
		public static final int ZMQ_RCVBUF = 12;
		public static final int ZMQ_PUSH = 8;
		public static final int ZMQ_MSG_SHARED = 128;
		public static final int ZMQ_MAX_VSM_SIZE = 30;
		public static final int ZMQ_SUBSCRIBE = 6;
		public static final int ZMQ_DOWNSTREAM = TestLibrary.ZMQ_PUSH;
		public static final int EADDRINUSE = (TestLibrary.ZMQ_HAUSNUMERO + 5);
		public static final int ENOCOMPATPROTO = (TestLibrary.ZMQ_HAUSNUMERO + 52);
		public static final int ECONNREFUSED = (TestLibrary.ZMQ_HAUSNUMERO + 7);
		public static final int EFSM = (TestLibrary.ZMQ_HAUSNUMERO + 51);
		public static final int EPROTONOSUPPORT = (TestLibrary.ZMQ_HAUSNUMERO + 2);
		public static final int EINPROGRESS = (TestLibrary.ZMQ_HAUSNUMERO + 8);
		public static final int ENETDOWN = (TestLibrary.ZMQ_HAUSNUMERO + 4);
		public static final int EMTHREAD = (TestLibrary.ZMQ_HAUSNUMERO + 50);
		public static final int ENOBUFS = (TestLibrary.ZMQ_HAUSNUMERO + 3);
		public static final int EADDRNOTAVAIL = (TestLibrary.ZMQ_HAUSNUMERO + 6);
		public static final int ZMQ_UPSTREAM = TestLibrary.ZMQ_PULL;
		public static final int ENOTSUP = (TestLibrary.ZMQ_HAUSNUMERO + 1);
		public static final int ETERM = (TestLibrary.ZMQ_HAUSNUMERO + 53);

		void zmq_version(IntByReference major, IntByReference minor, IntByReference patch);

		void zmq_version(IntBuffer major, IntBuffer minor, IntBuffer patch);

		int zmq_errno();

		Pointer zmq_strerror(int errnum);

		int zmq_msg_init(test.zmq_msg_t msg);

		int zmq_msg_init_size(test.zmq_msg_t msg, NativeSize size);

		int zmq_msg_init_data(test.zmq_msg_t msg, Pointer data, NativeSize size, TestLibrary.zmq_free_fn ffn, Pointer hint);

		int zmq_msg_close(test.zmq_msg_t msg);

		int zmq_msg_move(test.zmq_msg_t dest, test.zmq_msg_t src);

		int zmq_msg_copy(test.zmq_msg_t dest, test.zmq_msg_t src);

		Pointer zmq_msg_data(test.zmq_msg_t msg);

		NativeSize zmq_msg_size(test.zmq_msg_t msg);

		Pointer zmq_init(int io_threads);

		int zmq_term(Pointer context);

		Pointer zmq_socket(Pointer context, int type);

		int zmq_close(Pointer s);

		int zmq_setsockopt(Pointer s, int option, Pointer optval, NativeSize optvallen);

		int zmq_getsockopt(Pointer s, int option, Pointer optval, NativeSizeByReference optvallen);

		int zmq_bind(Pointer s, Pointer addr);

		int zmq_bind(Pointer s, java.lang.String addr);

		int zmq_connect(Pointer s, Pointer addr);

		int zmq_connect(Pointer s, java.lang.String addr);

		int zmq_send(Pointer s, test.zmq_msg_t msg, int flags);

		int zmq_recv(Pointer s, test.zmq_msg_t msg, int flags);

		int zmq_poll(test.zmq_pollitem_t items, int nitems, NativeLong timeout);

		int zmq_device(int device, Pointer insocket, Pointer outsocket);
		/// Pointer to unknown (opaque) type

		public static class zmq_free_fn extends PointerType {
			public zmq_free_fn(Pointer address) {
				super(address);
			}

			public zmq_free_fn() {
				super();
			}
		};
	}

/**
 * <i>native declaration : /usr/include/errno.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
//public class zmq_msg_t extends Structure {
//	/// C type : void*
//	public Pointer content;
//	public byte flags;
//	public byte vsm_size;
//	/// C type : unsigned char[30]
//	public byte[] vsm_data = new byte[(30)];
//	public zmq_msg_t() {
//		super();
//	}
//	/**
//	 * @param content C type : void*<br>
//	 * @param vsm_data C type : unsigned char[30]
//	 */
//	public zmq_msg_t(Pointer content, byte flags, byte vsm_size, byte vsm_data[]) {
//		super();
//		this.content = content;
//		this.flags = flags;
//		this.vsm_size = vsm_size;
//		if (vsm_data.length != this.vsm_data.length)
//			throw new java.lang.IllegalArgumentException("Wrong array size !");
//		this.vsm_data = vsm_data;
//	}
//	public static class ByReference extends zmq_msg_t implements Structure.ByReference {
//
//	};
//	public static class ByValue extends zmq_msg_t implements Structure.ByValue {
//
//	};
//}

/*public class zmq_pollitem_t extends Structure {
	/// C type : void*
	public Pointer socket;
	/// Conversion Error : SOCKET
	public short events;
	public short revents;
	public zmq_pollitem_t() {
		super();
	}
	/// @param socket C type : void*
	public zmq_pollitem_t(Pointer socket, short events, short revents) {
		super();
		this.socket = socket;
		this.events = events;
		this.revents = revents;
	}
	public static class ByReference extends zmq_pollitem_t implements Structure.ByReference {

	};
	public static class ByValue extends zmq_pollitem_t implements Structure.ByValue {

	};
}
*/

