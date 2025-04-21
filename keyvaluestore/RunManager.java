package com.lgcns.test;

public class RunManager {

	    public static void main(String[] args) {
	        String filePath = "sample.txt";
	        String keyDelimiter = "#";
	        String valueDelimiter = ",";

	        // SINGLE 모드 사용
	        FlexibleKeyValueStore storeSingle = new FlexibleKeyValueStore(filePath, keyDelimiter, FlexibleKeyValueStore.Mode.SINGLE, valueDelimiter);
	        System.out.println("=== SINGLE MODE ===");
	        storeSingle.printAll();
	        String resultSingle = storeSingle.get("front");
	        System.out.println("front = " + resultSingle);

	        // LIST 모드 사용
	        FlexibleKeyValueStore storeList = new FlexibleKeyValueStore(filePath, keyDelimiter, FlexibleKeyValueStore.Mode.LIST, valueDelimiter);
	        System.out.println("\n=== LIST MODE ===");
	        storeList.printAll();
	        java.util.List<String> resultList = storeList.get("front");
	        System.out.println("front = " + resultList);
	    }

}
