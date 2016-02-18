package org.lucci.lmu.input;

import java.io.File;
import java.util.List;

import org.lucci.lmu.AssociationRelation;
import org.lucci.lmu.DeploymentUnit;
import org.lucci.lmu.DeploymentUnitRelation;
import org.lucci.lmu.DeploymentUnitType;
import org.lucci.lmu.Entity;
import org.lucci.lmu.JarDeploymentUnit;
import org.lucci.lmu.Model;

public class DeploymentUnitAnalyser implements ModelAnalyser {


	protected String filePath;
	protected DeploymentUnitType type;
	protected String directory;
	protected int depth;
	
	public DeploymentUnitAnalyser(String filePath, int depth, DeploymentUnitType type){
		this.filePath = filePath;
		this.type = type;
		this.depth = depth;
	}
	
	@Override
	public Model analyse() throws Exception {
			
		switch(type){
			case JAR:
				return analyseJarUnit();
			case PLUGIN:
				return analysePluginUnit();
		}
		
		return null;
	}
	
	
	private Model analysePluginUnit() {
		return null;
	}	

	
	private Model analyseJarUnit(){
		DeploymentUnit du = new JarDeploymentUnit(filePath);
		directory = (new File(filePath)).getParent();
		Model model = new Model();
			
		model.addEntity(du);
		
		analyseRecursiveJarDependencies(du, model, du.retrieveDependencies(), depth);		
		
		return model;
	}
	
	
	private void analyseRecursiveJarDependencies(DeploymentUnit root, Model model, List<String> dependencies, int depth){
			
		for(String dependency : dependencies){			
			DeploymentUnit du = new JarDeploymentUnit(directory + "/" + dependency);
								
			model.addEntity(du);
			model.addRelation(new DeploymentUnitRelation(root, du));
			
			if(depth > 1){
				analyseRecursiveJarDependencies(du, model, du.retrieveDependencies() , depth-1);
			}
			
		}
		
		
	}

}
