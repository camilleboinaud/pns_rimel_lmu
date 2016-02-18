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
		System.out.println(filePath);
		setName(manifest.getMainAttributes().getValue("Name"));
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
		List<String> res = new ArrayList<String>();
		java.util.jar.Attributes attributes = manifest.getMainAttributes();
		
		String deps = attributes.getValue("Class-Path");
		System.out.println(deps);
		if(deps != null){
			res.addAll(Arrays.asList(deps.split(" ")));
		}
		
		return res;
	}


}
