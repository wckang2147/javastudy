package com.lgcns.test;

public class MyThread implements Runnable {

	private int seq;
	public MyThread(int seq){
		this.seq = seq;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Thread " + seq);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
