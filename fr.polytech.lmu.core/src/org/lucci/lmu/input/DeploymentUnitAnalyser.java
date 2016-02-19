package org.lucci.lmu.input;

import java.io.File;
import java.util.List;

import org.eclipse.core.internal.resources.WorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.pde.core.plugin.ModelEntry;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.lucci.lmu.AssociationRelation;
import org.lucci.lmu.DeploymentUnit;
import org.lucci.lmu.DeploymentUnitType;
import org.lucci.lmu.Entity;
import org.lucci.lmu.JarDeploymentUnit;
import org.lucci.lmu.Model;
import org.lucci.lmu.PluginDeploymentUnit;

public class DeploymentUnitAnalyser implements ModelAnalyser {


	protected String filePath;
	protected DeploymentUnitType type;
	protected int depth;
	protected String directory;
	
	public DeploymentUnitAnalyser(String directory, String filePath, int depth, DeploymentUnitType type){
		this.filePath = filePath;
		this.type = type;
		this.depth = depth;
		this.directory = directory;
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
		DeploymentUnit du = new PluginDeploymentUnit(filePath);
		
		Model model = new Model();
		
		Entity root = new Entity();
		root.setName(du.getName());
		
		model.addEntity(root);
		
		analyzeRecursivePluginDependencies(root, model, du.retrieveDependencies(), depth);		
		
		return model;
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
			String location = directory + "/" + dependency;
			
			if (new File(location).isDirectory()) {
				continue;
			}
			
			try {
				DeploymentUnit du = new JarDeploymentUnit(location);
					
				Entity entity = new Entity();
				entity.setName(du.getName());
					
				model.addEntity(entity);
				model.addRelation(new AssociationRelation(root, entity));
				
				if(depth > 1){
					analyseRecursiveJarDependencies(entity, model, du.retrieveDependencies() , depth--);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void analyzeRecursivePluginDependencies(Entity root, Model model, List<String> dependencies, int depth){
		for(String dependency : dependencies){
			
			ModelEntry dependencyModel = PluginRegistry.findEntry(dependency);
			
			if (dependencyModel == null) {
				System.err.println("did not find entry : " + dependency);
				continue;
			}
			
			String location = dependencyModel.getModel().getInstallLocation();
			
			DeploymentUnit du = new PluginDeploymentUnit(dependencyModel.getModel().getInstallLocation());

			Entity entity = new Entity();
			entity.setName(du.getName());
				
			model.addEntity(entity);
			model.addRelation(new AssociationRelation(root, entity));
			
			if(depth > 1){
				analyzeRecursivePluginDependencies(entity, model, du.retrieveDependencies() , depth - 1);
			}
			
		}
	}
}
