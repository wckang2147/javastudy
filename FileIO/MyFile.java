package com.lgcns.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class MyFile {

	public static BufferedReader openFile(String filename) throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader(filename));// , Charset.forName("UTF-8")));
		return fileReader;
	}

	public static void readFileContents(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));// , Charset.forName("UTF-8")));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}
	
	public static void writeFileContents(String filename, String[] contents) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true )); // true = append
		
		for (String string : contents) {
			bw.write(string);
			bw.write("\n");
		}
		bw.close();
	}
	public static void printFileContents(String filename, String[] contents) throws IOException {
		PrintWriter pw = new PrintWriter ( new BufferedWriter(new FileWriter(filename, true )) ); // true = append
		
		for (String string : contents) {
			pw.println(string);
		}
		pw.close();
	}

	@SuppressWarnings("resource")
	public static String[] readConsole(String delimiter) {
		Scanner sc;
		sc = new Scanner(System.in);
		String line = sc.nextLine();
		String[] str = line.split(delimiter);
		return str;
	}

	public static void readProxy() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		BufferedReader fileReader1 = new BufferedReader(new FileReader(br.readLine() + ".txt"));
		BufferedReader fileReader2 = new BufferedReader(new FileReader(fileReader1.readLine()));
		System.out.println(fileReader2.readLine());
		br.close();
		fileReader1.close();
		fileReader2.close();
	}

	public static String findService(String filenameString, String pathString) throws IOException {
		// TODO Auto-generated method stub

		try (BufferedReader br = new BufferedReader(new FileReader(filenameString + ".txt"))) {
			String line, serviceName = null;
			while ((line = br.readLine()) != null) {

				if (line.startsWith(pathString)) {
					serviceName = line.substring(line.indexOf("#") + 1);
					// System.out.println("serviceName=" + serviceName);

					if (serviceName.startsWith("Proxy")) {
						return findService(serviceName.substring(0, serviceName.indexOf(".")), pathString);
					}

					br.close();
					return serviceName;
				}
			}
		}
		return null;
	}

}
