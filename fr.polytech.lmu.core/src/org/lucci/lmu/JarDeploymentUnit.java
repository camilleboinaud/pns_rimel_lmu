package org.lucci.lmu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarDeploymentUnit extends DeploymentUnit {
	
	protected Manifest manifest;
	
	public JarDeploymentUnit(String filePath){
		super(filePath);
		
		String name = manifest.getMainAttributes().getValue("Name");
		
		if (name == null) {
			name = manifest.getMainAttributes().getValue("Bundle-SymbolicName");
		}
		
		if (name == null) {
			String[] pathTokens = filePath.split("/");
			name = pathTokens[pathTokens.length - 1];
		}
		
		setName(name);
	}
	
	@Override
	protected void retrieveDescriptionFile() {
		try {
			manifest =  new JarFile(filePath).getManifest();
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
			for (String dep : bundlesString.split(",")) {
				if (dep.contains(";")) {
					dep = dep.split(";")[0];
				}
				
				dependancies.add(dep);
			}		
		}
		
		bundlesString = attributes.getValue("Class-Path");
		if(bundlesString != null){
			dependancies.addAll(Arrays.asList(bundlesString.split(" ")));
		}

		return dependancies;
	}


}
