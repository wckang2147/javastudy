package com.lgcns.test;

import java.util.ArrayList;

public class RunManager {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Thread> threadsList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(new MyThread(i));
			t.start();
			threadsList.add(t);
		}

		for (int i = 0; i < threadsList.size(); i++) {
			Thread t = threadsList.get(i);
			try {
				t.join();
			} catch (Exception e) {
			}
		}
		System.out.println("main end.");
	}

}
