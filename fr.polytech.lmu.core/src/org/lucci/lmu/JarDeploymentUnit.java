package org.lucci.lmu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class JarDeploymentUnit extends DeploymentUnit {
	
	
	public JarDeploymentUnit(String filePath){
		super(filePath);
		
		String name = manifest.getMainAttributes().getValue("Name");
		
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
		
		String bundlesString;
				
		bundlesString = attributes.getValue("Class-Path");
		if(bundlesString != null){
			dependancies.addAll(Arrays.asList(bundlesString.split(" ")));
		}

		return dependancies;
	}


}
