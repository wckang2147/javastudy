package com.lgcns.test;

import java.io.IOException;

public class RunManager {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String[] input = WckangFile.readConsole(" ");
		
		String serviceName = WckangFile.findService(input[0],input[1]);
		WckangFile.readFileContents(serviceName);
		
	}

}
