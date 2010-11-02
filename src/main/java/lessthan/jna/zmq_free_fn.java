package lessthan.jna;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class zmq_free_fn extends PointerType {
	public zmq_free_fn(Pointer address) {
		super(address);
	}

	public zmq_free_fn() {
		super();
	}
}