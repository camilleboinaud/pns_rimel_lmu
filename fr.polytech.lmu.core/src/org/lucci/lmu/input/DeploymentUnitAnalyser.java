package org.lucci.lmu.input;

import java.io.File;
import java.util.List;

import org.eclipse.core.internal.resources.WorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.pde.core.plugin.ModelEntry;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.lucci.lmu.AssociationRelation;
import org.lucci.lmu.DeploymentUnit;
import org.lucci.lmu.DeploymentUnitRelation;
import org.lucci.lmu.DeploymentUnitType;
import org.lucci.lmu.Entity;
import org.lucci.lmu.JarDeploymentUnit;
import org.lucci.lmu.Model;
import org.lucci.lmu.PluginDeploymentUnit;
import org.lucci.lmu.Relation;

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
		DeploymentUnit du = new PluginDeploymentUnit(filePath);
		
		Model model = new Model();
		
		analyzeRecursivePluginDependencies(du, model, depth);		
		
		return model;
	}	

	
	private Model analyseJarUnit(){
		DeploymentUnit du = new JarDeploymentUnit(filePath);
		directory = (new File(filePath)).getParent();
		Model model = new Model();
					
		analyseRecursiveJarDependencies(du, model, depth);		
		
		return model;
	}
	
	
	private void analyseRecursiveJarDependencies(DeploymentUnit root, Model model, int depth){
		model.addEntity(root);
		
		for(String dependency : root.retrieveDependencies()){
			String location = directory + "/" + dependency;
			
			if (new File(location).isDirectory()) {
				continue;
			}
			
			try {
				DeploymentUnit du = new JarDeploymentUnit(location);
					
				addEntity(model, root, du);
				
				if(depth > 1){
					analyseRecursiveJarDependencies(du, model, depth - 1);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void analyzeRecursivePluginDependencies(DeploymentUnit root, Model model, int depth){
		model.addEntity(root);
		
		for(String dependency : root.retrieveDependencies()) {
			
			ModelEntry dependencyModel = PluginRegistry.findEntry(dependency);
			
			if (dependencyModel == null) {
				System.err.println("did not find entry : " + dependency);
				continue;
			}
			
			String location = dependencyModel.getModel().getInstallLocation();
			
			DeploymentUnit du = new PluginDeploymentUnit(dependencyModel.getModel().getInstallLocation());

			addEntity(model, root, du);
						
			if(depth > 1){
				analyzeRecursivePluginDependencies(du, model, depth - 1);
			}
		}
		
	}
	
	public void addEntity(Model model, DeploymentUnit root, DeploymentUnit du) {
		boolean found = false;
		
		for (Entity addedEntity : model.getEntities()) {
			if (addedEntity.getName().equals(du.getName())) {
				found = true;
				break;
			}
		}
		
		if (!found) {
			model.addEntity(du);
		}
		
		found = false;
		for (Relation relation : model.getRelations()) {
			Entity source = relation.getHeadEntity();
			Entity dest   = relation.getTailEntity();
			
			if (dest.getName().equals(root.getName()) && source.getName().equals(du.getName()) ) {
				found = true;
				break;
			}
		}
		
		if (!found) {
			model.addRelation(new DeploymentUnitRelation(root, du));
		}
	}
}
