package org.lucci.lmu.input;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.lucci.lmu.Entity;
import org.lucci.lmu.Model;
import org.lucci.lmu.test.DynamicCompiler;

import toools.ClassContainer;
import toools.ClassName;
import toools.ClassPath;
import toools.Clazz;
import toools.io.FileUtilities;
import toools.io.file.RegularFile;

/*
 * Created on Oct 11, 2004
 */

/**
 * @author luc.hogie
 */
public class JarFileAnalyser implements ModelAnalyser, FileContentBasedAnalyser
{
	private Collection<RegularFile> knownJarFiles = new HashSet<RegularFile>();

	public Collection<RegularFile> getJarFiles()
	{
		return this.knownJarFiles;
	}
	
	private byte[] data;
	
	public JarFileAnalyser(byte[] data) {
		this.data = data;
	}

	public Model analyse() throws Exception
	{				
		try
		{
			// create a jar file on the disk from the binary data
			RegularFile jarFile = RegularFile.createTempFile("lmu-", ".jar");
			jarFile.setContent(data);

			ClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toURL() });

			ClassPath classContainers = new ClassPath();
			classContainers.add(new ClassContainer(jarFile, classLoader));

			for (RegularFile thisJarFile : this.knownJarFiles)
			{
				classContainers.add(new ClassContainer(thisJarFile, classLoader));
			}

			ModelFiller modelFiller = new ModelFiller();

			Model model = modelFiller.createModel(classContainers.listAllClasses());
			
			jarFile.delete();
			
			return model;
		}
		catch (IOException ex)
		{
			throw new IllegalStateException();
		}

	}

	protected static Class<?> createClassNamed(String fullName)
	{
		ClassName cn = Clazz.getClassName(fullName);
		String src = "";

		if (cn.pkg != null)
		{
			src += "package " + cn.pkg + ";";
		}

		src += "public class " + cn.name + " {}";

		// System.out.println(src);
		return DynamicCompiler.compile(fullName, src);
	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println(createClassNamed("lucci.Coucou"));
	 * System.out.println(createClassNamed("Coucou")); }
	 */

	public String computeEntityName(Class<?> c)
	{
		return c.getName().substring(c.getName().lastIndexOf('.') + 1);
	}

	public String computeEntityNamespace(Class<?> c)
	{
		return c.getPackage() == null ? Entity.DEFAULT_NAMESPACE : c.getPackage().getName();
	}

	public Model createModel(File file) throws Exception
	{
		data = FileUtilities.getFileContent(file);
		return analyse();
	}
	
	public JarFileAnalyser() {
		
	}

	@Override
	public Model analyse(byte[] data) throws Exception {
		this.data = data;
		return analyse();
	}
}
