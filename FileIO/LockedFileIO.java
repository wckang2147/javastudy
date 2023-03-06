package com.lgcns.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

class CompanyVo {
	int id;
	String name;

	@Override
	public String toString() {
		return "Company [id = " + id + ", name= " + name + "]";
	}
}

public class LockedFileIO {

	private static void readJsonFile() {
		try {
			Reader reader = new FileReader(".\\test.json");

			Gson gson = new Gson();

			// below method is supported at 2.10.1
			CompanyVo company = gson.fromJson(reader, CompanyVo.class);
			System.out.println(company);
			System.out.println(gson.toJson(company));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		RandomAccessFile raf = new RandomAccessFile("lock.tmp", "rw");
		FileChannel channel = raf.getChannel();

		FileLock lock = null;

		while ((lock = channel.tryLock()) == null) {
			System.out.println("locked");
			Thread.sleep(1000);
		}
		readJsonFile();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String line = null;

		while ((line = br.readLine()) != null) {

			if ("exit".equals(line)) {
				break;
			}
		}
		lock.release();

	}

}
