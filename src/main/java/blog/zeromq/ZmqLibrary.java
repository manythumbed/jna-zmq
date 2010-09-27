package blog.zeromq;

import com.sun.jna.Library;

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
	static final int EINPROGRESS  = 8;

	static final int EMTHREAD = 50;
	static final int EFSM  = 51;
	static final int ENOCOMPATPROTO = 52;
	static final int ETERM = 53;

	int zmq_errno();
	String zmq_strerror(int errnum);
}