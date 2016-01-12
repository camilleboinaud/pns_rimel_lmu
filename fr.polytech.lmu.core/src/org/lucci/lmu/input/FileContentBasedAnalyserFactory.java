package org.lucci.lmu.input;

import java.util.HashMap;
import java.util.Map;

public class FileContentBasedAnalyserFactory {
	
	private static FileContentBasedAnalyserFactory instance = null;
	
	public static FileContentBasedAnalyserFactory getInstance() {
		if (instance == null) {
			instance = new FileContentBasedAnalyserFactory();
		}
		
		return instance;
	}
	
	private Map <String, Class<? extends FileContentBasedAnalyser>> analyserMap;
	
	public FileContentBasedAnalyser getAnalyser(String type) throws Exception {
		return analyserMap.get(type).newInstance();
	}
	
	private FileContentBasedAnalyserFactory() {
		analyserMap = new HashMap<>();
		analyserMap.put("jar", JarFileAnalyser.class);
		analyserMap.put("jar", LmuParser.class);
	}
}
