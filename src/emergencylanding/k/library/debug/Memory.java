package emergencylanding.k.library.debug;

import k.core.util.Helper;

public class Memory {
	private static long lastF = Memory.getFree();
	private static long lastM = Memory.getMax();
	private static long lastT = Memory.getTotal();

	private Memory() {
		System.out.println("AHEM! Don't create this class!");
		byte[] chrs = "POTATO".getBytes();
		int res = 0;
		for (byte b : chrs) {
			res += b;
		}
		System.exit(res);
	}

	public static void printFree() {
		long free = Memory.getFree();
		System.out.println("FREE_MEM: " + free);
		Memory.lastF = free;
	}

	public static void printMax() {
		long max = Memory.getMax();
		System.out.println("MAX_MEM: " + max);
		Memory.lastM = max;
	}

	public static void printTotal() {
		long total = Memory.getTotal();
		System.out.println("TOTAL_MEM: " + total);
		Memory.lastT = total;
	}

	public static long getFree() {
		return Runtime.getRuntime().freeMemory();
	}

	public static long getMax() {
		return Runtime.getRuntime().maxMemory();
	}

	public static long getTotal() {
		return Runtime.getRuntime().totalMemory();
	}

	public static void printAll() {
		Memory.printFree();
		Memory.printMax();
		Memory.printTotal();
	}

	public static void comparePrint() {
		String[] lines = new String[3];
		if (Memory.getFree() < Memory.lastF) {
			lines[0] = "Free memory is less.";
		} else {
			lines[0] = "Free memory is more!";
		}

		if (Memory.getMax() < Memory.lastM) {
			lines[1] = "Max memory is less.";
		} else {
			lines[1] = "Max memory is more!";
		}

		if (Memory.getTotal() < Memory.lastT) {
			lines[2] = "Total memory is less.";
		} else {
			lines[2] = "Total memory is more!";
		}
		Helper.Arrays.dump(lines);
	}

	public static void gc() {
		System.gc();
	}

}
