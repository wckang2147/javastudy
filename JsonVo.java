package com.lgcns.test;

import java.util.List;
import java.util.Map;

public class JsonVo {
	public int port;
	public List<Map<String, Object>> routes;
	// public Map<String, Object> subject;

	@Override
	public String toString() {
		return "Proxy [port=" + port + ", routes =" + routes + "]";
	}
}
