package in.exchange.rate.util;

import java.util.Random;

public class Utility {

	public static void delay(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void randomDelay() {
		delay(500 + (new Random()).nextInt(2000));
	}

	public static long currentTime() {
		return System.nanoTime() / 1_000_000;
	}

	public static void print(String str) {
		System.out.println(str);
	}

}
