package org.lucci.lmu.input;

import java.util.ArrayList;
import java.util.List;

import org.lucci.lmu.Model;

public class JavaFileListAnalyser extends ModelFactory {
	@Override
	/**
	 * @param data[0] : ClassLoader : the class loader
	 * @param data[1] : List <String> : the class names
	 */
	public Model createModel(Object... data) throws Exception {
		ClassLoader classloader = (ClassLoader) data[0];
		List <String> classNames = (List) data[1];
		
		// Load classes from the ClassLoader
		List <Class<?>> classes = new ArrayList<Class<?>>();
		
		for (String className : classNames) {
			classes.add(classloader.loadClass(className));
		}
				
		ModelFiller modelFiller = new ModelFiller();
		return modelFiller.createModel(classes);		
	}
	
}
