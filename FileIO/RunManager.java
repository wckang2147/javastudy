package com.lgcns.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class RunManager {

	public static void main(String[] args) throws Throwable {

		String[] msg = {"test", "123"};
		MyFile.writeFileContents("test.txt", msg);
		MyFile.printFileContents("print.txt", msg);
		
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		String line = null;
//		line = br.readLine();
//		BufferedReader fileReader1 = new BufferedReader(new FileReader(line + ".txt"));
//		String serviceFileName = fileReader1.readLine();
//		System.out.println(serviceFileName);
//		fileReader1.close();

	}

}
