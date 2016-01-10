package org.lucci.lmu.test;

import java.io.File;
import java.io.IOException;

import org.lucci.lmu.Entities;
import org.lucci.lmu.Entity;
import org.lucci.lmu.Model;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;

import toools.io.FileUtilities;
import toools.io.file.RegularFile;

public class TestScript {

	public static void main(String args[]) throws ParseError, IOException {
		System.out.println(new File(".").getAbsolutePath());
		Model model = new JarFileAnalyser().createModel(new File("test-resources/test.jar"));

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

		/*
		 * Entity e = Entities.findEntityByName(model, "WaitExpression");
		 * System.out.println(e.getName());
		 * System.out.println(e.getAttributes());
		 */
	}
}
