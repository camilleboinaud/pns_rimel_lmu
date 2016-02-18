package org.lucci.lmu;

import java.util.List;

public abstract class DeploymentUnit extends Entity {
	
	protected String filePath;
	
	protected DeploymentUnit(String filePath){
		this.filePath = filePath;
		super.setColorName("orange");
		super.getStereoTypeList().add("deployment-unit");
		retrieveDescriptionFile();
		retrieveDependencies();
	}
	
	/**
	 * Method used to retrieve description file (Manifest.mf, plugin.xml, etc.)
	 */
	protected abstract void retrieveDescriptionFile();
	
	/**
	 * Method used to retrieve dependencies associated to deployment unit
	 */
	public abstract List<String> retrieveDependencies();
	
	
}
