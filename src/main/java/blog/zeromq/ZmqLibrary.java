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

	static final int ZMQ_MAX_VSM_SIZE = 30;
	static final int ZMQ_DELIMITER = 31;
	static final int ZMQ_VSM = 32;

	int ZMQ_MSG_MORE = 1;

	int zmq_msg_init(zmq_msg_t msg);

	int zmq_msg_init_size(zmq_msg_t msg, NativeLong size);

	int zmq_msg_init_data(zmq_msg_t msg, Pointer data, NativeLong size, zmq_free_fn ffn, Pointer hint);

	int zmq_msg_close(zmq_msg_t msg);

	int zmq_msg_move(zmq_msg_t dest, zmq_msg_t src);

	int zmq_msg_copy(zmq_msg_t dest, zmq_msg_t src);

	Pointer zmq_msg_data(zmq_msg_t msg);

	NativeLong zmq_msg_size(zmq_msg_t msg);

	Pointer zmq_init(int io_threads);

	int zmq_term(Pointer context);

	static final int ZMQ_PAIR = 0;
	static final int ZMQ_PUB = 1;
	static final int ZMQ_SUB = 2;
	static final int ZMQ_REQ = 3;
	static final int ZMQ_REP = 4;
	static final int ZMQ_XREQ = 5;
	static final int ZMQ_XREP = 6;
	static final int ZMQ_PULL = 7;
	static final int ZMQ_PUSH = 8;
	static final int ZMQ_UPSTREAM = ZMQ_PULL	; /*  Old alias, remove in 3.x              */
	static final int ZMQ_DOWNSTREAM = ZMQ_PUSH	; /*  Old alias, remove in 3.x              */
	
	static final int ZMQ_HWM = 1;
/* static final int ZMQ_LWM = 2  no longer supported */
 static final int ZMQ_SWAP = 3;
 static final int ZMQ_AFFINITY = 4;
 static final int ZMQ_IDENTITY = 5;
 static final int ZMQ_SUBSCRIBE = 6;
 static final int ZMQ_UNSUBSCRIBE = 7;
 static final int ZMQ_RATE = 8;
 static final int ZMQ_RECOVERY_IVL = 9;
 static final int ZMQ_MCAST_LOOP = 10;
 static final int ZMQ_SNDBUF = 11;
 static final int ZMQ_RCVBUF = 12;
 static final int ZMQ_RCVMORE = 13;

	static final int ZMQ_NOBLOCK = 1;
	static final int ZMQ_SNDMORE = 2;

	Pointer zmq_socket (Pointer context, int type);
	int zmq_close (Pointer socket);

}