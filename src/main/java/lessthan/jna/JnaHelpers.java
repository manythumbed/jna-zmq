package lessthan.jna;

import com.sun.jna.Memory;
import com.sun.jna.Native;

public class JnaHelpers {

	public static Memory asMemory(String data) {
		byte[] terminated = Native.toByteArray(data);
		Memory memory = new Memory(terminated.length);
		memory.write(0l, terminated, 0, terminated.length);

		return memory;
	}

}
