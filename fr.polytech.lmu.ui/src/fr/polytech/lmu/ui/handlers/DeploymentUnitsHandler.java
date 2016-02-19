package fr.polytech.lmu.ui.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.lucci.lmu.DeploymentUnitType;
import org.lucci.lmu.Model;
import org.lucci.lmu.input.DeploymentUnitAnalyser;
import org.lucci.lmu.input.ModelAnalyser;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;

import fr.polytech.lmu.ui.Activator;
import toools.io.file.RegularFile;

public class DeploymentUnitsHandler extends AbstractHandler {
	
	protected static String OUTPUT_DIRECTORY; 
	protected static String OUTPUT_EXTENSION;
	protected static String OUTPUT_NAME;
	protected static int WISHED_DEPTH;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		OUTPUT_EXTENSION = Activator.getDefault().getPreferenceStore().getString("outputExtension");
		OUTPUT_DIRECTORY = Activator.getDefault().getPreferenceStore().getString("outputDirectory");
		OUTPUT_NAME = Activator.getDefault().getPreferenceStore().getString("outputName");
		WISHED_DEPTH = Integer.parseInt(Activator.getDefault().getPreferenceStore().getString("wishedDepth"));
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		MessageDialog.openInformation(
				window.getShell(),
				"lmu-ui",
				"Your file (" + OUTPUT_NAME + "." + OUTPUT_EXTENSION + ") will be registered in the following directory : " + OUTPUT_DIRECTORY);
		
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getSelection();
		
		try {
		
			if (selection instanceof StructuredSelection) {
				
				Object selected = ((StructuredSelection) selection).getFirstElement();			
				ModelAnalyser analyser = null;
	
				if(selected instanceof IPackageFragmentRoot && ((IPackageFragmentRoot)selected).isArchive()){
					System.out.println("Jar file");
					
					IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot) selected;
					analyser = new DeploymentUnitAnalyser(
							packageFragmentRoot.getPath().makeAbsolute().toString(), 
							WISHED_DEPTH, 
							DeploymentUnitType.JAR
					);
				}
				
				if (selected instanceof IJavaProject) {
					System.out.println("Project as plugin");
					
					IJavaProject project = (IJavaProject) selected;
					
					analyser = new DeploymentUnitAnalyser(ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath() + "/" + ((IJavaProject) selected).getPath().makeRelative().toString(), WISHED_DEPTH, DeploymentUnitType.PLUGIN);
				}
	
				
				if (analyser == null) {
					throw new UnsupportedOperationException(selected.toString());
				}
				
				Model model = analyser.analyse();
				RegularFile outputFile = 
						new RegularFile(OUTPUT_DIRECTORY + "/" + OUTPUT_NAME + "." + OUTPUT_EXTENSION);
				AbstractWriter factory = AbstractWriter.getTextFactory(OUTPUT_EXTENSION);
				
				if (factory == null) {
					System.out.println("Do not know how to generate '" + OUTPUT_EXTENSION + "' code\n");
				} else {
					System.out.println(model.getEntities().size() + " entities and " 
							+ model.getRelations().size() + " relations\n");
		
					try {
						System.out.println("Writing file " + outputFile.getPath() + "\n");
						byte[] outputBytes = factory.writeModel(model);
						outputFile.setContent(outputBytes);
					} catch (WriterException ex) {
						System.err.println(ex.getMessage() + "'\n");
					} catch (IOException ex) {
						System.err.println("I/O error while writing file " + outputFile.getPath() + "\n");
					}
				}
			}
		
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

}
