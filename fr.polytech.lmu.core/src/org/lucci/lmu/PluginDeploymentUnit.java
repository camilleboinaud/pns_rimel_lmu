package org.lucci.lmu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PluginDeploymentUnit extends DeploymentUnit {
	
	
	public PluginDeploymentUnit(String filePath){
		super(filePath);
		
		String name = manifest.getMainAttributes().getValue("Bundle-SymbolicName");
		
		if (name == null) {
			String[] pathTokens = filePath.split("/");
			name = pathTokens[pathTokens.length - 1];
		}
		
		if (name.contains(";")) {
			name = name.split(";")[0];
		}
		
		setName(name);
	}
	
	@Override
	protected void retrieveDescriptionFile() {
		try {			
			if (new File(filePath).isDirectory()) {
				manifest = new Manifest(new FileInputStream(filePath + "/META-INF/MANIFEST.MF"));
			}
			else {
				manifest =  new JarFile(filePath).getManifest();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<String> retrieveDependencies(){
		List <String> dependancies = new ArrayList<>();
		
		Attributes attributes = manifest.getMainAttributes();
		
		String bundlesString = attributes.getValue("Require-Bundle");
		if (bundlesString != null) {
			
			for (String dep : bundlesString.split(",")) {
				if (dep.contains(";")) {
					dep = dep.split(";")[0];
				}
								
				dependancies.add(dep);
			}
		}

		return dependancies;
	}

}
