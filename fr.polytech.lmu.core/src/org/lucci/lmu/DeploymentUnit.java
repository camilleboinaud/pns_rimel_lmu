package org.lucci.lmu;

import java.util.List;

public abstract class DeploymentUnit extends NamedModelElement {
	
	protected String directory;
	protected String filePath;
	
	protected DeploymentUnit(String filePath){
		this.filePath = filePath;
		
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
