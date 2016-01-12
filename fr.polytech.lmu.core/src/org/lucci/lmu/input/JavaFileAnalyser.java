package org.lucci.lmu.input;

import java.util.ArrayList;
import java.util.List;

import org.lucci.lmu.Model;

public class JavaFileAnalyser extends ModelFactory {

	@Override
	public Model createModel(Object... data) throws Exception {
		String path = (String) data[0];
		String name = (String) data[1];
		
		ClassFileCompiler compiler = new ClassFileCompiler();
		
		List<Class<?>> classes = new ArrayList<>();
		Class<?> clazz = compiler.compile(path, name);
		classes.add(clazz);
		
		ModelFiller modelFiller = new ModelFiller();
		return modelFiller.createModel(classes);		
	}
	
}
