package org.lucci.lmu.input;

import java.util.ArrayList;
import java.util.List;

import org.lucci.lmu.Model;

public class JavaFileListAnalyser implements ModelAnalyser {
	
	private ClassLoader classLoader;
	private List<String> classNames;
	
	public JavaFileListAnalyser(ClassLoader classLoader, List<String> classNames) {
		this.classLoader = classLoader;
		this.classNames = classNames;
	}
	
	@Override
	public Model analyse() throws Exception {
		
		// Load classes from the ClassLoader
		List <Class<?>> classes = new ArrayList<Class<?>>();
		
		for (String className : classNames) {
			classes.add(classLoader.loadClass(className));
		}
				
		ModelFiller modelFiller = new ModelFiller();
		return modelFiller.createModel(classes);		
	}
	
}
