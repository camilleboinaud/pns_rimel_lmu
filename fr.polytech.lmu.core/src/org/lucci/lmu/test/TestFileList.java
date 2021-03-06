package org.lucci.lmu.test;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lucci.lmu.Model;
import org.lucci.lmu.input.JavaFileListAnalyser;
import org.lucci.lmu.input.ModelAnalyser;
import org.lucci.lmu.input.ModelFiller;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;

import toools.io.FileUtilities;
import toools.io.file.RegularFile;

public class TestFileList {
	public static void main(String[] args) throws Exception {		
		String binDir = "test-resources/";
		String classNamesArray[] = {"test.Car"/*,"test.Vehicle","test.Plane"*/};
		List<String> classNames = new ArrayList(Arrays.asList(classNamesArray));
		
		ClassLoader classLoader = new URLClassLoader(new URL[]{new URL("file:/home/lecpie/workspacelmu/fr.polytech.lmu.core/test-resources/")});
		
		ModelAnalyser analyser = new JavaFileListAnalyser(classLoader, classNames);
		
		Model model = analyser.analyse();
		
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
