package blog.zeromq;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface ZmqLibrary extends Library {

	void zmq_version(int[] major, int[] minor, int[] patch);

	static final int ZMQ_HAUSNUMERO = 156384712;
	static final int ENOTSUP = 1;
	static final int EPROTONOSUPPORT = 2;
	static final int ENOBUFS = 3;
	static final int ENETDOWN = 4;
	static final int EADDRINUSE = 5;
	static final int EADDRNOTAVAIL = 6;
	static final int ECONNREFUSED = 7;
	static final int EINPROGRESS = 8;

	static final int EMTHREAD = 50;
	static final int EFSM = 51;
	static final int ENOCOMPATPROTO = 52;
	static final int ETERM = 53;

	int zmq_errno();

	String zmq_strerror(int errnum);

	int ZMQ_MAX_VSM_SIZE = 30;
	int ZMQ_DELIMITER = 31;
	int ZMQ_VSM = 32;

	int ZMQ_MSG_MORE = 1;

	int zmq_msg_init(zmq_msg_t msg);
	int zmq_msg_init_size(zmq_msg_t msg, NativeLong size);
	int zmq_msg_init_data(zmq_msg_t msg, Pointer data, NativeLong size, zmq_free_fn ffn, Pointer hint);
	int zmq_msg_close (zmq_msg_t msg);
	int zmq_msg_move (zmq_msg_t dest, zmq_msg_t src);
	int zmq_msg_copy (zmq_msg_t dest, zmq_msg_t src);
	Pointer zmq_msg_data(zmq_msg_t msg);
	NativeLong zmq_msg_size (zmq_msg_t msg);
}