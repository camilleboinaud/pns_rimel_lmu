package org.lucci.lmu.input;

import java.io.IOException;

import org.lucci.lmu.test.DynamicCompiler;

import toools.io.file.RegularFile;

public class ClassFileCompiler {
	
	public Class<?> compile (String path, String name) throws IOException {
		RegularFile file = new RegularFile(path);
		String source = new String(file.getContent());
		
		Class<?> clazz = DynamicCompiler.compile(name, source);
		
		return clazz;
	}
}
