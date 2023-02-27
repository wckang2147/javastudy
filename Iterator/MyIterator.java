package com.lgcns.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



public class MyIterator {

	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Test1", "TTT0");
		map.put("Test2", "TTT1");
		map.put("Test3", "TTT2");
		if (!map.isEmpty()) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			}
		}
		else
		{
			System.out.println("Map is empty");
		}
		System.out.println("-1------");
		Iterator<Entry<String, String>> entries1 = map.entrySet().iterator();
		while (entries1.hasNext()) {
		    Map.Entry entry = (Map.Entry) entries1.next();
		    String key = (String)entry.getKey();
		    String value = (String)entry.getValue();
		    System.out.println("Key = " + key + ", Value = " + value);
		    if (key.equals("Test1")) {
		    	entries1.remove();
		    	System.out.println("Removed " + key);
		    }
		}
		System.out.println("-2------");

		Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
		    Map.Entry<String, String> entry = entries.next();
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		System.out.println("-3------");
		// keys iterator
		for (String key : map.keySet()) {
		    System.out.println("Key = " + key);
		}
		// values  iterator
		for (String value : map.values()) {
		    System.out.println("Value = " + value);
		}
		

	}

}
