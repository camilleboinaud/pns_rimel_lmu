package org.lucci.lmu;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.eclipse.osgi.storage.ManifestLocalization;

public class PluginDeploymentUnit extends JarDeploymentUnit {
	
	protected Manifest manifest;
	
	public PluginDeploymentUnit(String filePath){
		super(filePath);
		
		String name = manifest.getMainAttributes().getValue("Bundle-SymbolicName");
		
		if (name == null) {
			String[] pathTokens = filePath.split("/");
			name = pathTokens[pathTokens.length - 1];
		}
		
		setName(name);
	}
	
	@Override
	protected void retrieveDescriptionFile() {
		try {
			System.out.println("lol");
			
			manifest = new Manifest(new FileInputStream(filePath + "/META-INF/MANIFEST.MF"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<String> retrieveDependencies(){
		List <String> dependancies = new ArrayList<>();
		
		Attributes attributes = manifest.getMainAttributes();
		
		String bundlesString = attributes.getValue("Bundle-ClassPath");
		if (bundlesString != null) {
			dependancies.addAll(Arrays.asList(bundlesString.split(",")));
		}
		
		bundlesString = attributes.getValue("Require-Bundle");
		if (bundlesString != null) {
			dependancies.addAll(Arrays.asList(bundlesString.split(",")));
		}

		return dependancies;
	}

}
