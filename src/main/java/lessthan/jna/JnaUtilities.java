package lessthan.jna;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class JnaUtilities {

	public static Memory asMemory(String data) {
		byte[] terminated = Native.toByteArray(data);
		Memory memory = new Memory(terminated.length);
		memory.write(0l, terminated, 0, terminated.length);

		return memory;
	}

	public static String asJavaString(String data) {
		return Native.toString(data.getBytes());
	}

	public static String asJavaString(Pointer pointer)	{
		return asJavaString(pointer.getString(0));
	}


}
