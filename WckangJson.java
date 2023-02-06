package com.lgcns.test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WckangJson {

	public JsonVo loadData(String filename) {

		JsonVo json = null;

		try (FileReader fr = new FileReader(filename)) {

			Gson gson = new Gson();
			json = gson.fromJson(fr, JsonVo.class);

			System.out.println(json);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	public void writeJsonFile(JsonVo json, String filename) {

		try (FileWriter fw = new FileWriter(filename)) {

			// Pretty printing
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			gson.toJson(json, fw);

			System.out.println("Write done.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
