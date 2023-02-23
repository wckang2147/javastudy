package com.lgcns.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;

class Test {
	public transient int seq;
	//@SerializedName("testId") 
	public int id;
	public String name;
	
	public Test(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public Test() {
		// TODO Auto-generated constructor stub
		this.id = 1;
		this.name = "test";
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Test Class [id=" + id + ", name=" + name + "]";
	}
}
//{
//	  "id": 1,
//	  "employee": [
//	    {
//	      "id": 12345,
//	      "name": "Kang"
//	    },
//	    {
//	      "id": 12346,
//	      "name": "Kim"
//	    }
//	  ],
//	  "team": {
//	    "name": "Security",
//	    "leader": "Kim"
//	  }
//	}
class CompanyVo {
	int id;
	List <Map<String, Object>> employee;
	Map <String, Object> team;
	
	@Override
	public String toString()
	{
		return "Company [id = " + id + ", employee= " + employee + ", team= " + team + "]"; 
	}
}
public class MyJsonParser {
	
    // ExclusionStrategy 
    static ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            if ("id".equals(fieldAttributes.getName())) {
                return true;
            }
            return false;
        }

        public boolean shouldSkipClass(Class aClass) {
            return false;
        }
    };

	public static void main(String[] args) throws Exception {
		parseJsonSimple();
		
		// parse json by elements methods
		parseJsonElements();
		
		// loading json from file to class
		readJsonFile();
		
		writeJsonFile();
				
		// exclusion id field
//        Gson gson = new GsonBuilder().setExclusionStrategies(exclusionStrategy).create();

	}

	private static void readJsonFile() {
		try {
			Reader reader = new FileReader(".\\json.txt");

//			Gson gson = new Gson();
			
			// below method is supported at 2.10.1
			Gson gson = new GsonBuilder()				
					  .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL).create();
			CompanyVo company = gson.fromJson(reader, CompanyVo.class); 
			System.out.println(company);
			System.out.println( gson.toJson(company) );
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void writeJsonFile() throws Exception {
		try {
			Reader reader = new FileReader(".\\json.txt");

//			Gson gson = new Gson();
			
			// below method is supported at 2.10.1
			Gson gson = new GsonBuilder().setPrettyPrinting()				
					  .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL).create();
			
			CompanyVo company = gson.fromJson(reader, CompanyVo.class); 
			System.out.println(company);
			System.out.println( gson.toJson(company) );

			// write json to file
			FileWriter fw = new FileWriter("./output.txt");
			gson.toJson(company, fw);
			fw.flush();
			fw.close();

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private static void parseJsonSimple() {
		// TODO Auto-generated method stub
		// Create simple json object
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "test");
		jsonObject.addProperty("id", 1);
		
		String jsonStr = gson.toJson(jsonObject);
		System.out.println(jsonStr);
		
		// String to Json element
		JsonElement jsonElement = JsonParser.parseString("{ \"key\":\"value\" }");
		System.out.println(jsonElement.toString());
		
		// Class to Json
		Test testClass = new Test();
		testClass.id = 1;
		testClass.name = "name1";
		Gson gsonFromClass = new Gson();
		String testJson = gsonFromClass.toJson(testClass);
		System.out.println(testJson);
		
		// json string to class
		String testStr = "{\"id\":1,\"name\":\"test\"}";
		Gson gsonToClass = new Gson();
		Test testClass2 = gsonToClass.fromJson(testStr, Test.class);
		System.out.println(testClass2);
		
		// JSON print format
		Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create(); // .serializeNulls()
		
		Test testClass3 = gsonPretty.fromJson(testStr, Test.class);
		String prettyJson = gsonPretty.toJson(testClass3);
		System.out.println(prettyJson);
		
		// Java List to JSON object
		Gson gsonList = new GsonBuilder().setPrettyPrinting().create();
		Test testList1 = new Test(1, "test1");
		Test testList2 = new Test(2, "test2");
		List<Test> testLists = Arrays.asList(testList1, testList2);
		String jsonStr1 = gsonList.toJson(testLists);
		System.out.println(jsonStr1);
		
		// Make set from json
		String jsonString = "[\"abc\",\"def\",\"ghi\"]";
		Gson gson1 = new Gson();
		java.lang.reflect.Type setType = new TypeToken<HashSet<String>>(){}.getType();
		Set<String> alpha = gson1.fromJson(jsonString, setType);
		System.out.println(alpha);
	}

	private static void parseJsonElements() {
//		{
//		    "id" : 1,
//		    "names": [
//		        "wck", "kang"
//		    ],
//		    "visitor": {
//		        "title" : "mr",
//		        "name" : "kim"
//		    }
//		}
        String json = "{\"id\":1,\"names\": [\"wck\", \"kang\"],\"visitor\":{\"title\":\"mr\",\"name\":\"kim\"}}";
        
        // parse json string
        JsonElement element = JsonParser.parseString(json);
 
        // get JsonObject 
        JsonObject object = element.getAsJsonObject();
 
        // get id
        long id = object.get("id").getAsLong();
        System.out.println("id = " + id); 
 
        // get array
        // JsonArray namesJsonArray = object.getAsJsonArray("names");
        JsonArray namesJsonArray = object.get("names").getAsJsonArray();
        namesJsonArray.forEach(item -> {
        	System.out.println(item.toString());
        });
//        for (int i = 0; i < namesJsonArray.size(); i++) {
//            String name = namesJsonArray.get(i).getAsString();
//            System.out.println("name[" + i + "] : " + name); 
//        }
 
        JsonObject visitorJsonObject = object.get("visitor").getAsJsonObject();
        // JsonObject visitorJsonObject = object.getAsJsonObject("visitor");
        String visitorTitle = visitorJsonObject.get("title").getAsString();
        String visitorName = visitorJsonObject.get("name").getAsString();
        System.out.println(visitorTitle + "." + visitorName);
	}

}
