package com.lgcns.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyKeyInput {

	public void keyIn() throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String line = null;

		while ((line = br.readLine()) != null) {

			if (line.isEmpty())
				continue;
			if ("exit".equals(line)) {
				break;
			}

			System.out.println("Input value : " + line);

			//String[] commandStrings = line.split(" ");

			
		}

		br.close();
	}
}
