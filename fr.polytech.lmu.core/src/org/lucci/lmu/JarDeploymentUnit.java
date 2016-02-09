package org.lucci.lmu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarDeploymentUnit extends DeploymentUnit {
	
	protected Manifest manifest;
	
	public JarDeploymentUnit(String filePath){
		super(filePath);
		setName(manifest.getMainAttributes().getValue("Bundle-SymbolicName"));
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
		return new ArrayList<String>(){{
			java.util.jar.Attributes attributes = manifest.getMainAttributes();
			addAll(Arrays.asList(attributes.getValue("Bundle-ClassPath").split(",")));
			addAll(Arrays.asList(attributes.getValue("Require-Bundle").split(",")));
		}};
	}


}
