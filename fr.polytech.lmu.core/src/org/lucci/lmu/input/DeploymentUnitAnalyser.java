package org.lucci.lmu.input;

import java.util.List;

import org.lucci.lmu.AssociationRelation;
import org.lucci.lmu.DeploymentUnit;
import org.lucci.lmu.DeploymentUnitType;
import org.lucci.lmu.Entity;
import org.lucci.lmu.JarDeploymentUnit;
import org.lucci.lmu.Model;

public class DeploymentUnitAnalyser implements ModelAnalyser {


	protected String filePath;
	protected DeploymentUnitType type;
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
		Model model = new Model();
		
		Entity root = new Entity();
		root.setName(du.getName());
		
		model.addEntity(root);
		
		analyseRecursiveJarDependencies(root, model, du.retrieveDependencies(), depth);		
		
		return model;
	}
	
	
	private void analyseRecursiveJarDependencies(Entity root, Model model, List<String> dependencies, int depth){
			
		for(String dependency : dependencies){			
			DeploymentUnit du = new JarDeploymentUnit(dependency);
				
			Entity entity = new Entity();
			entity.setName(du.getName());
				
			model.addEntity(entity);
			model.addRelation(new AssociationRelation(root, entity));
			
			if(depth > 1){
				analyseRecursiveJarDependencies(entity, model, du.retrieveDependencies() , depth--);
			}
			
		}
		
		
	}

}
