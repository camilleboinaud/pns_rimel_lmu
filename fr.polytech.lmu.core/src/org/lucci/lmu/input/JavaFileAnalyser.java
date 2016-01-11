package org.lucci.lmu.input;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.lucci.lmu.Model;
import org.lucci.lmu.test.DynamicCompiler;

import toools.io.file.RegularFile;

public class JavaFileAnalyser extends ModelFactory {

	@Override
	public Model createModel(Object... data) throws Exception {
		
		String path = (String) data[0];
		String name = (String) data[1];
		
		System.out.println(path);
		System.out.println(name);
		
		RegularFile file = new RegularFile(path);
		String source = new String(file.getContent());
		
		List<Class<?>> classes = new ArrayList<>();
		Class<?> clazz = DynamicCompiler.compile(name, source);
		classes.add(clazz);
		
		ModelFiller modelFiller = new ModelFiller();
		return modelFiller.createModel(classes);		
	}
	
}
