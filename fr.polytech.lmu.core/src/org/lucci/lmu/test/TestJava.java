package org.lucci.lmu.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.lucci.lmu.Model;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ModelFactory;
import org.lucci.lmu.input.ModelFiller;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;

import toools.ClassContainer;
import toools.ClassPath;
import toools.io.FileUtilities;
import toools.io.file.RegularFile;

public class TestJava {
	
	public static void main (String[] args) throws Exception {
		ModelFiller modelFiller = new ModelFiller();
		
		String sourcePath = "test-resources/TestClass.java";
		
		Model model = ModelFactory.getModelFactory("java").createModel(sourcePath, "org.lucci.lmu.test.TestClass");

		RegularFile outputFile = new RegularFile("test-gen/test.pdf");
		String outputType = FileUtilities.getFileNameExtension(outputFile.getName());
		AbstractWriter factory = AbstractWriter.getTextFactory(outputType);

		if (factory == null) {
			System.out.println("Do not know how to generate '" + outputType + "' code\n");
		} else {
			System.out.println(
					model.getEntities().size() + " entities and " + model.getRelations().size() + " relations\n");

			try {
				System.out.println("Writing file " + outputFile.getPath() + "\n");
				byte[] outputBytes = factory.writeModel(model);
				outputFile.setContent(outputBytes);
			} catch (WriterException ex) {
				System.err.println(ex.getMessage() + "'\n");
			} catch (IOException ex) {
				System.err.println("I/O error while writing file " + outputFile.getPath() + "\n");

			}
		}
	}

}
