package com.lgcns.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunManagerKeyIn {
	
	public static void inputKey() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.isEmpty())
				continue;
			if ("exit".equals(line)) {
				break;
			}
			System.out.println("Input value : " + line);
			// String[] commandStrings = line.split(" ");
		}
		br.close();
	}

	public static void main(String[] args)  {

		try {
			inputKey();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		MyKeyInput keyInput = new MyKeyInput();
		try {
			keyInput.keyIn();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/

	}

}
