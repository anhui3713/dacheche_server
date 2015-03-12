package com.imooc.dacheche.common;

import java.util.UUID;

public class Utils {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String getId() {
		return UUID.randomUUID().toString();
	}
	
	public static void delay(final Runnable run, final long millis) {
		synchronized (run) {
			new Thread() {
				public void run() {
					Utils.sleep(millis);
					run.run();
				}
			}.start();
		}
	}
}
