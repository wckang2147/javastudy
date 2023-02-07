package com.lgcns.test;

import java.util.ArrayList;

import com.google.gson.Gson;

public class TraceJson {

	public static TraceJsonVo trace;
	
	TraceJson()
	{
		Gson gson = new Gson();
		TraceJsonVo vo = new TraceJsonVo();
		vo.target = "test";
		vo.status = "200";
		vo.services = new ArrayList<TraceJsonVo>();
		
		TraceJsonVo vo2 = new TraceJsonVo();
		vo2.target = "test2";
		vo2.status = "201";
		
		vo.services.add(vo2);
		TraceJsonVo vo3 = new TraceJsonVo();
		vo3.target = "test3";
		vo3.status = "202";
		
		vo.services.add(vo3);

		
		String jsonStr = gson.toJson(vo);
		System.out.println("JSON = "+ jsonStr);
	}
}
