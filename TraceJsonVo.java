package com.lgcns.test;

import java.util.List;
import java.util.Map;

public class TraceJsonVo {
	public transient int id;
	public String target;
	public String status;
	public List <TraceJsonVo> services;
	
	@Override
	public String toString() {
		return "{ target=" + target + ", status = " + status + "\n\tservices = " + services + " }\n";
	}
}
